package maollo.comprejogos.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import maollo.comprejogos.domain.Cart;
import maollo.comprejogos.domain.CartItem;
import maollo.comprejogos.domain.Game;
import maollo.comprejogos.domain.UserCompreJogos;
import maollo.comprejogos.dto.CartItemRequestDTO;
import maollo.comprejogos.dto.CartResponseDTO;
import maollo.comprejogos.exception.ResourceNotFoundException;
import maollo.comprejogos.mapper.CartMapper;
import maollo.comprejogos.repository.CartItemRepository;
import maollo.comprejogos.repository.CartRepository;
import maollo.comprejogos.repository.GameRepository;
import maollo.comprejogos.repository.UserCompreJogosRepository;
import maollo.comprejogos.service.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
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
        // Busca o usuário ou lança exceção
        UserCompreJogos user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + userEmail));

        // Busca o carrinho do usuário ou lança exceção
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Carrinho não encontrado para o usuário: " + userEmail));

        Set<CartItem> items = cart.getItems();
        log.info("Itens no carrinho antes da remoção: {}", items);

        // --- Use um Iterator para remover com segurança ---
        boolean itemFoundAndProcessed = false;
        Iterator<CartItem> iterator = items.iterator();
        while (iterator.hasNext()) {
            CartItem item = iterator.next();
            Game game = item.getGame();
            log.info("Verificando item: {}", game.getName());

            if (game.getAppId().equals(gameAppId)) {
                log.info("Item encontrado! AppId: {}, Quantidade: {}", gameAppId, item.getQuantity());

                if (item.getQuantity() > 1) {
                    // Se a quantidade for maior que 1, apenas diminui
                    item.setQuantity(item.getQuantity() - 1);
                    log.info("Quantidade diminuída para {}", item.getQuantity());
                } else {
                    // Se a quantidade for 1, remove o item da COLEÇÃO
                    iterator.remove(); // <-- A MUDANÇA PRINCIPAL!
                    log.info("Item removido da coleção do carrinho.");
                    // JPA cuidará da exclusão do banco devido ao orphanRemoval=true
                }
                itemFoundAndProcessed = true;
                break; // Sai do loop, pois já processamos o item
            }
        }

        if (!itemFoundAndProcessed) {
            log.warn("Item com AppId {} não encontrado no carrinho do usuário {}", gameAppId, userEmail);
            // Você pode lançar uma exceção aqui se preferir
            throw new ResourceNotFoundException("Item não encontrado no carrinho: " + gameAppId);
            // Ou apenas retornar o carrinho inalterado
            //return cartMapper.toCartResponseDTO(cart);
        }

        // Salva a entidade PAI (Cart) após modificar a coleção de filhos
        Cart updatedCart = cartRepository.save(cart);
        log.info("Carrinho salvo após modificação.");

        // Retorna o DTO do carrinho atualizado
        return cartMapper.toCartResponseDTO(updatedCart);
    }
}