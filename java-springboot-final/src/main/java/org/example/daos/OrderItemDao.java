package org.example.daos;

import org.example.models.OrderItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Component
public class OrderItemDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderItemDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<OrderItem> getOrderItems() {
        return jdbcTemplate.query(
                "SELECT * FROM order_items",
                (rs, rowNum) -> new OrderItem(
                        rs.getInt("id"),
                        rs.getInt("order_id"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity")
                )
        );
    }

    public OrderItem getOrderItemById(int id) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM order_items WHERE id = ?",
                    (rs, rowNum) -> new OrderItem(
                            rs.getInt("id"),
                            rs.getInt("order_id"),
                            rs.getInt("product_id"),
                            rs.getInt("quantity")
                    ),
                    id
            );
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }

    public OrderItem createOrderItem(OrderItem orderItem) {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS
            );
            statement.setInt(1, orderItem.getOrderId());
            statement.setInt(2, orderItem.getProductId());
            statement.setInt(3, orderItem.getQuantity());
            return statement;
        }, keyHolder);

        int newId = keyHolder.getKey().intValue();

        return getOrderItemById(newId);
    }

    public OrderItem updateOrderItem(OrderItem orderItem, int id) {
        int rowsAffected = jdbcTemplate.update(
                "UPDATE order_items SET order_id = ?, product_id = ?, quantity = ? WHERE id = ?",
                orderItem.getOrderId(),
                orderItem.getProductId(),
                orderItem.getQuantity(),
                id
        );

        if (rowsAffected == 0) {
            return null;
        }

        return getOrderItemById(id);
    }

    public int deleteOrderItem(int id) {
        return jdbcTemplate.update(
                "DELETE FROM order_items WHERE id = ?",
                id
        );
    }
}