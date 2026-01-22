package ru.skypro.homework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${app.upload.main-dir}")
    private String mainDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        String rootPath = Paths.get(mainDir).toAbsolutePath().toUri().toString();

        registry.addResourceHandler ("/images/**")
                .addResourceLocations(rootPath);
    }
}
