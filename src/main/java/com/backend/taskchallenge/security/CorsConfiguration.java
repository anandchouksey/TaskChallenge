package com.backend.taskchallenge.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class CorsConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(),
                        HttpMethod.PUT.name(), HttpMethod.DELETE.name());
    }

}
