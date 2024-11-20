package backend.academy.parser.logic.service.interfaces;

import backend.academy.parser.logic.service.LogParser;
import backend.academy.parser.logic.service.StatisticsCounter;
import backend.academy.parser.model.Filter;
import backend.academy.parser.model.Statistic;

/**
 * Интерфейс работы с файлами
 * Конкретная реализация определяет с какими файлами она работает: LocalFiles, URLFiles
 */
public abstract class FileHandler {
    protected final LogParser parser;
    protected final Filter filter;
    protected final StatisticsCounter counter;

    protected FileHandler(Filter filter) {
        this.filter = filter;
        parser = new LogParser();
        counter = new StatisticsCounter(filter);
    }

    public abstract Statistic handleFiles();
}
