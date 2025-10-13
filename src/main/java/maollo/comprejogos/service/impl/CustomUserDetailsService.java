package maollo.comprejogos.service.impl;

import maollo.comprejogos.repository.UserCompreJogosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList; // Usado para as roles (perfis de usuário)

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserCompreJogosRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        maollo.comprejogos.domain.UserCompreJogos appUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email: " + email));

        // Por enquanto, não estamos usando roles (perfis), então a lista de authorities fica vazia.
        // Se você tivesse roles como 'ROLE_ADMIN', 'ROLE_USER', você as adicionaria aqui.
        return new User(appUser.getEmail(), appUser.getPassword(), new ArrayList<>());
    }
}