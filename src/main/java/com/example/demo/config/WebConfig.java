package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configure uploads directory with absolute path
        String uploadPath = "C:/Users/vashi/Downloads/gitrepocupid/CupidReads/src/main/resources/uploads/";
        
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath)
                .setCachePeriod(0);

        // Keep default static resource handling
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
} 