package maollo.comprejogos.controller;

import lombok.RequiredArgsConstructor;
import maollo.comprejogos.dto.CartItemRequestDTO;
import maollo.comprejogos.dto.CartResponseDTO;
import maollo.comprejogos.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;



    @GetMapping
    public ResponseEntity<CartResponseDTO> getMyCart(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(cartService.getCartForUser(userDetails.getUsername()));
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponseDTO> addItemToMyCart(@AuthenticationPrincipal UserDetails userDetails,
                                                          @Valid @RequestBody CartItemRequestDTO itemDto) {
        return ResponseEntity.ok(cartService.addItemToCart(userDetails.getUsername(), itemDto));
    }

    // Adicione os endpoints para PUT (atualizar quantidade) e DELETE (remover item)
    // Exemplo:
    @DeleteMapping("/items/{gameAppId}")
    public ResponseEntity<CartResponseDTO> removeItemFromMyCart(@AuthenticationPrincipal UserDetails userDetails,
                                                                @PathVariable Long gameAppId) {
        return ResponseEntity.ok(cartService.removeItemFromCart(userDetails.getUsername(), gameAppId));
    }
}