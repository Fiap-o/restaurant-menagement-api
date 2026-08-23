package com.fiap.restaurant_management.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.fiap.restaurant_management.dto.LoginDTO;
import com.fiap.restaurant_management.dto.TokenResponseDTO;
import com.fiap.restaurant_management.exception.CredenciaisInvalidasException;
import com.fiap.restaurant_management.security.JwtService;
import com.fiap.restaurant_management.security.UsuarioDetailsService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UsuarioDetailsService usuarioDetailsService;

    @Mock
    private JwtService jwtService;

    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(authenticationManager, usuarioDetailsService, jwtService);
    }

    @Test
    void login_deveRetornarToken_quandoCredenciaisValidas() {
        LoginDTO dto = new LoginDTO("joaosilva", "senha123");
        UserDetails userDetails = User.builder()
                .username("joaosilva")
                .password("hash")
                .authorities("ROLE_CLIENTE")
                .build();

        given(usuarioDetailsService.loadUserByUsername("joaosilva")).willReturn(userDetails);
        given(jwtService.generateToken(userDetails)).willReturn("token-gerado");

        TokenResponseDTO resultado = authService.login(dto);

        assertThat(resultado.getToken()).isEqualTo("token-gerado");
        assertThat(resultado.getTipo()).isEqualTo("Bearer");
    }

    @Test
    void login_deveLancarCredenciaisInvalidas_quandoAutenticacaoFalha() {
        LoginDTO dto = new LoginDTO("joaosilva", "senhaErrada");

        willThrow(new BadCredentialsException("Bad credentials"))
                .given(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertThatThrownBy(() -> authService.login(dto))
                .isInstanceOf(CredenciaisInvalidasException.class);
    }
}
