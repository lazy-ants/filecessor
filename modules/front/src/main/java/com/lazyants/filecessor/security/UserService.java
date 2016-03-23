package com.lazyants.filecessor.security;

import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.UUID;

import static java.util.Collections.singletonList;

@Component
public class UserService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();
    private final HashMap<String, User> userMap;

    public UserService() {
        userMap = new HashMap<>();
        userMap.put("user1", new User("user1", UUID.randomUUID().toString(), singletonList(new SimpleGrantedAuthority("ROLE_USER"))));
        userMap.put("user2", new User("user2", UUID.randomUUID().toString(), singletonList(new SimpleGrantedAuthority("ROLE_USER"))));
        userMap.put("user3", new User("user3", UUID.randomUUID().toString(), singletonList(new SimpleGrantedAuthority("ROLE_USER"))));
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
}
