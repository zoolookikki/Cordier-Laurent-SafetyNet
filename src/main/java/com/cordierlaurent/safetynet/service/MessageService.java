package com.cordierlaurent.safetynet.service;

import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

@Service
public class MessageService {
    
    @Autowired
    private MessageSource messageSource;
    
    public String getMessage(String key) {
        return messageSource.getMessage(key,null, Locale.getDefault());
    }

}
