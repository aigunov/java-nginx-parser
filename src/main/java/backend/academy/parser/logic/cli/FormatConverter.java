package backend.academy.parser.logic.cli;

import backend.academy.parser.model.ReportFormat;
import com.beust.jcommander.IStringConverter;

public class FormatConverter implements IStringConverter<ReportFormat> {

    /**
     * Конвертирует String -> ReportFormat
     */
    @Override
    public ReportFormat convert(String value) {
        return ReportFormat.valueOf(value.toUpperCase());
    }
}
