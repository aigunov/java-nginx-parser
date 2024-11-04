package backend.academy.parser.logic;

import backend.academy.parser.logic.interfaces.FileHandler;
import backend.academy.parser.model.Filter;
import backend.academy.parser.model.Log;
import java.util.List;

public class URLFileHandler implements FileHandler {
    @Override
    public List<Log> handleFile(Filter filter) {
        return List.of();
    }
}
