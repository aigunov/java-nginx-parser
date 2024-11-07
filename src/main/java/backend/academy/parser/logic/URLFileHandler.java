package backend.academy.parser.logic;

import backend.academy.parser.logic.interfaces.FileHandler;
import backend.academy.parser.model.Filter;
import backend.academy.parser.model.Log;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class URLFileHandler implements FileHandler {
    private final LogParser logParser = new LogParser();
    private final HttpClient client = HttpClient.newHttpClient();

    @Override
    public List<Log> handleFiles(Filter filter) {
        return filter.paths()
            .stream()
            .flatMap(this::readFileLines)
            .map(logParser::parseLine)
            .toList();
    }

    public Stream<String> readFileLines(String url) {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build();
        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (!(response.statusCode() == 200)) {
                throw new IOException("Файл по URL: " + url + " недоступен.");
            }
            return response.body().lines();
        } catch (IOException | InterruptedException e) {
            log.error("Произошла ошибка чтения файла: {}", e.getMessage());
            return Stream.empty();
        }
    }

    public static void main(String[] args) {
        URLFileHandler filer = new URLFileHandler();
        var response = filer.readFileLines("https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs");
        response.limit(5).forEach(System.out::println);
    }
}
