package com.cordierlaurent.safetynet.interceptor;

import com.fasterxml.jackson.annotation.JsonView; // Pour @JsonView
import jakarta.servlet.http.HttpServletRequest; // Pour HttpServletRequest
import jakarta.servlet.http.HttpServletResponse; // Pour HttpServletResponse
import org.springframework.stereotype.Component; // Pour @Component
import org.springframework.web.method.HandlerMethod; // Pour HandlerMethod
import org.springframework.web.servlet.ModelAndView; // Pour ModelAndView
import org.springframework.web.servlet.HandlerInterceptor;
import org.apache.logging.log4j.LogManager; // Pour le logger
import org.apache.logging.log4j.Logger; // Pour le logger

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
