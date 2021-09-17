package com.hyl.gatewayserver.controller;

import com.hyl.gatewayserver.exception.CustomBadRequestException;
import com.hyl.gatewayserver.exception.CustomInternalServerErrorException;
import com.hyl.gatewayserver.exception.CustomNotFoundException;
import com.hyl.gatewayserver.exception.CustomUnauthorizedException;
import com.hyl.gatewayserver.model.AuthResponse;
import com.hyl.gatewayserver.model.SignInRequest;
import com.hyl.gatewayserver.model.SignUpRequest;
import com.hyl.gatewayserver.service.UserService;
import com.hyl.gatewayserver.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@RestController
public class GatewayController {

    private final JWTUtil jwtUtil;
    private final UserService userService;

    @Autowired
    public GatewayController(JWTUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }


    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    @PreAuthorize("!(hasRole('USER') or hasRole('ADMIN'))")
    public Mono<ResponseEntity<?>> login(@RequestBody SignInRequest signInRequest) {

        if (signInRequest.getEmail() == null || signInRequest.getPassword() == null
            || signInRequest.getEmail().isBlank() || signInRequest.getPassword().isBlank()) {

            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new CustomBadRequestException("Les champs 'email' et 'password' ne doivent pas être vide")));

        } else {
            return userService.doUserConnection(signInRequest)
                    .map(user -> ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(user), user.getId())));
        }
    }


  @PreAuthorize("!(hasRole('USER') or hasRole('ADMIN'))")
  @RequestMapping(value = "/signup", method = RequestMethod.POST)
  public Mono<ResponseEntity<?>> signup(@RequestBody SignUpRequest signUpRequest) {

      return userService.doUserInscription(signUpRequest).then(
              userService.doUserConnection(new SignInRequest(signUpRequest.getEmail(), signUpRequest.getPassword()))
                      .map(user -> ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(user), user.getId()))));
  }

    @PreAuthorize("!(hasRole('USER') or hasRole('ADMIN'))")
    @RequestMapping(value = "/forgot-password", method = RequestMethod.POST)
    public Mono<ResponseEntity<?>> forgotPassword(@RequestBody String email) {
        return userService.doForgotPassword(email).map(ResponseEntity::ok);
    }

    @ExceptionHandler({WebClientResponseException.class})
    public ResponseEntity<?> handleException(WebClientResponseException exception) {
        int status = exception.getRawStatusCode();

        if (HttpStatus.BAD_REQUEST.value() == status) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CustomBadRequestException(exception.toString()));

        } else if (HttpStatus.UNAUTHORIZED.value() == status) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CustomUnauthorizedException(exception.toString()));

        } else if (HttpStatus.NOT_FOUND.value() == status) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomNotFoundException(exception.toString()));

        } else if (HttpStatus.INTERNAL_SERVER_ERROR.value() == status) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomInternalServerErrorException(exception.toString()));

        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomInternalServerErrorException(exception.toString()));
        }
    }
}
