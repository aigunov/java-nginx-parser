package backend.academy.parser.logic.cli;
import com.beust.jcommander.IStringConverter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeConverter implements IStringConverter<LocalDateTime> {

    /**
     * Конвертирует дату из cli в объект LocalDateTime
     * Возможные форматы передаваемые из cli: "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd"
     */
    @Override
    public LocalDateTime convert(String value) {
        DateTimeFormatter formatterWithTime = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        DateTimeFormatter formatterWithoutTime = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            if (value.length() > 10) {
                return LocalDateTime.parse(value, formatterWithTime);
            }
            return LocalDate.parse(value, formatterWithoutTime).atStartOfDay();
        }catch (Exception e) {
            throw new IllegalArgumentException("Некорректный формат даты: " + value);
        }
    }
}

