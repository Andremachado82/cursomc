package com.andremachado.cursomc.services;

import com.andremachado.cursomc.domain.Cliente;
import com.andremachado.cursomc.repositories.ClienteRepository;
import com.andremachado.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AuthService {

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    EmailService emailService;

    private final Random random = new Random();

    public void sendNewPassword(String email) {
        Cliente cliente = clienteRepository.findByEmail(email);
        if (cliente == null) throw new ObjectNotFoundException("Email não encontrado");
        String novaSenha = novaSenha();
        cliente.setSenha(bCryptPasswordEncoder.encode(novaSenha));
        clienteRepository.save(cliente);

        emailService.sendNewPasswordEmail(cliente, novaSenha);
    }

    private String novaSenha() {
        char[] vet = new char[10];
        for (int i=0; i<10; i++) {
            vet[i] = randomChar();
        }
        return new String(vet);
    }

    // Gera uma senha aleatória de 10 caracteres
    private char randomChar() {
        int opt = random.nextInt(3);
        if (opt == 0) {
            return (char) (random.nextInt(10) + 48);
        } else if (opt == 1) {
            return (char) (random.nextInt(26) + 65);
        } else {
            return (char) (random.nextInt(26) + 97);
        }

    }
}
