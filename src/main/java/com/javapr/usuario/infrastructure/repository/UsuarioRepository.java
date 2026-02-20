package com.javapr.usuario.infrastructure.repository;

import com.javapr.usuario.infrastructure.entity.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByEmail(String email);

    //Evita retorno de info nula
    Optional<Usuario> findByEmail(String email);

    @Transactional
    void deleteByEmail(String email);
}
