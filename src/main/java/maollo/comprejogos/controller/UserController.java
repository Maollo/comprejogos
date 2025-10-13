package maollo.comprejogos.controller;

import lombok.RequiredArgsConstructor;
import maollo.comprejogos.domain.UserCompreJogos;
import maollo.comprejogos.service.UserCompreJogosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users") // Define o caminho base para todos os endpoints neste controller
public class UserController {

    private final UserCompreJogosService userService;


    /**
     * Endpoint para registrar um novo usuário.
     * HTTP POST para /api/users/register
     * @param user Os dados do usuário vêm no corpo da requisição em formato JSON.
     * @return O usuário criado com status 201 (Created) ou uma mensagem de erro com status 400 (Bad Request).
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserCompreJogos user) {
        try {
            UserCompreJogos registeredUser = userService.registerUser(user);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            // Captura a exceção que lançamos no service se o login ou email já existirem
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint para buscar um usuário pelo seu ID.
     * HTTP GET para /api/users/{id}
     * @param id O ID do usuário, vindo da URL.
     * @return O usuário encontrado com status 200 (OK) ou status 404 (Not Found) se não existir.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserCompreJogos> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(user -> ResponseEntity.ok(user)) // Se o usuário for encontrado, retorna 200 OK com o usuário no corpo
                .orElse(ResponseEntity.notFound().build()); // Se não, retorna 404 Not Found
    }
}