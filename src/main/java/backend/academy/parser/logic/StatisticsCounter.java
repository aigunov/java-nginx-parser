package backend.academy.parser.logic;

import backend.academy.parser.model.Fields;
import backend.academy.parser.model.Filter;
import backend.academy.parser.model.HttpStatus;
import backend.academy.parser.model.Log;
import backend.academy.parser.model.Statistic;
import com.google.common.math.Quantiles;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings({"MagicNumber"})
@SuppressFBWarnings({"RFI_SET_ACCESSIBLE"})
/**
 * Класс ответственный за подсчет статистики в файле
 */
@Slf4j
public class StatisticsCounter {
    private final List<Integer> bodyByteSentList = new ArrayList<>();
    private final TreeMap<Integer, Integer> codes;
    private final TreeMap<String, Integer> resources;
    private Long bodyByteSentSummary;
    private int countOfRequests;

    private static StatisticsCounter instance;

    private StatisticsCounter() {
        codes = new TreeMap<>();
        resources = new TreeMap<>();
        bodyByteSentSummary = 0L;
    }

    public static synchronized StatisticsCounter getInstance() {
        if (instance == null) {
            instance = new StatisticsCounter();
        }
        return instance;
    }


    /**
     * Метод конвертирует int статус код ответа http в enum HttpStatus
     */
    private static HttpStatus convertToEnum(final int code) {
        for (HttpStatus status : HttpStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Неизвестный HTTP статус код: " + code);
    }

    /**
     * Метод по  объекту класса filter фильтрует объекты класса Log и отсеивает те что не нужны
     *
     * @return boolean значение о том подходит ли logg условиям переданным в filter
     */
    public boolean filterLog(final Log logg, final Filter filter) {
        boolean answer = logg.time().isAfter(filter.from()) && logg.time().isBefore(filter.to());
        if (filter.filterField() != null) {
            try {
                Fields field = Fields.fromName(filter.filterField().toUpperCase());
                assert field != null;
                Field filterField = logg.getClass().getDeclaredField(field.field());
                filterField.setAccessible(true);

                var pattern = Pattern.compile(filter.filterValue());
                var fieldValue = filterField.get(logg);
                var matcher = pattern.matcher(String.valueOf(fieldValue));

                var f = matcher.matches();
                return answer && f;
            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.error("Ошибка конвертирования по --filter-field");
                throw new RuntimeException(e);
            }
        }
        return answer;
    }

    /**
     * Метод подсчитывает данные для статистики
     * @param log подходящий под условия лог
     */
    void calculateDataInLog(final Log log) {
        codes.put(log.status(), codes.getOrDefault(log.status(), 0) + 1);
        resources.put(log.resource(), resources.getOrDefault(log.resource(), 0) + 1);
        bodyByteSentSummary += log.bodyByteSent();
        countOfRequests++;
        bodyByteSentList.add(log.bodyByteSent());
    }

    /**
     * Метод создает statistic хранящий в себе всю собранную и агрегированную информацию из логов
     * @return объект record Statistic
     */
    Statistic getStatistic() {
        Collections.sort(bodyByteSentList);
        var avg = bodyByteSentSummary / countOfRequests;
        double percent95 = Quantiles.percentiles().index(95).compute(bodyByteSentList);
        double percent90 = Quantiles.percentiles().index(90).compute(bodyByteSentList);
        double percent99 = Quantiles.percentiles().index(99).compute(bodyByteSentList);

        var topStatusCodes = codes.entrySet().stream()
            .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
            .collect(Collectors.toMap(
                entry -> convertToEnum(entry.getKey()), // Преобразуем код статуса в константу HttpStatus
                Map.Entry::getValue, // Значение - количество
                (e1, e2) -> e1, // Если ключ повторяется, берем первое значение
                LinkedHashMap::new // Сохраняем порядок сортировки
            ));

        var topResources = resources.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new
            ));

        return Statistic.builder()
            .avg(avg)
            .requestCount(countOfRequests)
            .percent99(percent99)
            .percent95(percent95)
            .percent90(percent90)
            .resources(topResources)
            .statusCodes(topStatusCodes)
            .build();
    }
}
