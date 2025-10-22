package maollo.comprejogos.service;

import maollo.comprejogos.dto.CartItemRequestDTO;
import maollo.comprejogos.dto.CartResponseDTO;

public interface CartService {
    CartResponseDTO getCartForUser(String userEmail);
    CartResponseDTO addItemToCart(String userEmail, CartItemRequestDTO itemDto);
    CartResponseDTO updateItemInCart(String userEmail, Long gameAppId, int quantity);
    CartResponseDTO removeItemFromCart(String userEmail, Long gameAppId);
}