package backend.academy.parser.logic;

import backend.academy.parser.model.Filter;
import backend.academy.parser.model.Log;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StatisticsCounterTest {
    private final Filter filter = new Filter();
    private final Log log = Log.builder()
        .ip("127.0.0.1")
        .user("testUser")
        .time(LocalDateTime.parse("10/Oct/2000:13:55:36 -0700",
            DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH)))
        .request("GET /some/path/to/resource.jpg HTTP/1.0")
        .resource("resource.jpg")
        .status(200)
        .bodyByteSent(2326)
        .referer("https://example.com")
        .userAgent("Mozilla/5.0")
        .build();
    private final StatisticsCounter counter = StatisticsCounter.getInstance();

    @Test
    void testFilterLogTimeMatch() {
        filter.from(log.time().minusMinutes(1)); // Время "от" на минуту раньше
        filter.to(log.time().plusMinutes(1)); // Время "до" на минуту позже
        assertTrue(counter.filterLog(log, filter));
    }

    @Test
    void testFilterLogTimeNotMatch() {
        filter.from(log.time().plusHours(1)); // Время "от" на час позже
        filter.to(log.time().plusHours(2)); // Время "до" на два часа позже
        assertFalse(counter.filterLog(log, filter));
    }

    @Test
    void testFilterLogFieldMatchStatus() {
        filter.filterField("STATUS");
        filter.filterValue("200");
        assertTrue(counter.filterLog(log, filter));
    }

    @Test
    void testFilterLogFieldNotMatchStatus() {
        filter.filterField("STATUS");
        filter.filterValue("404");
        assertFalse(counter.filterLog(log, filter));
    }

    @Test
    void testFilterLogFieldMatchUserAgent() {
        filter.filterField("HTTP_USER_AGENT");
        filter.filterValue("Mozilla.*");
        assertTrue(counter.filterLog(log, filter));
    }

    @Test
    void testFilterLogFieldNotMatchUserAgent() {
        filter.filterField("HTTP_USER_AGENT");
        filter.filterValue("Chrome.*");
        assertFalse(counter.filterLog(log, filter));
    }
}
