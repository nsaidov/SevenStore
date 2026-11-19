package com.sevenstore.security;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

@Aspect
@Component
public class ApiKeyAspect {

    private final ApiKeyChecker apiKeyChecker;

    public ApiKeyAspect(ApiKeyChecker apiKeyChecker) {
        this.apiKeyChecker = apiKeyChecker;
    }

    @Before("@annotation(com.sevenstore.security.CheckApiKey)")
    public void checkApiKey() {

        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes()).getRequest();

        String apiKey  = request.getHeader("api-key");

        if (apiKey  == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Ошибка авторизации");
        }


        apiKeyChecker.isApiKeyCorrect(apiKey);
    }
}