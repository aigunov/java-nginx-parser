package backend.academy.parser.logic;

import backend.academy.parser.model.Log;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class LogParser {
    public Log parseLine(final String line) {
        String[] parts = line.split("\"");

        String[] firstPart = parts[0].split(" ");
        String[] statusAndBytes = parts[2].trim().split(" ");

        String remoteAddr = firstPart[0];
        String remoteUser = firstPart[2];
        String timeLocalStr = firstPart[3] + " " + firstPart[4];
        String request = parts[1];
        int status = Integer.parseInt(statusAndBytes[0]);
        int bodyBytesSent = Integer.parseInt(statusAndBytes[1]);
        String httpReferer = parts[3];
        String httpUserAgent = parts[5];

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
        LocalDateTime timeLocal = LocalDateTime.parse(timeLocalStr.substring(1, timeLocalStr.length() - 1), formatter);

        return Log.builder()
            .ip(remoteAddr)
            .user(remoteUser)
            .time(timeLocal)
            .request(request)
            .status(status)
            .bodyByteSent(bodyBytesSent)
            .referer(httpReferer)
            .userAgent(httpUserAgent)
            .build();
    }

}
