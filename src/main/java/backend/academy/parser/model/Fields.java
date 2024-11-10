package backend.academy.parser.model;

import lombok.Getter;

/**
 * Enum дозволенных параметров для ключа --filter-field
 * сделано для того чтобы не пользоваться в лишний раз Reflection API
 * вместо этого просто через константу извлечь название переменной
 * в классе Log по которому можно проводить фильтрацию
 */
@Getter
public enum Fields {
    REMOTE_ADDR("ip"),
    REMOTE_USER("user"),
    TIME_LOCAL("time"),
    REQUEST("request"),
    STATUS("status"),
    BODY_BYTES_SENT("bodyByteSent"),
    HTTP_REFERER("referer"),
    HTTP_USER_AGENT("userAgent");

    private final String field;

    Fields(String field) {
        this.field = field;
    }

    public static Fields fromName(String name) {
        for (Fields field : Fields.values()) {
            if (field.name().equals(name)) {
                return field;
            }
        }
        return null; // Возвращаем null, если константа не найдена
    }
}
