package com.lazyants.filecessor.controller;

import com.lazyants.filecessor.security.TokenAuthenticationService;
import com.lazyants.filecessor.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth", produces = {"application/json"})
public class UserController {
    private final UserService userService;

    private final TokenAuthenticationService authenticationService;

    @Autowired
    public UserController(UserService service, TokenAuthenticationService authenticationService) {
        this.userService = service;
        this.authenticationService = authenticationService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public LoginResponse login(@RequestBody final UserLogin login) throws AuthenticationException {
        if (login.name == null || login.password == null) {
            throw new BadCredentialsException("Invalid credentials");
        }
        User user = userService.loadUserByUsername(login.name);
        if (user == null || !user.getPassword().equals(login.password)) {
            throw new BadCredentialsException("Invalid credentials");
        }
        return new LoginResponse(authenticationService.createTokenForUser(userService.loadUserByUsername(login.name)));
    }

    private static class UserLogin {
        public String name;
        public String password;
    }

    private static class LoginResponse {
        public String token;

        public LoginResponse(final String token) {
            this.token = token;
        }
    }
}
