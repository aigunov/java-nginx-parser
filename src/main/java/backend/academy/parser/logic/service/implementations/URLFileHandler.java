package backend.academy.parser.logic.service.implementations;

import backend.academy.parser.logic.service.interfaces.FileHandler;
import backend.academy.parser.model.Filter;
import backend.academy.parser.model.Statistic;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

@SuppressFBWarnings({"DM_DEFAULT_ENCODING", "OS_OPEN_STREAM"})
@SuppressWarnings({"MagicNumber"})
/**
 * Класс ответственный за работу с файлами находящимся на сервере
 */
@Slf4j
public class URLFileHandler extends FileHandler {
    private final HttpClient client = HttpClient.newHttpClient();

    public URLFileHandler(final Filter filter) {
        super(filter);
    }

    @Override
    public Statistic handleFiles() {
        filter.paths().stream()
            .flatMap(this::readFileLines)
            .map(parser::parseLine)
            .filter(counter::filterLog)
            .forEach(counter::calculateDataInLog);
        return counter.getStatistic();
    }

    /**
     * Метод читает строки файла
     * @param url к файлу на сервере
     * @return {@code Stream<String>} от прочитанных строк файла,
     *     при этом не загружая весь файл в память
     */
    public Stream<String> readFileLines(final String url) {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build();
        try {
            log.info("Послан запрос: {} {}", request.method(), request.uri());
            var response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            if (response.statusCode() != 200) {
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

