package backend.academy.parser.model;

import java.time.LocalDateTime;
import lombok.Builder;

@SuppressWarnings({"RecordComponentNumber"})
/**
 * Record Log в который парсится строка из файла с логами
 * @param ip - адрес
 * @param user - удаленный пользователь
 * @param time - дата запроса
 * @param request - запрос
 * @param resource - ресурс к которому происходит обращение
 * @param status - статус код ответа http
 * @param bodyByteSent - кол-во отправленных байтов в теле запроса
 * @param referer - хз что это если честно, но в шаблоне nginx логов указан http_referer
 * @param userAgent - агент отправитель запроса с удаленного пользователя
 */
@Builder(toBuilder = true)
public record Log(
    String ip,
    String user,
    LocalDateTime time,
    String request,
    String resource,
    int status,
    int bodyByteSent,
    String referer,
    String userAgent
) {
}
