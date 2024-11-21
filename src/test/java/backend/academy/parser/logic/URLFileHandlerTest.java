package backend.academy.parser.logic;

import backend.academy.parser.logic.service.implementations.URLFileHandler;
import backend.academy.parser.model.Filter;
import com.sun.net.httpserver.HttpServer;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class URLFileHandlerTest {

    private URLFileHandler urlFileHandler;
    private Filter filter;

    @Mock
    private HttpURLConnection mockConnection;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        filter = new Filter();
        filter.paths(List.of("http://localhost:8080/testfile.log"));
        urlFileHandler = new URLFileHandler(filter);
    }

    @Test
    void testReadFileLines_SuccessfulResponse() throws Exception {
        String mockResponse = "INFO Test log entry 1\nERROR Test log entry 2\n";
        when(mockConnection.getResponseCode()).thenReturn(200);
        when(mockConnection.getInputStream()).thenReturn(
            new ByteArrayInputStream(mockResponse.getBytes())
        );

        URL realUrl = new URL("https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs");
        URL spyUrl = spy(realUrl);
        when(spyUrl.openConnection()).thenReturn(mockConnection);

        Stream<String> lines = urlFileHandler.readFileLines(spyUrl.toString());

        assertThat(lines).containsSequence("93.180.71.3 - - [17/May/2015:08:05:32 +0000] \"GET /downloads/product_1 HTTP/1.1\" 304 0 \"-\" \"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)\"");

    }
}
