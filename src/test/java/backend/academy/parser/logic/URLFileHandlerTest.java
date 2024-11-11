package backend.academy.parser.logic;

//import backend.academy.parser.model.Filter;
//import backend.academy.parser.model.Log;
//import com.sun.net.httpserver.HttpServer;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.net.InetSocketAddress;
//import java.util.List;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;

public class URLFileHandlerTest {
//
//    private static final int PORT = 8080;
//    private static HttpServer server;
//    private final URLFileHandler urlFileHandler = new URLFileHandler();
//
//    @BeforeAll
//    public static void setUp() throws IOException {
//        server = HttpServer.create(new InetSocketAddress(PORT), 0);
//        server.createContext("/logfile.log", exchange -> {
//            String response = """
//                216.46.173.126 - - [04/Jun/2015:04:06:11 +0000] "GET /downloads/product_1 HTTP/1.1" 304 0 "-" "Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.17)"
//                86.51.177.4 - - [04/Jun/2015:05:06:10 +0000] "GET /downloads/product_1 HTTP/1.1" 404 336 "-" "Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)"
//                """;
//            exchange.sendResponseHeaders(200, response.length());
//            try (OutputStream os = exchange.getResponseBody()) {
//                os.write(response.getBytes());
//            }
//        });
//        server.createContext("/forbiddenfile.log", exchange -> exchange.sendResponseHeaders(403, -1));
//        server.createContext("/notfound.log", exchange -> exchange.sendResponseHeaders(404, -1));
//        server.start();
//    }
//
//    @AfterAll
//    public static void tearDown() {
//        server.stop(0);
//    }
//
//    @Test
//    public void testHandleFiles_withValidUrl_returnsParsedLogs() {
//        var filter = Filter.builder()
//            .paths(List.of("http://localhost:" + PORT + "/logfile.log"))
//            .build();
//
//        List<Log> logs = urlFileHandler.handleFiles(filter);
//
//        assertEquals(2, logs.size());
//    }
//
//    @Test
//    public void testHandleFiles_withNon200Response_returnsEmptyList() {
//        var filter = Filter.builder()
//            .paths(List.of("http://localhost:" + PORT + "/forbiddenfile.log"))
//            .build();
//
//        List<Log> logs = urlFileHandler.handleFiles(filter);
//
//        assertTrue(logs.isEmpty(), "Expected empty list for non-200 response");
//    }
//
//    @Test
//    public void testHandleFiles_withInvalidUrl_returnsEmptyList() {
//        var filter = Filter.builder()
//            .paths(List.of("http://localhost:" + PORT + "/notfound.log"))
//            .build();
//
//        List<Log> logs = urlFileHandler.handleFiles(filter);
//
//        assertTrue(logs.isEmpty(), "Expected empty list for non-existent URL");
//    }
}
