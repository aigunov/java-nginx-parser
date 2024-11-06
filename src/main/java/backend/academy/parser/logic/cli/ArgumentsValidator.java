package backend.academy.parser.logic.cli;

import backend.academy.parser.model.Log;
import com.beust.jcommander.IParametersValidator;
import com.beust.jcommander.ParameterException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

public class ArgumentsValidator implements IParametersValidator {
    @Override
    public void validate(Map<String, Object> map) throws ParameterException {
        var to = (LocalDateTime) map.getOrDefault("to", LocalDateTime.MIN);
        var from = (LocalDateTime) map.getOrDefault("from", LocalDateTime.MAX);
        if (to.isAfter(from)) {
            throw new ParameterException("Неверно заданы фильтры для начала и конца даты");
        }
        var filterField = (String) map.getOrDefault("--filter-field", "none");
        var fields = Arrays.stream(Log.class.getDeclaredFields()).map(Field::getName).toList();
        if (!filterField.equals("none") && !fields.contains(filterField)) {
            throw new ParameterException("Неверно заданы параметры для фильтрации");
        }

        var filterValue = (String) map.getOrDefault("--filter-value", "none");
        // если задано поле для сортировки, но не задано значения для сортировки по полю и наоборот
        if ((!filterField.equals("none") && !filterValue.equals("none")) ||
            (filterField.equals("none") && !filterValue.equals("none"))) {
            throw new ParameterException("Неверно заданы параметры для фильтрации");
        }
    }
}