package maollo.comprejogos.service;

import maollo.comprejogos.dto.UserGameResponseDTO;
import java.util.List;

public interface UserGameService {
    List<UserGameResponseDTO> findGamesByUser(String userEmail);
}