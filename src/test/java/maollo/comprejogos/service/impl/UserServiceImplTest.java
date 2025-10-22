package maollo.comprejogos.service.impl;

import lombok.extern.log4j.Log4j2;
import maollo.comprejogos.utils.ERole;
import maollo.comprejogos.domain.Role;
import maollo.comprejogos.domain.UserCompreJogos;
import maollo.comprejogos.repository.RoleRepository;
import maollo.comprejogos.repository.UserCompreJogosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Log4j2
@ExtendWith(MockitoExtension.class) // Habilita o Mockito para este teste
class UserServiceImplTest {

    // Cria "Mocks" (simulações) das dependências que o UserService precisa
    @Mock
    private UserCompreJogosRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    // Injeta os Mocks criados acima na nossa classe de serviço
    @InjectMocks
    private UserCompreJogosServiceImpl userService;

    private UserCompreJogos testUser;
    private Role userRole;

    @BeforeEach
    void setUp() {
        // Configuração inicial para os testes
        testUser = new UserCompreJogos();
        testUser.setLogin("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("plainPassword123");

        userRole = new Role(ERole.ROLE_USER);
        testUser.setRoles( Set.of(userRole) );
    }

    @Test
    void registerUser_ShouldSucceed_WhenDataIsValid() {

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        when(passwordEncoder.encode("plainPassword123")).thenReturn("hashedPassword");

        //when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(userRole));

        when(userRepository.save(any(UserCompreJogos.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserCompreJogos savedUser = userService.registerUser(testUser);

        assertNotNull(savedUser);
        assertEquals("test@example.com", savedUser.getEmail());
        assertEquals("hashedPassword", savedUser.getPassword());
        //assertTrue(savedUser.getRoles().contains(userRole)); // Verifica se a role foi atribuída
        assertEquals(1, savedUser.getRoles().size());
    }

    @Test
    void registerUser_ShouldThrowException_WhenEmailAlreadyExists() {

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));


        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            userService.registerUser(testUser);
        });

        assertEquals("Email já está em uso.", exception.getMessage());
    }
}