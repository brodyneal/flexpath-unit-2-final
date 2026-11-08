package org.example.controllers;

import org.example.daos.OrderDao;
import org.example.models.Order;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api")
@RestController
public class OrderController {

    private final OrderDao orderDao;

    public OrderController(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @GetMapping("/orders")
    public List<Order> getOrders() {
        return orderDao.getOrders();
    }

    @GetMapping("/orders/{id}")
    public Order getOrderById(@PathVariable("id") int id) {
        Order order = orderDao.getOrderById(id);

        if (order == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND
            );
        }

        return order;
    }

    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order createdOrder = orderDao.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @PutMapping("/orders/{id}")
    public Order updateOrder(
            @PathVariable("id") int id,
            @RequestBody Order order) {

        Order updatedOrder = orderDao.updateOrder(order, id);

        if (updatedOrder == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return updatedOrder;
    }

    @DeleteMapping("/orders/{id}")
    public int deleteOrder(@PathVariable("id") int id) {
        int rowsAffected = orderDao.deleteOrder(id);

        if (rowsAffected == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return rowsAffected;
    }
}