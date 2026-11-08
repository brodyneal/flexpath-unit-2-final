package org.example.daos;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.util.List;
import org.example.models.Product;
import java.sql.PreparedStatement;
import java.sql.Statement;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.dao.EmptyResultDataAccessException;

@Component
public class ProductDao {

    private final JdbcTemplate jdbcTemplate;

    public ProductDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Product> getProducts() {
        return jdbcTemplate.query(
                "SELECT * FROM products",
                (resultSet, rowNum) -> new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getBigDecimal("price")
                )
        );
    }

    public Product getProductById(int id) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM products WHERE id = ?",
                    (resultSet, rowNum) -> new Product(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getBigDecimal("price")
                    ),
                    id
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Product createProduct(Product product) {
        String sql = "INSERT INTO products (name, price) VALUES (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, product.getName());
            ps.setBigDecimal(2, product.getPrice());

            return ps;
        }, keyHolder);

        return getProductById(keyHolder.getKey().intValue());
    }

    public Product updateProduct(Product product, int id) {
        int rowsAffected = jdbcTemplate.update(
                "UPDATE products SET name = ?, price = ? WHERE id = ?",
                product.getName(),
                product.getPrice(),
                id
        );

        if (rowsAffected == 0) {
            return null;
        }

        return getProductById(id);
    }

    public int deleteProduct(int id) {
        return jdbcTemplate.update(
                "DELETE FROM products WHERE id = ?",
                id
        );
    }
}
