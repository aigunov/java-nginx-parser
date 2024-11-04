package backend.academy.parser.model;

import java.time.LocalDateTime;

public class Log {
    private String ip;
    private String user;
    private LocalDateTime time;
    private String request;
    private int status;
    private int bodyByteSent;
    private String referer;
    private String userAgent;

}
