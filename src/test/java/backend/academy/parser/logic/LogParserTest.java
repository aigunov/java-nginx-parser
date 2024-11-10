package backend.academy.parser.logic;

import backend.academy.parser.model.Log;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import static org.junit.Assert.assertEquals;

class LogParserTest {
    private LogParser logParser = new LogParser();

    @Test
    void testParseLine() {
        String logLine =
            "127.0.0.1 - - [10/Oct/2000:13:55:36 -0700] \"GET /apache_pb.gif HTTP/1.0\" 200 2326 \"-\" \"Mozilla/4.08 [en] (Win98; I)";
        Log expectedLog = Log.builder()
            .ip("127.0.0.1")
            .user("-")
            .time(LocalDateTime.parse("10/Oct/2000:13:55:36 -0700",
                DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH)))
            .request("GET /apache_pb.gif HTTP/1.0")
            .resource("apache_pb.gif")
            .status(200)
            .bodyByteSent(2326)
            .referer("-")
            .userAgent("Mozilla/4.08 [en] (Win98; I)")
            .build();

        Log actualLog = logParser.parseLine(logLine);

        assertEquals(expectedLog, actualLog);
    }

    @Test
    void testParseLineWithNoUser() {
        String logLine =
            "127.0.0.1 - - [10/Oct/2000:13:55:36 -0700] \"GET /apache_pb.gif HTTP/1.0\" 200 2326 \"-\" \"Mozilla/4.08 [en] (Win98; I)";
        Log expectedLog = Log.builder()
            .ip("127.0.0.1")
            .user("-")
            .time(LocalDateTime.parse("10/Oct/2000:13:55:36 -0700",
                DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH)))
            .request("GET /apache_pb.gif HTTP/1.0")
            .resource("apache_pb.gif")
            .status(200)
            .bodyByteSent(2326)
            .referer("-")
            .userAgent("Mozilla/4.08 [en] (Win98; I)")
            .build();

        Log actualLog = logParser.parseLine(logLine);

        assertEquals(expectedLog, actualLog);
    }

    @Test
    void testParseLineWithResourceInPath() {
        String logLine =
            "127.0.0.1 - - [10/Oct/2000:13:55:36 -0700] \"GET /some/path/to/resource.jpg HTTP/1.0\" 200 2326 \"-\" \"Mozilla/4.08 [en] (Win98; I)";
        Log expectedLog = Log.builder()
            .ip("127.0.0.1")
            .user("-")
            .time(LocalDateTime.parse("10/Oct/2000:13:55:36 -0700",
                DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH)))
            .request("GET /some/path/to/resource.jpg HTTP/1.0")
            .resource("resource.jpg")
            .status(200)
            .bodyByteSent(2326)
            .referer("-")
            .userAgent("Mozilla/4.08 [en] (Win98; I)")
            .build();

        Log actualLog = logParser.parseLine(logLine);

        assertEquals(expectedLog, actualLog);
    }
}
