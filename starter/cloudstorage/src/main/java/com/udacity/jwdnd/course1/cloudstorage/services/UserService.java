package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final HashService hashService;

    public UserService(UserMapper userMapper, HashService hashService)
    {
        this.userMapper = userMapper;
        this.hashService = hashService;
    }

    public int createUser(User user)
    {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);
        return userMapper.insertUser(new User(null,user.getUsername(),encodedSalt,hashedPassword,
                user.getFirstName(), user.getLastName()));
    }

    public User getUser(String userName)
    {
        return userMapper.getUser(userName);
    }

    public boolean isUsernameAvailable(String userName)
    {
        return userMapper.getUser(userName) == null;
    }
}
