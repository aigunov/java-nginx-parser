package backend.academy.parser.model;

import lombok.Builder;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record Log(
    String ip,
    String user,
    LocalDateTime time,
    String request,
    int status,
    int bodyByteSent,
    String referer,
    String userAgent
) {
}
