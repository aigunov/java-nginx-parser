package backend.academy.parser.logic.cli;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LocalDateTimeConverterTest {

    private LocalDateTimeConverter converter = new LocalDateTimeConverter();

    @Test
    void testConvertWithTime() {
        String dateString = "2023-12-10T15:30:00";
        LocalDateTime expectedDateTime = LocalDateTime.of(2023, 12, 10, 15, 30, 0);

        LocalDateTime actualDateTime = converter.convert(dateString);

        assertEquals(expectedDateTime, actualDateTime);
    }

    @Test
    void testConvertWithoutTime() {
        String dateString = "2023-12-10";
        LocalDateTime expectedDateTime = LocalDateTime.of(2023, 12, 10, 0, 0, 0);

        LocalDateTime actualDateTime = converter.convert(dateString);

        assertEquals(expectedDateTime, actualDateTime);
    }

    @Test
    void testConvertInvalidFormat() {
        String invalidDateString = "2023-12-10T15:30"; // Некорректный формат

        assertThrows(IllegalArgumentException.class, () -> {
            converter.convert(invalidDateString);
        });
    }
}
