package backend.academy.parser.logic.service;

import backend.academy.parser.model.Log;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@SuppressWarnings({"MagicNumber"})
/**
 * Класс Парсер
 */
public class LogParser {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);

    /**
     * Метод который парсит строку лога
     *
     * @param line - строка из файла логов которую парсят
     * @return сконструированный объект класса Log
     */
    public Log parseLine(final String line) {
        String[] parts = line.split("\"");

        String[] firstPart = parts[0].split(" ");
        String[] statusAndBytes = parts[2].trim().split(" ");

        String remoteAddr = firstPart[0];
        String remoteUser = firstPart[2];
        String timeLocalStr = firstPart[3] + " " + firstPart[4];
        String request = parts[1];
        String resource = extractResourceName(request);
        int status = Integer.parseInt(statusAndBytes[0]);
        int bodyBytesSent = Integer.parseInt(statusAndBytes[1]);
        String httpReferer = parts[3];
        String httpUserAgent = parts[5];
        LocalDateTime timeLocal = LocalDateTime.parse(timeLocalStr.substring(1, timeLocalStr.length() - 1), formatter);

        return Log.builder()
            .ip(remoteAddr)
            .user(remoteUser)
            .time(timeLocal)
            .request(request)
            .resource(resource)
            .status(status)
            .bodyByteSent(bodyBytesSent)
            .referer(httpReferer)
            .userAgent(httpUserAgent)
            .build();
    }

    public static String extractResourceName(String requestLine) {
        if (requestLine == null || requestLine.isEmpty()) {
            return null;
        }
        String[] parts = requestLine.split(" ")[1].split("/");

        return parts[parts.length - 1];
    }
}
