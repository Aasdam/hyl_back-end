package com.hyl.gatewayserver.manager;

import com.hyl.gatewayserver.model.Role;
import com.hyl.gatewayserver.service.UserService;
import com.hyl.gatewayserver.utils.JWTUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final JWTUtil jwtUtil;
    private final UserService userService;

    @Autowired
    public AuthenticationManager(JWTUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getPrincipal().toString();

        String username;
        try {
            username = jwtUtil.getUsernameFromToken(authToken);
        } catch (Exception e) {
            username = null;
        }

        if (username != null && jwtUtil.validateToken(authToken)) {
            // Extraction des roles
            Claims claims = jwtUtil.getAllClaimsFromToken(authToken);
            List<String> rolesMap = claims.get("role", List.class);
            List<Role> roles = new ArrayList<>();
            for (String rolemap : rolesMap) {
                roles.add(Role.valueOf(rolemap));
            }

            // Si role non admin
            if (!roles.contains(Role.ROLE_ADMIN)) {
                final String usernameInLambda = username;
                return userService.doGetIdByEmail(username).flatMap(idUser -> {
                    // Vérification si id renseigné dans le header = id correspondant à l'adresse email extrait du token jwt (sécurité renforcé)
                    String idUserInHeader = (authentication.getCredentials() != null ? authentication.getCredentials().toString() : null);
                    if (idUser.equals(idUserInHeader)) {
                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(usernameInLambda, null, roles.stream().map(
                                        authority -> new SimpleGrantedAuthority(authority.name())).collect(Collectors.toList())
                                );
                        return Mono.just(auth);
                    } else {
                        return Mono.empty();
                    }
                });
                // Si role admin
            } else {
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(username, null, roles.stream().map(
                                authority -> new SimpleGrantedAuthority(authority.name())).collect(Collectors.toList())
                        );
                return Mono.just(auth);
            }
        } else {
            return Mono.empty();
        }
    }
}
