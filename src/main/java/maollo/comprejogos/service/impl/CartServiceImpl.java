package maollo.comprejogos.service.impl;

// ... (imports para User, Game, Cart, Repositories, Mappers, DTOs, Exceptions)
import lombok.RequiredArgsConstructor;
import maollo.comprejogos.domain.Cart;
import maollo.comprejogos.domain.CartItem;
import maollo.comprejogos.domain.Game;
import maollo.comprejogos.domain.UserCompreJogos;
import maollo.comprejogos.dto.CartItemRequestDTO;
import maollo.comprejogos.dto.CartResponseDTO;
import maollo.comprejogos.mapper.CartMapper;
import maollo.comprejogos.repository.CartRepository;
import maollo.comprejogos.repository.GameRepository;
import maollo.comprejogos.repository.UserCompreJogosRepository;
import maollo.comprejogos.service.CartService;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserCompreJogosRepository userRepository;
    private final GameRepository gameRepository;
    private final CartMapper cartMapper; // Supondo que você criou um CartMapper

    // Construtor com @Autowired

    @Override
    public CartResponseDTO getCartForUser(String userEmail) {
        UserCompreJogos user = userRepository.findByEmail(userEmail).orElseThrow(/*...exceção...*/);
        Cart cart = findOrCreateCartForUser(user);
        return cartMapper.toCartResponseDTO(cart);
    }

    @Override
    public CartResponseDTO addItemToCart(String userEmail, CartItemRequestDTO itemDto) {
        UserCompreJogos user = userRepository.findByEmail(userEmail).orElseThrow(/*...exceção...*/);
        Game game = gameRepository.findByAppId(itemDto.getGameAppId()).orElseThrow(/*...exceção...*/);
        Cart cart = findOrCreateCartForUser(user);

        Optional<CartItem> existingItem = cart.getItems().stream()
            .filter(item -> item.getGame().getAppId().equals(itemDto.getGameAppId()))
            .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + itemDto.getQuantity());
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setGame(game);
            newItem.setQuantity(itemDto.getQuantity());
            // Lógica para pegar o preço correto (promocional ou não)
            newItem.setPrice(game.isPromotionActive() ? game.getPromotionalPrice() : game.getPrice());
            cart.getItems().add(newItem);
        }

        return cartMapper.toCartResponseDTO(cartRepository.save(cart));
    }

    // ... Implementar updateItemInCart e removeItemFromCart de forma similar

    private Cart findOrCreateCartForUser(UserCompreJogos user) {
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });
    }

    @Override
    public CartResponseDTO updateItemInCart(String userEmail, Long gameAppId, int quantity) {
        return null;
    }

    @Override
    public CartResponseDTO removeItemFromCart(String userEmail, Long gameAppId) {
        return null;
    }
}