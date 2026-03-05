package com.javapr.usuario.business;

import com.javapr.usuario.infrastructure.clients.ViaCepClient;
import com.javapr.usuario.infrastructure.clients.ViaCepDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ViaCepService {

    private final ViaCepClient client;

    public ViaCepDTO buscaDadosEndereco(String cep) {
        return client.buscaDadosEndereco(cep);
    }

    private String processarCep(String cep) {
        String cepFormatado = cep.replace("-", "").
                replace(".", "").replace(" ", "");


        if (cepFormatado.matches("\\d+")
                || !Objects.equals(cepFormatado.length(),  8)) {
            throw new IllegalArgumentException("O cep contém caracteres inválidos, favor verificar");


        }
        return cepFormatado;
    }

}
