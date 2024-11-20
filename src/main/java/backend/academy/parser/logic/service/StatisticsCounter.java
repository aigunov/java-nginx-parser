package backend.academy.parser.logic.service;

import backend.academy.parser.model.Filter;
import backend.academy.parser.model.Log;
import backend.academy.parser.model.Statistic;
import com.google.common.math.Quantiles;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings({"MagicNumber"})
@SuppressFBWarnings({"RFI_SET_ACCESSIBLE"})
/**
 * Класс ответственный за подсчет статистики в файле
 */
@Getter
@Slf4j
public class StatisticsCounter {
    private final Filter filter;
    private final List<Integer> bodyByteSentList = new ArrayList<>();
    private final TreeMap<Integer, Integer> codes;
    private final TreeMap<String, Integer> resources;
    private Long bodyByteSentSummary;
    private int countOfRequests;

    public StatisticsCounter(final Filter filter) {
        this.filter = filter;
        codes = new TreeMap<>();
        resources = new TreeMap<>();
        bodyByteSentSummary = 0L;
    }

    private Map<String, Object> createMapOfFields(Log log) {
        return Map.of(
            "remote_addr", log.ip(),
            "remote_user", log.user(),
            "time_local", log.time(),
            "request", log.request(),
            "status", log.status(),
            "body_bytes_sent", log.bodyByteSent(),
            "http_referer", log.referer(),
            "http_user_agent", log.userAgent()
        );
    }

    /**
     * Метод по  объекту класса filter фильтрует объекты класса Log и отсеивает те что не нужны
     *
     * @return boolean значение о том подходит ли logg условиям переданным в filter
     */
    public boolean filterLog(final Log logg) {
        var fields = createMapOfFields(logg);
        boolean answer = logg.time().isAfter(filter.from()) && logg.time().isBefore(filter.to());
        if (filter.filterField() == null) {
            return answer;
        }
        var filterField = fields.get(filter.filterField());
        if (filterField == null) {
            log.error("Ошибка конвертирования по --filter-field");
            throw new RuntimeException();
        }
        var pattern = Pattern.compile(filter.filterValue());
        var matcher = pattern.matcher(String.valueOf(filterField));
        var f = matcher.matches();
        return answer && f;
    }

    /**
     * Метод подсчитывает данные для статистики
     * @param log подходящий под условия лог
     */
    public void calculateDataInLog(final Log log) {
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
    public Statistic getStatistic() {
        Collections.sort(bodyByteSentList);
        double avg = countOfRequests != 0 ? (double) bodyByteSentSummary / countOfRequests : 0;
        double percent95 = Quantiles.percentiles().index(95).compute(bodyByteSentList);
        double percent90 = Quantiles.percentiles().index(90).compute(bodyByteSentList);
        double percent99 = Quantiles.percentiles().index(99).compute(bodyByteSentList);

        var topStatusCodes = codes.entrySet().stream()
            .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new
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
