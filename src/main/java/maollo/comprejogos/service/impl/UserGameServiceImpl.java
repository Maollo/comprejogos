package maollo.comprejogos.service.impl;

import lombok.RequiredArgsConstructor;
import maollo.comprejogos.domain.UserCompreJogos;
import maollo.comprejogos.dto.UserGameResponseDTO;
import maollo.comprejogos.exception.ResourceNotFoundException;
import maollo.comprejogos.mapper.UserGameMapper;
import maollo.comprejogos.repository.UserCompreJogosRepository;
import maollo.comprejogos.repository.UserGameRepository;
import maollo.comprejogos.service.UserGameService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserGameServiceImpl implements UserGameService {

    private final UserGameRepository userGameRepository;
    private final UserCompreJogosRepository userRepository;
    private final UserGameMapper userGameMapper;

    @Override
    public List<UserGameResponseDTO> findGamesByUser(String userEmail) {
        UserCompreJogos user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + userEmail));

        return userGameRepository.findByUser(user).stream()
                .map(userGameMapper::toUserGameResponseDTO)
                .collect(Collectors.toList());
    }
}