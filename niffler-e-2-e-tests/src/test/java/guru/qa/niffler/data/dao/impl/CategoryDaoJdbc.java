package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.entity.CategoryEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CategoryDaoJdbc implements CategoryDao {

    private final Connection connection;

    public CategoryDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    private static final Logger logger = LoggerFactory.getLogger(CategoryDaoJdbc.class);

    @Override
    public CategoryEntity create(CategoryEntity category) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO category (name, username, archived) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, category.getName());
            ps.setString(2, category.getUsername());
            ps.setBoolean(3, category.isArchived());
            ps.executeUpdate();
            try (ResultSet resultSet = ps.getGeneratedKeys()) {
                if (resultSet.next()) {
                    UUID generatedKey = resultSet.getObject("id", UUID.class);
                    category.setId(generatedKey);
                }
                return category;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<CategoryEntity> findByUsernameAndCategoryName(String username, String categoryName) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM category WHERE username = ? AND name = ?"
        )) {
            ps.setString(1, username);
            ps.setString(1, categoryName);
            ps.executeQuery();
            try (ResultSet resultSet = ps.getResultSet()) {
                if (resultSet.next()) {
                    CategoryEntity ce = new CategoryEntity();
                    ce.setId(resultSet.getObject("id", UUID.class));
                    ce.setName(resultSet.getString("name"));
                    ce.setUsername(resultSet.getString("username"));
                    ce.setArchived(resultSet.getBoolean("archived"));
                    return Optional.of(ce);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<CategoryEntity> findById(UUID id) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM category WHERE id = ?"
        )) {
            ps.setObject(1, id);
            ps.executeQuery();
            try (ResultSet resultSet = ps.getResultSet()) {
                if (resultSet.next()) {
                    CategoryEntity ce = new CategoryEntity();
                    ce.setId(resultSet.getObject("id", UUID.class));
                    ce.setName(resultSet.getString("name"));
                    ce.setUsername(resultSet.getString("username"));
                    ce.setArchived(resultSet.getBoolean("archived"));
                    return Optional.of(ce);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(CategoryEntity category) {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM category WHERE id = ?"
        )) {
            ps.setObject(1, category.getId());
            int rowsRemoved = ps.executeUpdate();
            if (rowsRemoved > 0) {
                logger.info("Успешно удалена строка с id: {}.",
                        category.getId());
            } else {
                logger.warn("Строка с id: {} не найдена. Удаление не выполнено.", category.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public List<CategoryEntity> findAllByUsername(String username) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM category WHERE username = ?"
        )) {
            ps.setString(1, username);
            ps.executeQuery();
            try (ResultSet resultSet = ps.getResultSet()) {
                List<CategoryEntity> ceList = new ArrayList<>();
                while (resultSet.next()) {
                    CategoryEntity ce = new CategoryEntity();
                    ce.setId(resultSet.getObject("id", UUID.class));
                    ce.setName(resultSet.getString("name"));
                    ce.setUsername(resultSet.getString("username"));
                    ce.setArchived(resultSet.getBoolean("archived"));
                    ceList.add(ce);
                }
                if (!ceList.isEmpty()) {
                    logger.info("Для пользователя '{}' найдено категорий: {}", username, ceList.size());
                } else {
                    logger.warn("Для пользователя '{}' не найдено ни одной категории", username);
                }
                return ceList;
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public List<CategoryEntity> findAll() {
        List<CategoryEntity> category = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM category")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CategoryEntity ce = new CategoryEntity();
                    ce.setId(rs.getObject("id", UUID.class));
                    ce.setName(rs.getString("name"));
                    ce.setUsername(rs.getString("username"));
                    ce.setArchived(rs.getBoolean("archived"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return category;
    }
}
