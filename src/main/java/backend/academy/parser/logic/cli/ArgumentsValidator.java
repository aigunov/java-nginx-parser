package backend.academy.parser.logic.cli;

import com.beust.jcommander.IParametersValidator;
import com.beust.jcommander.ParameterException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ArgumentsValidator implements IParametersValidator {
    private static final List<String> FIELDS = List.of(
        "remote_addr", "remote_user", "time_local", "request", "status", "body_bytes_sent", "http_referer",
        "http_user_agent"
    );

    /**
     * Метод валидации переданных через cli параметров
     *
     * @throws ParameterException в случае если хоть один из параметров невалиден
     */
    @Override
    public void validate(Map<String, Object> map) throws ParameterException {
        var to = (LocalDateTime) map.getOrDefault("--to", LocalDateTime.MIN);
        var from = (LocalDateTime) map.getOrDefault("--from", LocalDateTime.MAX);
        if (!to.isAfter(from)) {
            throw new ParameterException("Неверно заданы фильтры для начала и конца даты");
        }
        var filterField = Optional.ofNullable(map.get("--filter-field")).orElse("").toString();
        if (!filterField.isEmpty() && !FIELDS.contains(filterField)) {
            throw new ParameterException("Неверно заданы параметры для фильтрации по полю");
        }

        var filterValue = Optional.ofNullable(map.get("--filter-value")).orElse("").toString();
        // если задано поле для сортировки, но не задано значения для сортировки по полю и наоборот
        if ((!filterField.isEmpty() && filterValue.isEmpty()) || (filterField.isEmpty() && !filterValue.isEmpty())) {
            throw new ParameterException("Неверно заданы параметры для фильтрации по значению");
        }
    }
}
