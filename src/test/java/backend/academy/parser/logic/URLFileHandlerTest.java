package backend.academy.parser.logic;

import backend.academy.parser.logic.service.URLFileHandler;
import backend.academy.parser.model.Filter;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class URLFileHandlerTest {
    private URLFileHandler urlFileHandler;
    private Filter filter;
    private HttpServer server;

    @BeforeEach
    void setUp() throws IOException {
        server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/testfile.log", exchange -> {
            String response = "INFO Test log entry 1\nERROR Test log entry 2\n";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            try (PrintWriter out = new PrintWriter(exchange.getResponseBody())) {
                out.write(response);
            }
        });
        server.start();

        filter = new Filter();
        filter.paths(List.of("http://localhost:8080/testfile.log"));
        urlFileHandler = new URLFileHandler(filter);
    }

    @Test
    void testReadFileLines_SuccessfulResponse() {
        String url = "http://localhost:8080/testfile.log";
        Stream<String> lines = urlFileHandler.readFileLines(url);

        assertThat(lines).containsExactly("INFO Test log entry 1", "ERROR Test log entry 2");
    }

    @Test
    void testReadFileLines_ServerErrorResponse() {
        server.createContext("/errorfile.log", exchange -> {
            exchange.sendResponseHeaders(500, -1);
            exchange.close();
        });
        String url = "http://localhost:8080/errorfile.log";

        Stream<String> lines = urlFileHandler.readFileLines(url);
        assertThat(lines).isEmpty();
    }

    @AfterEach
    void tearDown() {
        server.stop(0);
    }
}
