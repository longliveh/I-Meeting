package com.example.auth.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class UploadFilePathConfig implements WebMvcConfigurer {
    @Value("${filepath.staticAccessPath}")
    private String staticAccessPath;
    @Value("${filepath.filehome}")
    private String uploadFolder;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(staticAccessPath).addResourceLocations("file:/" + uploadFolder);
    }

}
