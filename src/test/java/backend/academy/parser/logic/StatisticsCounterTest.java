package backend.academy.parser.logic;

import backend.academy.parser.logic.service.StatisticsCounter;
import backend.academy.parser.model.Filter;
import backend.academy.parser.model.Log;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StatisticsCounterTest {
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
    private final Filter filter = Filter.builder()
        .from(log.time().minusMinutes(1))
        .to(log.time().plusMinutes(1))
        .filterField("status")
        .filterValue("200")
        .build();



    @Test
    void testFilterLogTimeMatch() {
        var counter = new StatisticsCounter(filter);
        filter.from(log.time().minusMinutes(1));
        filter.to(log.time().plusMinutes(1));
        assertTrue(counter.filterLog(log));
    }

    @Test
    void testFilterLogTimeNotMatch() {
        var counter = new StatisticsCounter(filter);
        filter.from(log.time().plusHours(1));
        filter.to(log.time().plusHours(2));
        assertFalse(counter.filterLog(log));
    }

    @Test
    void testFilterLogFieldMatchStatus() {
        var counter = new StatisticsCounter(filter);
        filter.filterField("status");
        filter.filterValue("200");
        assertTrue(counter.filterLog(log));
    }

    @Test
    void testFilterLogFieldNotMatchStatus() {
        var counter = new StatisticsCounter(filter);
        filter.filterField("status");
        filter.filterValue("404");
        assertFalse(counter.filterLog(log));
    }

    @Test
    void testFilterLogFieldMatchUserAgent() {
        var counter = new StatisticsCounter(filter);
        filter.filterField("http_user_agent");
        filter.filterValue("Mozilla.*");
        assertTrue(counter.filterLog(log));
    }

    @Test
    void testFilterLogFieldNotMatchUserAgent() {
        var counter = new StatisticsCounter(filter);
        filter.filterField("http_user_agent");
        filter.filterValue("Chrome.*");
        assertFalse(counter.filterLog(log));
    }
}


