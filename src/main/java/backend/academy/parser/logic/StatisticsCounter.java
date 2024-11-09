package backend.academy.parser.logic;

import backend.academy.parser.model.Fields;
import backend.academy.parser.model.Filter;
import backend.academy.parser.model.HttpStatus;
import backend.academy.parser.model.Log;
import backend.academy.parser.model.Statistic;
import com.google.common.math.Quantiles;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StatisticsCounter {
    private final Filter filter;
    private final List<Log> logs;
    private final List<Integer> bodyByteSentList = new ArrayList<>();
    private final TreeMap<Integer, Integer> codes;
    private final TreeMap<String, Integer> resources;
    private Long bodyByteSentSummary;
    private int countOfRequests;

    public StatisticsCounter(Filter filter, List<Log> logs) {
        this.filter = filter;
        this.logs = logs;
        codes = new TreeMap<>();
        resources = new TreeMap<>();
        bodyByteSentSummary = 0L;
    }

    public Statistic countStatistic(){
        logs.stream()
            .filter(this::filterLog)
            .forEach(this::calculateDataInLog);
        return getStatistic();
    }

    private boolean filterLog(final Log logg) {
        boolean answer = logg.time().isAfter(filter.from()) && logg.time().isBefore(filter.to());
        if(filter.filterField() != null){
            try {
                Fields field = Fields.fromName(filter.filterField().toUpperCase());
                assert field != null;
                Field filterField = logg.getClass().getDeclaredField(field.getField());
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

    private void calculateDataInLog(final Log log) {
        codes.put(log.status(), codes.getOrDefault(log.status(), 0) + 1);
        resources.put(log.resource(), resources.getOrDefault(log.resource(), 0) + 1);
        bodyByteSentSummary += log.bodyByteSent();
        countOfRequests++;
        bodyByteSentList.add(log.bodyByteSent());
    }

    private Statistic getStatistic(){
        var avg = bodyByteSentSummary / countOfRequests;
        double percent = Quantiles.percentiles().index(95).compute(bodyByteSentList.stream().sorted().toList());

        var topStatusCodes = codes.entrySet().stream()
            .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed()) // Сортируем по значению в обратном порядке
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
            .percent95(percent)
            .resources(topResources)
            .statusCodes(topStatusCodes)
            .build();
    }

    private static HttpStatus convertToEnum(final int code) {
        for (HttpStatus status : HttpStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Неизвестный HTTP статус код: " + code);
    }
}
