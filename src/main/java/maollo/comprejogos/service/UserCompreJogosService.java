package maollo.comprejogos.service;


import maollo.comprejogos.domain.UserCompreJogos;

import java.util.Optional;

public interface UserCompreJogosService {


    UserCompreJogos registerUser(UserCompreJogos user);


    Optional<UserCompreJogos> findById(Long id);


    Optional<UserCompreJogos> findByEmail(String email);
}