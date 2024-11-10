package backend.academy.parser.logic;

import backend.academy.parser.logic.interfaces.FileHandler;
import backend.academy.parser.model.Filter;
import backend.academy.parser.model.Log;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

@SuppressFBWarnings({"DM_DEFAULT_ENCODING"})
/**
 * Класс ответственный за работу с файлами находящимся на сервере
 */
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

    /**
     * Метод читает строки файла
     * @param url к файлу на сервере
     * @return {@code Stream<String>} от прочитанных строк файла,
     *     при этом не загружая весь файл в память
     */
    private Stream<String> readFileLines(String url) {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build();
        try {
            log.info("Послан запрос: {} {}", request.method(), request.uri());
            var response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.body()));
            return  reader.lines();
        } catch (IOException | InterruptedException e) {
            log.error("Произошла ошибка чтения файла: {}", e.getMessage());
            return Stream.empty();
        }
    }

}
