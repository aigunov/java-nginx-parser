package backend.academy.parser.logic.cli;

import backend.academy.parser.model.ReportFormat;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FormatConverterTest {
    private final FormatConverter converter = new FormatConverter();

    @Test
    public void testConvertMarkdown() {
        assertEquals(ReportFormat.MARKDOWN, converter.convert("markdown"));
    }

    @Test
    public void testConvertAdoc() {
        assertEquals(ReportFormat.ADOC, converter.convert("adoc"));
    }

    @Test
    public void testConvertUpperCase() {
        assertEquals(ReportFormat.MARKDOWN, converter.convert("MARKDOWN"));
    }

    @Test
    public void testConvertMixedCase() {
        assertEquals(ReportFormat.MARKDOWN, converter.convert("mArKdoWn"));
    }

    @Test
    public void testInvalidValueThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> converter.convert("invalid_format"));
    }

    @Test
    public void testNullValueThrowsException() {
        assertThrows(NullPointerException.class, () -> converter.convert(null));
    }
}

