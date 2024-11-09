package backend.academy.parser.logic.interfaces;

import backend.academy.parser.model.Filter;
import backend.academy.parser.model.Log;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface FileHandler {

    Set<Log> handleFiles(Filter filter) ;
}
