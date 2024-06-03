package com.gnservices.portfolio.controller;

import com.gnservices.portfolio.domain.User;
import com.gnservices.portfolio.dto.AuthenticationResponse;
import com.gnservices.portfolio.security.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class AuthenticationController {
    private final AuthenticationService authService;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    /**
     * Registers a new user.
     *
     * @param request User registration details
     * @return ResponseEntity containing the authentication response
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody User request
    ) {
        logger.info("Received registration request for user: {}", request.getUsername());
        AuthenticationResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Authenticates a user during login.
     *
     * @param request User login details
     * @return ResponseEntity containing the authentication response
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody User request
    ) {
        logger.info("Received login request for user: {}", request.getUsername());
        AuthenticationResponse response = authService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Refreshes the authentication token.
     *
     * @param request  HTTP request
     * @param response HTTP response
     * @return ResponseEntity with the refreshed token or an error status
     */
    @PostMapping("/refresh_token")
    public ResponseEntity refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try {
            logger.info("Received token refresh request");
            authService.refreshToken(request, response);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error refreshing token: {}", e.getMessage());
            return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).build();
        }
    }
}
