package ru.skypro.homework.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

/**
 * Кастомный конвертер для обработки JSON-данных, передаваемых с MIME-типом {@code application/octet-stream}.
 *
 * <p>Служит для корректной десериализации JSON-объектов в Multipart-запросах,
 * в случае прихода части данных в виде потока байтов, а не в {@code application/json}. </p>
 * <p> Характерно для Swagger или Docker-прокси.</p>
 *
 * <p>Работает только на чтение. Запись в формате {@code octet-stream} для данного конвертера отключена.</p>
 *
 * @see org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter
 */

@Component
public class OctetStreamJsonConverter extends AbstractJackson2HttpMessageConverter {
    public OctetStreamJsonConverter(ObjectMapper objectMapper) {
        super(objectMapper, MediaType.APPLICATION_OCTET_STREAM);
    }

    @Override
    protected boolean canRead(MediaType mediaType) {
        return mediaType == null || MediaType.APPLICATION_OCTET_STREAM.isCompatibleWith(mediaType) || super.canRead(mediaType);
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return false;
    }
}
