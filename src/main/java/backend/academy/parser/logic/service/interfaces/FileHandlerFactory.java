package backend.academy.parser.logic.service.interfaces;

import backend.academy.parser.model.Filter;

public interface FileHandlerFactory {
    default FileHandler getFileHandler(Filter filter) {
        return createFileHandler(filter);
    }

    FileHandler createFileHandler(Filter filter);
}
