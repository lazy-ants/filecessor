package com.lazyants.filecessor.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lazyants.filecessor.configuration.ApplicationConfiguration;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import static java.util.Collections.singletonList;

@Component
public class UserService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();
    private final HashMap<String, User> userMap;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(ApplicationConfiguration configuration) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        userMap = new HashMap<>();

        if (configuration.getUsersFile() != null) {
            byte[] encoded = Files.readAllBytes(Paths.get(configuration.getUsersFile()));
            UserData[] data = mapper.readValue(new String(encoded), UserData[].class);
            for (UserData userData: data) {
                addUser(new User(userData.getName(), userData.getPassword(), singletonList(new SimpleGrantedAuthority("ROLE_USER"))));
            }
        } else {
            addUser(new User("user1", "user1", singletonList(new SimpleGrantedAuthority("ROLE_USER"))) );
        }
    }

    @Override
    public final User loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userMap.get(username);
        if (user == null) {
            throw new UsernameNotFoundException("user not found");
        }
        detailsChecker.check(user);
        return user;
    }

    private void addUser(User user) {
        if (userMap.putIfAbsent(user.getUsername(), user) == null) {
            logger.info("User " + user.getUsername() + " added with password \"" + user.getPassword() + "\"");
        }
    }

    @Data
    static class UserData {
        private String name;
        private String password;
    }
}
