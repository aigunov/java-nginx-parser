package backend.academy.parser.logic.reports;

import backend.academy.parser.model.Filter;
import backend.academy.parser.model.HttpStatus;
import backend.academy.parser.model.ReportFormat;
import backend.academy.parser.model.Statistic;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ADOCReportGeneratorTest {
    private ADOCReportGenerator reportGenerator;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    public void setUp() {
        reportGenerator = new ADOCReportGenerator();
        outputStream = new ByteArrayOutputStream();
    }

    @Test
    public void testGenerateReport() {
        Filter filter = new Filter(
            List.of("access.log"),
            LocalDateTime.of(2024, 8, 31, 0, 0),
            LocalDateTime.now(),
            ReportFormat.ADOC,
            null,
            null,
            null
        );

        Statistic statistic = Statistic.builder()
            .requestCount(10000)
            .statusCodes(Map.of(
                HttpStatus.OK, 8000,
                HttpStatus.NOT_FOUND, 1000,
                HttpStatus.INTERNAL_SERVER_ERROR, 500
            ))
            .resources(Map.of(
                "/index.html", 5000,
                "/about.html", 2000,
                "/contact.html", 1000
            ))
            .avg(500.0)
            .percent95(950.0)
            .percent90(900.0)
            .percent99(990.0)
            .build();

        PrintStream out = new PrintStream(outputStream);

        reportGenerator.generateReport(filter, statistic, out);
        String reportContent = outputStream.toString();

        assertTrue(reportContent.contains("= Отчет по HTTP-запросам"), "Отчет должен содержать заголовок");
        assertTrue(reportContent.contains("Дата отчета: "), "Отчет должен содержать текущую дату");
        assertTrue(reportContent.contains("== Общая информация"), "Отчет должен содержать раздел 'Общая информация'");

        assertTrue(reportContent.contains("| Количество запросов | 10 000"),
            "Отчет должен содержать количество запросов");
        assertTrue(reportContent.contains("| Средний размер ответа | 500.0b"),
            "Отчет должен содержать средний размер ответа");
        assertTrue(reportContent.contains("|   95p размера ответа | 950.0b |"),
            "Отчет должен содержать 95-й перцентиль размера ответа");

        assertTrue(reportContent.contains("== Запрашиваемые ресурсы"),
            "Отчет должен содержать раздел 'Запрашиваемые ресурсы'");
        assertTrue(reportContent.contains("| `/index.html`       | 5 000"),
            "Отчет должен содержать количество запросов для /index.html");
        assertTrue(reportContent.contains("| `/about.html`       | 2 000"),
            "Отчет должен содержать количество запросов для /about.html");

        assertTrue(reportContent.contains("== Статистика по кодам ответа"),
            "Отчет должен содержать раздел 'Статистика по кодам ответа'");
        assertTrue(reportContent.contains("| 200 | OK                     | 8 000"),
            "Отчет должен содержать статус 200 с количеством 8000");
        assertTrue(reportContent.contains("| 404 | NOT_FOUND              | 1 000"),
            "Отчет должен содержать статус 404 с количеством 1000");
        assertTrue(reportContent.contains("| 500 | INTERNAL_SERVER_ERROR  | 500"),
            "Отчет должен содержать статус 500 с количеством 500");

        assertTrue(reportContent.contains("== Дополнительная информация"),
            "Отчет должен содержать раздел 'Дополнительная информация'");
        assertTrue(reportContent.contains("* Количество уникальных ресурсов: 3"),
            "Отчет должен содержать количество уникальных ресурсов");
    }
}
