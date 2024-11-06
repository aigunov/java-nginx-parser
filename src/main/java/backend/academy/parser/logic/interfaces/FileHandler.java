package backend.academy.parser.logic.interfaces;

import backend.academy.parser.model.Filter;
import backend.academy.parser.model.Log;
import java.io.IOException;
import java.util.List;

public interface FileHandler {

    List<Log> handleFile(Filter filter) throws IOException;
}
