package backend.academy.parser.logic;

import backend.academy.parser.model.Filter;
import backend.academy.parser.model.Log;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.*;

class StatisticsCounterTest {
    private Filter filter = new Filter();
    private Log log = Log.builder()
        .ip("127.0.0.1")
        .user("testUser")
        .time(LocalDateTime.parse("10/Oct/2000:13:55:36 -0700", DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH)))
        .request("GET /some/path/to/resource.jpg HTTP/1.0")
        .resource("resource.jpg")
        .status(200)
        .bodyByteSent(2326)
        .referer("https://example.com")
        .userAgent("Mozilla/5.0")
        .build();
    private StatisticsCounter counter = new StatisticsCounter(filter, List.of(log));

    @Test
    void testFilterLogTimeMatch() {
        filter.from(log.time().minusMinutes(1)); // Время "от" на минуту раньше
        filter.to(log.time().plusMinutes(1)); // Время "до" на минуту позже
        assertTrue(counter.filterLog(log));
    }

    @Test
    void testFilterLogTimeNotMatch() {
        filter.from(log.time().plusHours(1)); // Время "от" на час позже
        filter.to(log.time().plusHours(2)); // Время "до" на два часа позже
        assertFalse(counter.filterLog(log));
    }

    @Test
    void testFilterLogFieldMatchStatus() {
        filter.filterField("STATUS");
        filter.filterValue("200");
        assertTrue(counter.filterLog(log));
    }

    @Test
    void testFilterLogFieldNotMatchStatus() {
        filter.filterField("STATUS");
        filter.filterValue("404");
        assertFalse(counter.filterLog(log));
    }

    @Test
    void testFilterLogFieldMatchUserAgent() {
        filter.filterField("HTTP_USER_AGENT");
        filter.filterValue("Mozilla.*");
        assertTrue(counter.filterLog(log));
    }

    @Test
    void testFilterLogFieldNotMatchUserAgent() {
        filter.filterField("HTTP_USER_AGENT");
        filter.filterValue("Chrome.*");
        assertFalse(counter.filterLog(log));
    }
}
