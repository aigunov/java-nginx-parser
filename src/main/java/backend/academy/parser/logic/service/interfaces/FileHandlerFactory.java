package backend.academy.parser.logic.service.interfaces;

import backend.academy.parser.model.Filter;

/**
 * Фабрика создания обработчика файлов
 */
public interface FileHandlerFactory {
    default FileHandler getFileHandler(Filter filter) {
        return createFileHandler(filter);
    }

    /**
     * Фабричный метод
     * @param filter - необходимый в работе FileHandler
     */
    FileHandler createFileHandler(Filter filter);
}
