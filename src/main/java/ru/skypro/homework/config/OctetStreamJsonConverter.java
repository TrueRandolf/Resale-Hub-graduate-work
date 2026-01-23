package ru.skypro.homework.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

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
    public boolean canWrite(Class<?> clazz, MediaType mediaType) { return false; }
}
