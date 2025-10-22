package maollo.comprejogos.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.Valid;
import maollo.comprejogos.dto.AuthRequestDTO;
import maollo.comprejogos.dto.AuthResponseDTO;
import maollo.comprejogos.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody AuthRequestDTO authRequest) throws Exception {
        try {
            // Tenta autenticar
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            // 3. ADICIONE O LOG DE AVISO (WARN) PARA LOGIN FALHO
            logger.warn("Falha na tentativa de login para o usuário: {}", authRequest.getEmail());
            throw new Exception("Email ou senha inválidos", e);
        }

        // Se a autenticação for bem-sucedida, gera o token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
        final String jwt = jwtService.generateToken(userDetails);

        // Retorna o token
        logger.info("Usuário '{}' autenticado com sucesso.", authRequest.getEmail());
        return ResponseEntity.ok(new AuthResponseDTO(jwt));
    }
}