package maollo.comprejogos.service;

import maollo.comprejogos.domain.Order;
import maollo.comprejogos.domain.UserCompreJogos;
import maollo.comprejogos.dto.OrderResponseDTO;

import java.util.List;

public interface OrderService {

    OrderResponseDTO checkout(String userEmail);
    Order createOrder(UserCompreJogos user, List<Long> gameAppIds) throws Exception;

    List<OrderResponseDTO> findOrdersByUser(String userEmail);
}