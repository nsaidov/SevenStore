package com.sevenstore.service;

import com.sevenstore.entity.User;
import com.sevenstore.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.SecureRandom;
import java.util.Random;

@Service
public class UserService {

    private final UserRepository userRepository;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random RANDOM = new SecureRandom();

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Transactional
    public Boolean createUser(@RequestBody User user){
        if (user.getName() == null || user.getName().equals("")){
            return false;
        }

        user.setApiKey(generateRandomString(30));

        user.setPasswordHash("123");
        userRepository.save(user);

        return true;
    }


    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
}
