package com.cordierlaurent.safetynet.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.cordierlaurent.safetynet.interceptor.JsonViewInterceptor;
/**
 * Allows you to configure and register the JsonView Interceptor that it is active on all requests processed by Spring MVC.
 * The JsonviewInterceptor is only used to trace which view is applied.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JsonViewInterceptor jsonViewInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jsonViewInterceptor);
    }
}
