package backend.academy.parser.logic.service.implementations;

import backend.academy.parser.logic.service.interfaces.FileHandler;
import backend.academy.parser.logic.service.interfaces.FileHandlerFactory;
import backend.academy.parser.model.Filter;

public class PathFileHandlerFactory implements FileHandlerFactory {
    @Override
    public FileHandler createFileHandler(final Filter filter) {
        return new PathFileHandler(filter);
    }
}
