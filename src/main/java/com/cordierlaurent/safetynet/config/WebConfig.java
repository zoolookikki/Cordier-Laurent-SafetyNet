package com.cordierlaurent.safetynet.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.cordierlaurent.safetynet.interceptor.JsonViewInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JsonViewInterceptor jsonViewInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jsonViewInterceptor);
    }
}
