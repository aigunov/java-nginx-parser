package backend.academy.parser.logic.reports;

import backend.academy.parser.model.Filter;
import backend.academy.parser.model.HttpStatus;
import backend.academy.parser.model.ReportFormat;
import backend.academy.parser.model.Statistic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MDReportGeneratorTest {
    private MDReportGenerator reportGenerator;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    public void setUp() {
        reportGenerator = new MDReportGenerator();
        outputStream = new ByteArrayOutputStream();
    }

    @Test
    public void testGenerateReport() {
        // Arrange: создаем объекты Filter и Statistic для теста
        Filter filter = new Filter(
            List.of("access.log"),
            LocalDateTime.of(2024, 8, 31, 0, 0),
            LocalDateTime.now(),
            ReportFormat.MARKDOWN,
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
                "index.html", 5000,
                "about.html", 2000,
                "contact.html", 1000
            ))
            .avg(500.0)
            .percent95(950.0)
            .percent90(900.0)
            .percent99(990.0)
            .build();

        PrintStream out = new PrintStream(outputStream);

        reportGenerator.generateReport(filter, statistic, out);
        String reportContent = outputStream.toString();
        System.out.println(reportContent);


        assertTrue(reportContent.contains("#### Общая информация"), "Отчет должен содержать заголовок 'Общая информация'");
        assertTrue(reportContent.contains("#### Запрашиваемые ресурсы"), "Отчет должен содержать заголовок 'Запрашиваемые ресурсы'");
        assertTrue(reportContent.contains("#### Коды ответа"), "Отчет должен содержать заголовок 'Коды ответа'");
        assertTrue(reportContent.contains("#### Дополнительная информация"), "Отчет должен содержать заголовок 'Дополнительная информация'");

        assertTrue(reportContent.contains("|  Количество запросов | 10 000 |"), "Отчет должен содержать количество запросов");
        assertTrue(reportContent.contains("| Средний размер ответа| 500.0b |"), "Отчет должен содержать средний размер ответа");
        assertTrue(reportContent.contains("|   95p размера ответа | 950.0b |"), "Отчет должен содержать 95-й перцентиль размера ответа");

        assertTrue(reportContent.contains("|  `/index.html`  | 5 000 |"), "Отчет должен содержать данные для ресурса /index.html");
        assertTrue(reportContent.contains("|  `/about.html`  | 2 000 |"), "Отчет должен содержать данные для ресурса /about.html");
        assertTrue(reportContent.contains("|  `/contact.html`  | 1 000 |"), "Отчет должен содержать данные для ресурса /contact.html");

        assertTrue(reportContent.contains("| 200 | OK                     | 8 000 |"), "Отчет должен содержать код ответа 200");
        assertTrue(reportContent.contains("| 404 | NOT_FOUND              | 1 000 |"), "Отчет должен содержать код ответа 404");
        assertTrue(reportContent.contains("| 500 | INTERNAL_SERVER_ERROR  | 500 |"), "Отчет должен содержать код ответа 500");

        assertTrue(reportContent.contains("| Количество уникальных ресурсов | 3 |"), "Отчет должен содержать количество уникальных ресурсов");
        assertTrue(reportContent.contains("__*Размеры ответов представлены в байтах*__"), "Отчет должен указывать, что размеры ответов представлены в байтах");
    }
}
