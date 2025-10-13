package maollo.comprejogos.service.impl;


import lombok.RequiredArgsConstructor;
import maollo.comprejogos.domain.UserCompreJogos;
import maollo.comprejogos.repository.UserCompreJogosRepository;
import maollo.comprejogos.service.UserCompreJogosService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCompreJogosServiceImpl implements UserCompreJogosService {

    private final UserCompreJogosRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Adicione esta linha

    @Override
    public Optional<UserCompreJogos> findById(Long id) {
        return userRepository.findById(id);
    }

//    private final PasswordEncoder passwordEncoder;


    @Override
    public UserCompreJogos registerUser(UserCompreJogos user) {
        // Validação básica para evitar duplicatas
        if (userRepository.findByLogin(user.getLogin()).isPresent()) {
            throw new IllegalStateException("Login já está em uso.");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalStateException("Email já está em uso.");
        }
         user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    @Override
    public Optional<UserCompreJogos> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}