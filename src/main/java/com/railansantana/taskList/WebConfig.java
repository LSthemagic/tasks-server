package com.railansantana.taskList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://tasks-front-two.vercel.app")  // substitua com o URL do seu front-end
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}
