package backend.academy.parser.logic.service.implementations;

import backend.academy.parser.logic.service.interfaces.FileHandler;
import backend.academy.parser.logic.service.interfaces.FileHandlerFactory;
import backend.academy.parser.model.Filter;

public class URLFileHandlerFactory implements FileHandlerFactory {
    @Override
    public FileHandler createFileHandler(Filter filter) {
        return new URLFileHandler(filter);
    }
}
