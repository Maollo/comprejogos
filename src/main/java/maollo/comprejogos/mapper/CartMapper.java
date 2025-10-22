package maollo.comprejogos.mapper;

import maollo.comprejogos.dto.CartItemResponseDTO;
import maollo.comprejogos.dto.CartResponseDTO;
import maollo.comprejogos.domain.Cart;
import maollo.comprejogos.domain.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Component // Anotação para que o Spring possa injetar esta classe em outros componentes (como o CartService)
public class CartMapper {

    /**
     * Converte a entidade principal Cart para o seu DTO de resposta.
     * @param cart A entidade Cart vinda do banco de dados.
     * @return Um CartResponseDTO populado para ser enviado na API.
     */
    public CartResponseDTO toCartResponseDTO(Cart cart) {
        if (cart == null) {
            return null;
        }

        CartResponseDTO dto = new CartResponseDTO();
        dto.setId(cart.getId());
        dto.setTotal(cart.getTotal()); // Usa o método utilitário que criamos na entidade

        // Converte cada CartItem da entidade para seu respectivo DTO
        dto.setItems(cart.getItems().stream()
                .map(this::toCartItemResponseDTO) // Chama o método auxiliar abaixo para cada item
                .collect(Collectors.toSet()));

        return dto;
    }

    /**
     * Converte uma entidade CartItem para o seu DTO de resposta.
     * @param item A entidade CartItem vinda do banco de dados.
     * @return Um CartItemResponseDTO populado.
     */
    public CartItemResponseDTO toCartItemResponseDTO(CartItem item) {
        if (item == null) {
            return null;
        }

        CartItemResponseDTO dto = new CartItemResponseDTO();
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());

        // Calcula o subtotal para este item
        dto.setSubtotal(item.getPrice().multiply(new BigDecimal(item.getQuantity())));

        // Pega os detalhes do jogo associado a este item do carrinho
        if (item.getGame() != null) {
            dto.setGameAppId(item.getGame().getAppId());
            dto.setGameName(item.getGame().getName());
            dto.setGameImageUrl(item.getGame().getImageUrl());
        }

        return dto;
    }
}