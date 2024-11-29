package backend.academy.parser.model;

import java.util.Map;
import lombok.Builder;

@Builder(toBuilder = true)
public record Statistic(int requestCount,
                        Map<Integer, Integer> statusCodes,
                        Map<String, Integer> resources,
                        double avg,
                        double percent95,
                        double percent90,
                        double percent99) {
}
