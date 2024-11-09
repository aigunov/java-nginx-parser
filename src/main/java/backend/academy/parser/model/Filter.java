package backend.academy.parser.model;

import backend.academy.parser.logic.cli.ArgumentsValidator;
import backend.academy.parser.logic.cli.FormatConverter;
import backend.academy.parser.logic.cli.LocalDateTimeConverter;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Parameters(separators = " ", parametersValidators = ArgumentsValidator.class)
public class Filter {
    @Parameter(names = {"-p", "--path"},
        description = "Путь к файлу или URL",
        required = true, variableArity = true)
    private List<String> paths;

    @Parameter(names = {"-e", "--from"},
        description = "Дата конца",
        required = false,
        converter = LocalDateTimeConverter.class)
    private LocalDateTime from = LocalDateTime.MIN;

    @Parameter(names = {"-s", "--to"},
        description = "Дата начала",
        required = false,
        converter = LocalDateTimeConverter.class)
    private LocalDateTime to = LocalDateTime.MAX;

    @Parameter(names = {"-f", "--format"},
        description = "Формат вывода отчета(MARKDOWN or ADOC)",
        required = false,
        converter = FormatConverter.class)
    private ReportFormat format = ReportFormat.MARKDOWN;

    @Parameter(names = {"-fv", "--filter-value"},
        description = "Сортировка по значению по переданном полю",
        required = false)
    private String filterValue;

    @Parameter(names = {"-ff", "--filter-field"},
        description = "По какому полю сортировка",
        required = false)
    private String filterField;

    private Path domenPath;
}

