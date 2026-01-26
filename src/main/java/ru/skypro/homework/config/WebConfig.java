package ru.skypro.homework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

/**
 * Конфигурация доступа к статическим ресурсам приложения.
 *
 * <p>Переопределяет методы {@link WebMvcConfigurer} для сведения
 * внешних HTTP-запросов с файловой системой сервера. Эндпоинт {@code /images/**}
 * связывается с директорией, указанной в свойстве
 * {@code app.upload.main-dir}.</p>
 *
 * <p>Настройка критична для корректного отображения загруженных медиафайлов
 * (изображений) в веб-интерфейсе и Swagger UI! Установлено время
 * кэширования ресурсов для оптимизации нагрузки на файловый сервер.</p>
 *
 * <p>При развертывании в Docker-контейнере необходимо обеспечить соответствие
 * пути в {@code mainDir} смонтированному тому (volume).</p>
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${app.upload.main-dir}")
    private String mainDir;

    @Value("${app.images.cache-period}")
    private Integer browserCash;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String rootPath = Paths.get(mainDir).toAbsolutePath().toUri().toString();

        registry.addResourceHandler("/images/**")
                .addResourceLocations(rootPath)
                .setCachePeriod(browserCash);
    }

}
