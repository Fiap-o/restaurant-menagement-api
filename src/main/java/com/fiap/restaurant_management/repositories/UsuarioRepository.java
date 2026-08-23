package com.fiap.restaurant_management.repositories;

import com.fiap.restaurant_management.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByLogin(String login);

    List<Usuario> findByNomeContainingIgnoreCase(String nome);

    boolean existsByEmail(String email);
}