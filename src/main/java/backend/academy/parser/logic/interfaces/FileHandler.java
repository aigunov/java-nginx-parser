package backend.academy.parser.logic.interfaces;

import backend.academy.parser.model.Filter;
import backend.academy.parser.model.Log;
import java.util.List;

/**
 * Интерфейс работы с файлами
 * Конкретная реализация определяет с какими файлами она работает: LocalFiles, URLFiles
 */
public interface FileHandler {

    /**
     * Этот метод вызываемый другими классами для извлечения содержимого файлов в виде List<Log>
     * Сначала извлекаются абсолютные пути к переданному glob выражению
     * вызывается метод parseLine класса LogParser
     *
     * @param filter - объект класса Filter содержащий в себе переданный через cli параметры
     * @return - List<Log> содержащий в себе все извлеченные логи
     */

    List<Log> handleFiles(Filter filter) ;
}
