package com.fiap.restaurant_management.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.fiap.restaurant_management.dto.LoginDTO;
import com.fiap.restaurant_management.security.JwtService;
import com.fiap.restaurant_management.service.AuthService;
import com.fiap.restaurant_management.dto.TokenResponseDTO;
import com.fiap.restaurant_management.security.UsuarioDetailsService;
import com.fiap.restaurant_management.exception.CredenciaisInvalidasException;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioDetailsService usuarioDetailsService;
    private final JwtService jwtService;

    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            UsuarioDetailsService usuarioDetailsService,
            JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.usuarioDetailsService = usuarioDetailsService;
        this.jwtService = jwtService;
    }

    @Override
    public TokenResponseDTO login(LoginDTO dto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getSenha()));
        } catch (BadCredentialsException ex) {
            throw new CredenciaisInvalidasException();
        }

        UserDetails userDetails = usuarioDetailsService.loadUserByUsername(dto.getLogin());
        String token = jwtService.generateToken(userDetails);

        return new TokenResponseDTO(token, "Bearer");
    }
}