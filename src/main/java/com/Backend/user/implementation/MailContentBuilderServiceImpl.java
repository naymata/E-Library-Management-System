package com.Backend.user.implementation;

import com.Backend.user.service.MailContentBuilderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

public class MailContentBuilderServiceImpl implements MailContentBuilderService {
    @Autowired
    private  TemplateEngine templateEngine;
    @Override
    public String build(String message) {
        return templateEngine.process("mailTemplate", contextSetUp(message));
    }

    private Context contextSetUp(String message){
        Context context = new Context();
        context.setVariable("message",message);
        return context;
    }
}
