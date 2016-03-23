package com.lazyants.filecessor.security;

import com.lazyants.filecessor.configuration.ApplicationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class TokenAuthenticationService {

    private final TokenHandler tokenHandler;

    @Autowired
    public TokenAuthenticationService(ApplicationConfiguration configuration, UserService userService) {
        tokenHandler = new TokenHandler(configuration.getJwtSecret(), userService);
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        final User user = tokenHandler.parseUserFromToken(authHeader.substring(7));
        if (user != null) {
            return new UserAuthentication(user);
        }
        return null;
    }

    public String createTokenForUser(User user) {
        return tokenHandler.createTokenForUser(user);
    }
}
