package com.javapr.usuario.business;

import com.javapr.usuario.business.converter.UsuarioConverter;
import com.javapr.usuario.business.dto.EnderecoDTO;
import com.javapr.usuario.business.dto.TelefoneDTO;
import com.javapr.usuario.business.dto.UsuarioDTO;
import com.javapr.usuario.infrastructure.entity.Endereco;
import com.javapr.usuario.infrastructure.entity.Telefone;
import com.javapr.usuario.infrastructure.entity.Usuario;
import com.javapr.usuario.infrastructure.exeptcions.ConflictException;
import com.javapr.usuario.infrastructure.exeptcions.ResourceNotFoundException;
import com.javapr.usuario.infrastructure.repository.EnderecoRepository;
import com.javapr.usuario.infrastructure.repository.TelefoneRepository;
import com.javapr.usuario.infrastructure.repository.UsuarioRepository;
import com.javapr.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
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

    public UsuarioDTO buscarUsuarioPorEmail(String email) {
        try {
            return usuarioConverter.paraUsuarioDTO(
                    usuarioRepository.findByEmail(email)
                            .orElseThrow(
                                    () -> new ResourceNotFoundException("Email inexistente " + email)
                            )

            );
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Email inexistente " + email);
        }
    }

        public void deletaUsuarioPorEmail (String email){
            usuarioRepository.deleteByEmail(email);
        }



    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO dto) {
        //Busca email por token
        String email = jwtUtil.extractUsername(token.substring(7));

        //Criptografia de Senha
        dto.setSenha(dto.getSenha() != null ? passwordEncoder.encode(dto.getSenha()) : null);
        //Busca dados no banco
        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("Email inexistente " + email));
        //Mesclou os dados com os dados no banco
        Usuario usuario = usuarioConverter.updateUsuario(dto, usuarioEntity);

        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));

    }
    public EnderecoDTO atualizaEndereco(Long idEndereco, EnderecoDTO enderecoDTO) {

        Endereco entity = enderecoRepository.findById(idEndereco).orElseThrow(() ->
        new ResourceNotFoundException("ID inexistente" + idEndereco));

        Endereco endereco = usuarioConverter.updateEndereco(enderecoDTO,entity);

        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));
    }

    public TelefoneDTO atualizaTelefone(Long idTelefone, TelefoneDTO dto) {

        Telefone entity = telefoneRepository.findById(idTelefone).orElseThrow(() ->
        new ResourceNotFoundException("ID inexistente" + idTelefone));

        Telefone telefone = usuarioConverter.updateTelefone(dto,entity);

        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));
    }

    public EnderecoDTO cadastraEndereco(String token, EnderecoDTO dto) {

        String email = jwtUtil.extractUsername(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("Email inexistente " + email));

        Endereco endereco = usuarioConverter.paraEnderecoEntity(dto, usuario.getId());
        Endereco enderecoEntity = enderecoRepository.save(endereco);
        return usuarioConverter.paraEnderecoDTO(enderecoEntity);
    }

    public TelefoneDTO cadastraTelefone(String token, TelefoneDTO dto) {

        String email = jwtUtil.extractUsername(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("Email inexistente " + email));

        Telefone telefone = usuarioConverter.paraTelefoneEntity(dto, usuario.getId());
        Telefone telefoneEntity = telefoneRepository.save(telefone);
        return usuarioConverter.paraTelefoneDTO(telefoneEntity);

    }

}
