package backend.academy.parser.model;

import java.time.LocalDateTime;
import lombok.Builder;

@SuppressWarnings({"RecordComponentNumber"})
@Builder(toBuilder = true)
public record Log(
    String ip,
    String user,
    LocalDateTime time,
    String request,
    String resource,
    int status,
    int bodyByteSent,
    String referer,
    String userAgent
) {
}
