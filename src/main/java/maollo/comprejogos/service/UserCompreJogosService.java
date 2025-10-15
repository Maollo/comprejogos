package maollo.comprejogos.service;


import maollo.comprejogos.domain.UserCompreJogos;
import maollo.comprejogos.dto.UserResponseDTO;
import maollo.comprejogos.dto.UserUpdateProfileDTO;

import java.util.Optional;

public interface UserCompreJogosService {


    UserCompreJogos registerUser(UserCompreJogos user);


    Optional<UserCompreJogos> findById(Long id);


    Optional<UserCompreJogos> findByEmail(String email);
    // --- NOVO MÉTODO ---
    UserResponseDTO updateUserProfile(String userEmail, UserUpdateProfileDTO profileDto);

}