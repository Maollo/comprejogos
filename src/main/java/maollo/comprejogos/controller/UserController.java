package maollo.comprejogos.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import maollo.comprejogos.domain.UserCompreJogos;
import maollo.comprejogos.dto.UserRegisterDTO;
import maollo.comprejogos.dto.UserResponseDTO;
import maollo.comprejogos.mapper.UserMapper;
import maollo.comprejogos.service.UserCompreJogosService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users") // Define o caminho base para todos os endpoints neste controller
public class UserController {

    private final UserCompreJogosService userService;
    private final UserMapper userMapper; // Injetar o Mapper


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
}