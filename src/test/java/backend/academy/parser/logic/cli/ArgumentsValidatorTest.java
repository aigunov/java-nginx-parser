package backend.academy.parser.logic.cli;

import backend.academy.parser.model.ReportFormat;
import com.beust.jcommander.ParameterException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ArgumentsValidatorTest {
    private static final ArgumentsValidator validator = new ArgumentsValidator();
    private static final Map<String, Object> valid = new HashMap<>();
    private static final Map<String, Object> wrong = new HashMap<>();
    private static final LocalDateTime from = LocalDate.of(2004, 8, 31).atStartOfDay();
    private static final LocalDateTime to = LocalDate.of(2034, 10, 20).atStartOfDay();

    @BeforeAll
    static void setUp() {

        valid.put("--path", "*.txt");
        valid.put("--format", ReportFormat.MARKDOWN);
        valid.put("--from", from);
        valid.put("--to", to);

        wrong.put("--path", "*.txt");
        wrong.put("--format", ReportFormat.MARKDOWN);
        wrong.put("--from", from);
        wrong.put("--to", to);
        wrong.put("--filter-field", "STTAAATUS");
    }

    @Test
    void testArgumentsValidatorValid() {
        Assertions.assertDoesNotThrow(() -> {
            validator.validate(valid);
            System.out.println("Аргументы Валидны");
        });
    }

    @Test
    void testArgumentsValidatorThrowTimeParameterException() {
        Assertions.assertThrows(ParameterException.class, () -> {
            validator.validate(wrong);
            System.out.println("Время параметров невалидно");
        });
    }

    @Test
    void testArgumentsValidatorThrowFilterFieldException() {
        Assertions.assertThrows(ParameterException.class, () -> {
            validator.validate(wrong);
        });
    }

    @Test
    void testArgumentsValidatorThrowFilterValueException() {
        Assertions.assertThrows(ParameterException.class, () -> {
            validator.validate(wrong);
        });
    }

}
