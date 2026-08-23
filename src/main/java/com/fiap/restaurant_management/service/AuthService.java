package com.fiap.restaurant_management.service;

import com.fiap.restaurant_management.dto.LoginDTO;
import com.fiap.restaurant_management.dto.TokenResponseDTO;

public interface AuthService {

    TokenResponseDTO login(LoginDTO dto);
}