package backend.academy.parser.logic;

import backend.academy.parser.model.Filter;
import backend.academy.parser.model.Log;
import backend.academy.parser.model.Statistic;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class StatisticsCounter {
    private final Filter filter;
    private final Set<Log> logs;
    private final List<Integer> bodyByteSentList = new ArrayList<>();
    private final Map<Integer, Integer> codes;
    private final Map<String, Integer> resources;
    private Long bodyByteSentSummary;
    private final Integer countOfRequests;

    public StatisticsCounter(Filter filter, Set<Log> logs) {
        this.filter = filter;
        this.logs = logs;
        codes = new HashMap<>();
        resources = new HashMap<>();
        bodyByteSentSummary = 0L;
        countOfRequests = logs.size();
    }

    public Statistic countStatistic(){
        logs.stream()
            .filter(this::filterLog)
            .forEach(this::calculateDataInLog);
        return getStatistic();
    }

    private boolean filterLog(final Log log) {
        boolean answer = log.time().isAfter(filter.from()) && log.time().isBefore(filter.to());
        if(!filter.filterField().equals("none")){
            try {
                Field filterField = log.getClass().getDeclaredField(filter.filterField());
                filterField.setAccessible(true);
                if(filterField.getName().equals("status") || filterField.getName().equals("bodyByteSent")){
                    return answer && ((int) filterField.get(log)) == Integer.parseInt(filter.filterValue());
                }else{
                    return answer && String.valueOf(filterField.get(log)).equals(filter.filterValue());
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    private void calculateDataInLog(final Log log) {
        codes.put(log.status(), codes.getOrDefault(log.status(), 0) + 1);
        resources.put(log.resource(), resources.getOrDefault(log.resource(), 0) + 1);
        bodyByteSentSummary += log.bodyByteSent();
        bodyByteSentList.add(log.bodyByteSent());
    }

    private Statistic getStatistic(){
        var avg = bodyByteSentSummary / countOfRequests;
        var percent = calculatePercent95(bodyByteSentList.stream().sorted().toList());

        var topStatusCodes = codes.entrySet().stream()
            .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed()) // Сортируем по значению в обратном порядке
            .limit(3) // Ограничиваем топ 3
            .collect(Collectors.toMap(
                Map.Entry::getKey, // Ключ - статус-код
                Map.Entry::getValue, // Значение - количество
                (e1, e2) -> e1, // Если ключ повторяется, берем первое значение
                LinkedHashMap::new // Сохраняем порядок сортировки
            ));

        var topResources = resources.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(3)
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

    private double calculatePercent95(List<Integer> bytes) {
        double position = (95.0 / 100) * bytes.size(); // Шаг 2: Позиция 95-го перцентиля

        int rank1 = (int) Math.floor(position); // Ближайший ранг с нижней стороны
        int rank2 = (int) Math.ceil(position); // Ближайший ранг с верхней стороны

        if (rank1 == rank2) {
            return bytes.get(rank1); // Если ранги одинаковые, просто берем значение
        } else {
            int x1 = bytes.get(rank1); // Значение с рангом rank1
            int x2 = bytes.get(rank2); // Значение с рангом rank2

            return x1 + ((position - rank1) / (rank2 - rank1)) * (x2 - x1); // Линейная интерполяция
        }
    }
}
