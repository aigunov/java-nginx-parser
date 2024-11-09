package backend.academy.parser.model;

import java.util.Map;
import lombok.Builder;

@Builder(toBuilder = true)
public record Statistic(int requestCount,
                        Map<HttpStatus, Integer> statusCodes,
                        Map<String, Integer> resources,
                        double avg,
                        double percent95) {
}
