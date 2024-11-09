package backend.academy.parser.logic;

import backend.academy.parser.logic.interfaces.FileHandler;
import backend.academy.parser.model.Filter;
import backend.academy.parser.model.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class URLFileHandler implements FileHandler {
    private final LogParser logParser = new LogParser();
    private final HttpClient client = HttpClient.newHttpClient();

    @Override
    public Set<Log> handleFiles(Filter filter) {
        return filter.paths()
            .stream()
            .flatMap(this::readFileLines)
            .map(logParser::parseLine)
            .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Log::time))));
    }

    private Stream<String> readFileLines(String url) {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build();
        try {
            log.info("Послан запрос: {} {}", request.method(), request.uri());
            var response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            if (!(response.statusCode() == 200)) {
                throw new IOException("Файл по URL: " + url + " недоступен.");
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.body()));
            return  reader.lines();
        } catch (IOException | InterruptedException e) {
            log.error("Произошла ошибка чтения файла: {}", e.getMessage());
            return Stream.empty();
        }
    }

}
