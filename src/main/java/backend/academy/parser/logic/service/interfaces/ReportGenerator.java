package backend.academy.parser.logic.service.interfaces;

import backend.academy.parser.model.Filter;
import backend.academy.parser.model.Statistic;
import java.io.PrintStream;
import java.time.LocalDateTime;

/**
 * Абстрактный класс, для работы с генераторами отчетов
 */
public abstract class ReportGenerator {
    protected String fromDate;
    protected String toDate;
    protected String requestCount;
    protected String uniqueResourcesCount;
    protected Double avgResponseSize;
    protected Double p99ResponseSize;
    protected Double p95ResponseSize;
    protected Double p90ResponseSize;

    /**
     * Метод подгоняет полученные данные к читаемому виду
     * @param filter - значения фильтров с которыми был задан поиск
     * @param statistic - рассчитанные значения статистики
     */
    protected void extractData(Filter filter, Statistic statistic) {
        fromDate = filter.from() != null && filter.from() != LocalDateTime.MIN ? filter.from().toString() : "-";
        toDate = filter.to() != null && filter.to() != LocalDateTime.MAX ? filter.to().toString() : "-";
        requestCount = String.format("%,d", statistic.requestCount());
        avgResponseSize = statistic.avg();
        p99ResponseSize = statistic.percent99();
        p95ResponseSize = statistic.percent95();
        p90ResponseSize = statistic.percent90();
        uniqueResourcesCount = String.valueOf(statistic.resources().size());
    }

    public abstract void generateReport(Filter filter, Statistic statistic, PrintStream out);
}
