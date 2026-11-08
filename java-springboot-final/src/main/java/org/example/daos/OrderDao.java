
package org.example.daos;

import org.example.models.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // GET all orders
    public List<Order> getOrders() {
        return jdbcTemplate.query(
                "SELECT * FROM orders",
                (rs, rowNum) -> new Order(
                        rs.getInt("id"),
                        rs.getString("username")
                )
        );
    }

    // GET order by ID
    public Order getOrderById(int id) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM orders WHERE id = ?",
                    (rs, rowNum) -> new Order(
                            rs.getInt("id"),
                            rs.getString("username")
                    ),
                    id
            );
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }

    // CREATE order
    public Order createOrder(Order order) {
        String sql = "INSERT INTO orders (username) VALUES (?)";

        jdbcTemplate.update(sql, order.getUsername());

        return jdbcTemplate.queryForObject(
                "SELECT * FROM orders WHERE id = LAST_INSERT_ID()",
                (rs, rowNum) -> new Order(
                        rs.getInt("id"),
                        rs.getString("username")
                )
        );
    }

    // UPDATE order
    public Order updateOrder(Order order, int id) {
        int rowsAffected = jdbcTemplate.update(
                "UPDATE orders SET username = ? WHERE id = ?",
                order.getUsername(),
                id
        );

        if (rowsAffected == 0) {
            return null;
        }

        return getOrderById(id);
    }

    // DELETE order
    public int deleteOrder(int id) {
        return jdbcTemplate.update(
                "DELETE FROM orders WHERE id = ?",
                id
        );
    }
}
