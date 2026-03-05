package com.javapr.usuario.business;

import com.javapr.usuario.infrastructure.clients.ViaCepClient;
import com.javapr.usuario.infrastructure.clients.ViaCepDTO;
import com.javapr.usuario.infrastructure.exeptcions.IllegalArgumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ViaCepService {

    private final ViaCepClient client;

    public ViaCepDTO buscaDadosEndereco(String cep) {
        String cepValido = processarCep(cep);
        return client.buscaDadosEndereco(cepValido);
    }

    private String processarCep(String cep) {
        if (cep == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O CEP não pode ser nulo.");
        }

        String cepFormatado = cep.replace("-", "")
                .replace(".", "")
                .replace(" ", "");

        if (!cepFormatado.matches("\\d+") || cepFormatado.length() != 8) {

            throw new IllegalArgumentException("O cep contém caracteres inválidos ou tamanho incorreto, favor verificar");
        }

        return cepFormatado;
    }
}