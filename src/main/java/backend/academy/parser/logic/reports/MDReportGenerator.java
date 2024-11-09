package backend.academy.parser.logic.reports;

import backend.academy.parser.logic.interfaces.ReportGenerator;
import backend.academy.parser.model.Filter;
import backend.academy.parser.model.Statistic;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.Map;

public class MDReportGenerator implements ReportGenerator {
    @Override
    public void generateReport(final Filter filter, final Statistic statistic, PrintStream out) {
        StringBuilder report = new StringBuilder();
        var paths = filter.paths().stream().map(path -> path.split("/")[path.split("/").length - 1]).toList();

        // Общая информация
        report.append("#### Общая информация\n\n");
        report.append("|       Метрика        |     Значение |\n");
        report.append("|:--------------------:|:------------:|\n");
        report.append("|       Файл(-ы)       | `").append(paths).append("` |\n");

        report.append("|    Начальная дата    | ")
            .append(filter.from() != null && filter.from() != LocalDateTime.MIN ? filter.from() : "-").append(" |\n");

        report.append("|     Конечная дата    | ")
            .append(filter.to() != null && filter.to() != LocalDateTime.MAX ? filter.to() : "-").append(" |\n");

        report.append("|  Количество запросов | ").append(String.format("%,d", statistic.requestCount()))
            .append(" |\n");

        report.append("| Средний размер ответа| ").append(statistic.avg()).append("b |\n");
        report.append("|   99p размера ответа | ").append(statistic.percent99()).append("b |\n");
        report.append("|   95p размера ответа | ").append(statistic.percent95()).append("b |\n");
        report.append("|   90p размера ответа | ").append(statistic.percent90()).append("b |\n\n");

        // Запрашиваемые ресурсы
        report.append("#### Запрашиваемые ресурсы\n\n");
        report.append("|      Ресурс      | Количество |\n");
        report.append("|:----------------:|:----------:|\n");
        for (var entry : statistic.resources().entrySet()) {
            report.append("|  `/").append(entry.getKey()).append("`  | ")
                .append(String.format("%,d", entry.getValue())).append(" |\n");
        }
        report.append("\n");


        // Коды ответа
        report.append("#### Коды ответа\n\n");
        report.append("| Код |          Имя          | Количество |\n");
        report.append("|:---:|:---------------------:|:----------:|\n");
        for (var entry : statistic.statusCodes().entrySet()) {
            var count = entry.getValue();
            var codePhrase = entry.getKey();
            var codeNum = codePhrase.getCode();
            report.append("| ").append(codeNum).append(" | ")
                .append(String.format("%-22s", codePhrase)).append(" | ")
                .append(String.format("%,d", count)).append(" |\n");
        }

        // Дополнительная информация
        report.append("\n#### Дополнительная информация\n\n");
        report.append("|       Метрика        |     Значение |\n");
        report.append("|:--------------------:|:------------:|\n");
        report.append("| Количество уникальных ресурсов | ").append(statistic.resources().size()).append(" |\n");

        // Определение наиболее частого и редкого кода ответа
        var maxCode = statistic.statusCodes().entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(null);
        var minCode = statistic.statusCodes().entrySet().stream().min(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(null);

        report.append("| Наиболее частый ответ | ").append(maxCode != null ? maxCode + " " + maxCode.getCode() : "N/A").append(" |\n");
        report.append("| Наиболее редкий ответ | ").append(minCode != null ? minCode + " " + minCode.getCode() : "N/A").append(" |\n");
        report.append(" __*Размеры ответов представлены в байтах*__\n");


        out.println(report);
    }
}
