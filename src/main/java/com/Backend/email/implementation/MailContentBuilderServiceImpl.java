package com.Backend.email.implementation;

import com.Backend.email.service.MailContentBuilderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

public class MailContentBuilderServiceImpl implements MailContentBuilderService {
    @Autowired
    private  TemplateEngine templateEngine;
    /**
     * Builds an email message using a template and the provided message.
     *
     * @param message The message to be included in the email.
     * @return The built email message.
     */
    @Override
    public String build(String message) {
        return templateEngine.process("mailTemplate", contextSetUp(message));
    }

    /**
     * Sets up the Thymeleaf context for building an email message.
     *
     * @param message The message to be included in the email.
     * @return The Thymeleaf context.
     */
    private Context contextSetUp(String message){
        Context context = new Context();
        context.setVariable("message",message);
        return context;
    }
}
