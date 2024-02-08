package com.Backend.email.service;

import org.springframework.stereotype.Service;

@Service
public interface MailContentBuilderService {
    String build(String message);
}
