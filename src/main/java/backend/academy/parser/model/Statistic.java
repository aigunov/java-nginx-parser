package backend.academy.parser.model;

import java.util.Map;
import lombok.Builder;

/**
 * Record статистики в который записывается вся подсчитанная информация
 * @param requestCount - кол-во запросов
 * @param statusCodes - Map<HttpStatus, Integer> с кол-вом того или иного http status в порядке убывания по значению
 * @param resources - Map<String, Integer> с кол-вом сколько раз обращались к ресурсам в порядке убывания по значению
 * @param avg - среднее значение body byte sent
 * @param percent95 - 95 перцентиль
 * @param percent90 - 90 перцентиль
 * @param percent99 - 99 перцентиль
 */
@Builder(toBuilder = true)
public record Statistic(int requestCount,
                        Map<HttpStatus, Integer> statusCodes,
                        Map<String, Integer> resources,
                        double avg,
                        double percent95,
                        double percent90,
                        double percent99) {
}
