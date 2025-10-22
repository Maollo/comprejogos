package maollo.comprejogos.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import maollo.comprejogos.domain.UserCompreJogos;
import maollo.comprejogos.dto.UserRegisterDTO;
import maollo.comprejogos.dto.UserResponseDTO;
import maollo.comprejogos.dto.UserUpdateProfileDTO;
import maollo.comprejogos.mapper.UserMapper;
import maollo.comprejogos.service.UserCompreJogosService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import maollo.comprejogos.dto.UserGameResponseDTO;
import maollo.comprejogos.service.UserGameService;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users") // Define o caminho base para todos os endpoints neste controller
public class UserController {

    private final UserGameService userGameService;
    private final UserCompreJogosService userService;
    private final UserMapper userMapper;


    /**
     * Endpoint para registrar um novo usuário.
     * HTTP POST para /api/users/register
     *
     * @param user Os dados do usuário vêm no corpo da requisição em formato JSON.
     * @return O usuário criado com status 201 (Created) ou uma mensagem de erro com status 400 (Bad Request).
     */
    @PostMapping("/register")
    public UserResponseDTO registerUser(@Valid @RequestBody UserRegisterDTO user) {
        UserCompreJogos newUser = UserCompreJogos.builder().email(user.getEmail()).login(user.getLogin()).password(user.getPassword()).build();
        UserCompreJogos registeredUser = userService.registerUser(newUser);

        return userMapper.toUserResponseDTO(registeredUser);

    }
    /**
     * Endpoint para buscar a biblioteca de jogos ("Meus Jogos")
     * do usuário autenticado.
     */
    @GetMapping("/me/games")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserGameResponseDTO>> getMyGames(@AuthenticationPrincipal UserDetails userDetails) {
        List<UserGameResponseDTO> myGames = userGameService.findGamesByUser(userDetails.getUsername());
        return ResponseEntity.ok(myGames);
    }
    /**
     * Endpoint para buscar um usuário pelo seu ID.
     * HTTP GET para /api/users/{id}
     *
     * @param id O ID do usuário, vindo da URL.
     * @return O usuário encontrado com status 200 (OK) ou status 404 (Not Found) se não existir.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(userMapper::toUserResponseDTO) // Se o usuário for encontrado, retorna 200 OK com o usuário no corpo
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); // Se não, retorna 404 Not Found
    }
    /**
     * Endpoint para buscar os dados do usuário atualmente autenticado.
     * @param userDetails Objeto injetado pelo Spring Security contendo os dados do principal (usuário logado).
     * @return Os dados do usuário em um DTO de resposta.
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        // userDetails.getUsername() retorna o email, pois foi o que definimos no nosso UserDetails
        return userService.findByEmail(userDetails.getUsername())
                .map(userMapper::toUserResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint para que o usuário autenticado atualize seu próprio perfil.
     * @param userDetails O principal do usuário logado.
     * @param profileDto Os dados a serem atualizados.
     * @return Os dados atualizados do usuário.
     */
    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> updateCurrentUser(@AuthenticationPrincipal UserDetails userDetails,
                                                             @Valid @RequestBody UserUpdateProfileDTO profileDto) {
        String userEmail = userDetails.getUsername();
        UserResponseDTO updatedUser = userService.updateUserProfile(userEmail, profileDto);
        return ResponseEntity.ok(updatedUser);
    }
}