package com.sevenstore.security;

import com.sevenstore.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ApiKeyChecker {

    private final UserRepository userRepository;

    public ApiKeyChecker(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isApiKeyCorrect(String apiKey) {
        if (!userRepository.existsByApiKey(apiKey)) {
            throw new IllegalArgumentException("Ошибка авторизации");
        }

        return true;
    }
}