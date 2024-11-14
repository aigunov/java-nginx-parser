package backend.academy.parser.logic.reports;

import backend.academy.parser.model.Filter;
import backend.academy.parser.model.Statistic;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.PrintStream;
import java.util.Map;

@SuppressWarnings({"MultipleStringLiterals"})
@SuppressFBWarnings({"UCPM_USE_CHARACTER_PARAMETERIZED_METHOD", "VA_FORMAT_STRING_USES_NEWLINE"})
/**
 * Класс для генерации отчета по собранной статистике в формате MarkDown
 */
public class MDReportGenerator extends ReportGenerator {

    /**
     * Метод формирующий отчет в text blocks
     *
     * @param filter    - значения переданных через консоль данных
     * @param statistic - значения собранной статистики
     * @param out       - PrintStream для вывода report хоть в консоль хоть в файл
     */
    @Override
    public void generateReport(final Filter filter, final Statistic statistic, PrintStream out) {

        var paths = filter.paths().stream()
            .map(path -> path.split("/")[path.split("/").length - 1])
            .toList();
        extractData(filter, statistic);
        var maxCode = statistic.statusCodes().entrySet()
            .stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);

        var minCode = statistic.statusCodes().entrySet()
            .stream()
            .min(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);

        String generalInfo = """
            #### Общая информация

            |       Метрика        |     Значение |
            |:--------------------:|:------------:|
            |       Файл(-ы)       | `%s` |
            |    Начальная дата    | %s |
            |     Конечная дата    | %s |
            |  Количество запросов | %s |
            | Средний размер ответа| %s |
            |   99p размера ответа | %s |
            |   95p размера ответа | %s |
            |   90p размера ответа | %s |

            """.formatted(paths, fromDate, toDate, requestCount, avgResponseSize, p99ResponseSize, p95ResponseSize,
            p90ResponseSize);

        StringBuilder resourcesInfo = new StringBuilder("""
            #### Запрашиваемые ресурсы

            |      Ресурс      | Количество |
            |:----------------:|:----------:|
            """);
        statistic.resources().forEach((resource, count) ->
            resourcesInfo.append("|  `/").append(resource).append("`  | ")
                .append(String.format("%,d", count)).append(" |\n"));
        resourcesInfo.append("\n");

        // Коды ответа
        StringBuilder statusCodesInfo = new StringBuilder("""
            #### Коды ответа

            | Код |          Имя          | Количество |
            |:---:|:---------------------:|:----------:|
            """);
        statistic.statusCodes().forEach((codePhrase, count) ->
            statusCodesInfo.append("| ").append(codePhrase.getCode()).append(" | ")
                .append(String.format("%-22s", codePhrase)).append(" | ")
                .append(String.format("%,d", count)).append(" |\n"));
        statusCodesInfo.append("\n");

        // Дополнительная информация
        String additionalInfo = """
            #### Дополнительная информация

            |       Метрика        |     Значение |
            |:--------------------:|:------------:|
            | Количество уникальных ресурсов | %s |
            | Наиболее частый ответ | %s |
            | Наиболее редкий ответ | %s |
            __*Размеры ответов представлены в байтах*__

            """.formatted(
            uniqueResourcesCount,
            maxCode != null ? maxCode + " " + maxCode.getCode() : "N/A",
            minCode != null ? minCode + " " + minCode.getCode() : "N/A"
        );

        out.println(generalInfo + resourcesInfo + statusCodesInfo + additionalInfo);
    }

}
