package backend.academy.parser.logic.service;

import backend.academy.parser.model.Statistic;

/**
 * Интерфейс работы с файлами
 * Конкретная реализация определяет с какими файлами она работает: LocalFiles, URLFiles
 */
public abstract class FileHandler {
    protected final StatisticsCounter counter = StatisticsCounter.getInstance();
    protected final LogParser parser = LogParser.getInstance();

    public abstract Statistic handleFiles();

}
