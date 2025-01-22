package com.cordierlaurent.safetynet.interceptor;

import com.fasterxml.jackson.annotation.JsonView; 
import jakarta.servlet.http.HttpServletRequest; 
import jakarta.servlet.http.HttpServletResponse; 
import org.springframework.stereotype.Component; 
import org.springframework.web.method.HandlerMethod; 
import org.springframework.web.servlet.ModelAndView; 
import org.springframework.web.servlet.HandlerInterceptor;
import org.apache.logging.log4j.LogManager; 
import org.apache.logging.log4j.Logger; 

/**
 * Allows you to know which JSON view is used when processing HTTP responses.
 * 
 */
@Component
public class JsonViewInterceptor implements HandlerInterceptor {
    private static final Logger log = LogManager.getLogger(JsonViewInterceptor.class);
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            JsonView jsonView = method.getMethodAnnotation(JsonView.class);
            if (jsonView != null) {
                // Log the view being applied
                log.debug("JsonView applied: " + jsonView.value()[0].getSimpleName());
            } else {
                log.debug("No JsonView applied for this request.");
            }
        }
    }
}
