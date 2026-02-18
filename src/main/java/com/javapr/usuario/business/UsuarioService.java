package com.javapr.usuario.business;

import com.javapr.usuario.business.converter.UsuarioConverter;
import com.javapr.usuario.business.dto.UsuarioDTO;
import com.javapr.usuario.infrastructure.entity.Usuario;
import com.javapr.usuario.infrastructure.exeptcions.ConflictException;
import com.javapr.usuario.infrastructure.exeptcions.ResourceNotFoundException;
import com.javapr.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO){
        emailExist(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public void emailExist(String email) {
        if (verificaEmailExiste(email)) {
            throw new ConflictException("Email already in use: " + email);
        }
    }



    public boolean verificaEmailExiste(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public Usuario buscarUsuarioPorEmail(String email){
        return usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Email inexistente " + email));
    }
    public void deletaUsuarioPorEmail(String email){
        usuarioRepository.deleteByEmail(email);
    }
}
