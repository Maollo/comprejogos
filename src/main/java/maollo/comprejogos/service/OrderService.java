package maollo.comprejogos.service;

import maollo.comprejogos.domain.Order;
import maollo.comprejogos.domain.UserCompreJogos;
import java.util.List;

public interface OrderService {

    /**
     * Cria um novo pedido para um usuário com uma lista de jogos.
     * @param user O usuário que está fazendo o pedido.
     * @param gameAppIds A lista de AppIDs dos jogos a serem comprados.
     * @return O pedido criado e salvo no banco.
     * @throws Exception se algum jogo não for encontrado ou outra regra de negócio falhar.
     */
    Order createOrder(UserCompreJogos user, List<Long> gameAppIds) throws Exception;

    /**
     * Busca o histórico de pedidos de um usuário.
     * @param user O usuário.
     * @return Uma lista de seus pedidos.
     */
    List<Order> findOrdersByUser(UserCompreJogos user);
}