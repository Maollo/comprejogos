package maollo.comprejogos.service.impl;


import lombok.RequiredArgsConstructor;
import maollo.comprejogos.domain.Role;
import maollo.comprejogos.domain.UserCompreJogos;
import maollo.comprejogos.dto.UserResponseDTO;
import maollo.comprejogos.dto.UserUpdateProfileDTO;
import maollo.comprejogos.exception.ResourceNotFoundException;
import maollo.comprejogos.mapper.UserMapper;
import maollo.comprejogos.repository.RoleRepository;
import maollo.comprejogos.repository.UserCompreJogosRepository;
import maollo.comprejogos.service.UserCompreJogosService;
import maollo.comprejogos.utils.ERole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserCompreJogosServiceImpl implements UserCompreJogosService {

    private final UserCompreJogosRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Adicione esta linha
    private final RoleRepository roleRepository; // Injetar o novo repositório
    private final UserMapper userMapper; // Injetar o UserMapper


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
        Set<Role> roles = new HashSet<>();
        Role userRole = new Role();
        userRole.setName(ERole.ROLE_USER);
        roles.add(userRole);
        user.setRoles(roles);
         user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    @Override
    public Optional<UserCompreJogos> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserResponseDTO updateUserProfile(String userEmail, UserUpdateProfileDTO profileDto) {
        UserCompreJogos user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o email: " + userEmail));

        // Atualiza apenas os campos que não são nulos no DTO
        if (profileDto.getAvatarUrl() != null) {
            user.setAvatarUrl(profileDto.getAvatarUrl());
        }
        if (profileDto.getBirthDate() != null) {
            user.setBirthDate(profileDto.getBirthDate());
        }
        if (profileDto.getPhone() != null) {
            user.setPhone(profileDto.getPhone());
        }
        if (profileDto.getCountry() != null) {
            user.setCountry(profileDto.getCountry());
        }
        if (profileDto.getCity() != null) {
            user.setCity(profileDto.getCity());
        }

        UserCompreJogos updatedUser = userRepository.save(user);
        return userMapper.toUserResponseDTO(updatedUser);
    }
}