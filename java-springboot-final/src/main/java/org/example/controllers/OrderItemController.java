package org.example.controllers;

import org.example.daos.OrderItemDao;
import org.example.models.OrderItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api")
@RestController
public class OrderItemController {

    private final OrderItemDao orderItemDao;

    public OrderItemController(OrderItemDao orderItemDao) {
        this.orderItemDao = orderItemDao;
    }

    @GetMapping("/order-items")
    public List<OrderItem> getOrderItems() {
        return orderItemDao.getOrderItems();
    }

    @GetMapping("/order-items/{id}")
    public OrderItem getOrderItemById(@PathVariable("id") int id) {
        OrderItem orderItem = orderItemDao.getOrderItemById(id);

        if (orderItem == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return orderItem;
    }

    @PostMapping("/order-items")
    public ResponseEntity<OrderItem> createOrderItem(
            @RequestBody OrderItem orderItem) {

        OrderItem createdOrderItem = orderItemDao.createOrderItem(orderItem);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrderItem);
    }

    @PutMapping("/order-items/{id}")
    public OrderItem updateOrderItem(
            @PathVariable("id") int id,
            @RequestBody OrderItem orderItem) {

        OrderItem updatedOrderItem = orderItemDao.updateOrderItem(orderItem, id);

        if (updatedOrderItem == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return updatedOrderItem;
    }

    @DeleteMapping("/order-items/{id}")
    public int deleteOrderItem(@PathVariable("id") int id) {
        int rowsAffected = orderItemDao.deleteOrderItem(id);

        if (rowsAffected == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return rowsAffected;
    }
}