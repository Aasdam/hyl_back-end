package com.hyl.gatewayserver.service;

import com.hyl.gatewayserver.encoder.PBKDF2Encoder;
import com.hyl.gatewayserver.model.Role;
import com.hyl.gatewayserver.model.SignInRequest;
import com.hyl.gatewayserver.model.SignUpRequest;
import com.hyl.gatewayserver.model.User;
import com.hyl.gatewayserver.proxies.UserApiProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Service
public class UserService {

    @Value("${hyl.admin.username}")
    private String adminUsername;

    @Value("${hyl.admin.passwordEncrypted}")
    private String adminPassword;

    private User admin;

    @PostConstruct
    public void init() {
        this.admin = new User("0", adminUsername, adminPassword, true, Collections.singletonList(Role.ROLE_ADMIN));
    }

    private final UserApiProxy userApiProxy;

    private final PBKDF2Encoder passwordEncoder;

    @Autowired
    public UserService(UserApiProxy userApiProxy, PBKDF2Encoder passwordEncoder) {
        this.userApiProxy = userApiProxy;
        this.passwordEncoder = passwordEncoder;
    }

    public User getAdmin() {
        return admin;
    }

    public Mono<User> doUserConnection(SignInRequest signInRequest) {
        if (signInRequest.getEmail().equals(adminUsername)
            && passwordEncoder.matches(signInRequest.getPassword(), adminPassword)) {
            return Mono.just(admin);
        } else {
            return userApiProxy.signin(admin, signInRequest).flatMap(idUser -> Mono.just(
                    new User(idUser, signInRequest.getEmail(), passwordEncoder.encode(signInRequest.getPassword()),
                    true, Collections.singletonList(Role.ROLE_USER))));
        }
    }


    public Mono<Void> doUserInscription(SignUpRequest signUpRequest) {
        return userApiProxy.signup(admin, signUpRequest);
    }


    public Mono<String> doGetIdByEmail(String email) {
        return userApiProxy.getIdByEmail(admin, email);
    }

    public Mono<Void> doForgotPassword(String email) {
        return userApiProxy.forgotPassword (admin, email);
    }
}
