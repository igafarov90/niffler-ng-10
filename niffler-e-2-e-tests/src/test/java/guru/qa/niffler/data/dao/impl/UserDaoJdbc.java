package guru.qa.niffler.data.dao.impl;


import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.model.CurrencyValues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class UserDaoJdbc implements UserDao {

    private final Connection connection;
    private static final Logger logger = LoggerFactory.getLogger(UserDaoJdbc.class);

    public UserDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public UserEntity create(UserEntity user) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO public.user(username, currency, firstname, surname, photo, photo_small, full_name)" +
                        "VALUES(?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getCurrency().name());
            ps.setString(3, user.getFirstname());
            ps.setString(4, user.getSurname());
            ps.setBytes(5, user.getPhoto());
            ps.setBytes(6, user.getPhotoSmall());
            ps.setString(7, user.getFullname());
            ps.executeUpdate();
            try (ResultSet resultSet = ps.getGeneratedKeys()) {
                if (resultSet.next()) {
                    UUID generatedKey = resultSet.getObject("id", UUID.class);
                    user.setId(generatedKey);
                }
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM public.user WHERE username = ?"
        )) {
            ps.setString(1, username);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    UserEntity ue = new UserEntity();
                    ue.setId(rs.getObject("id", UUID.class));
                    ue.setUsername(rs.getString("username"));
                    ue.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                    ue.setFirstname(rs.getString("firstname"));
                    ue.setSurname(rs.getString("surname"));
                    ue.setPhoto(rs.getBytes("photo"));
                    ue.setPhotoSmall(rs.getBytes("photo_small"));
                    ue.setFullname(rs.getString("fullname"));
                    return Optional.of(ue);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM public.user WHERE id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    UserEntity ue = new UserEntity();
                    ue.setId(rs.getObject("id", UUID.class));
                    ue.setUsername(rs.getString("username"));
                    ue.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                    ue.setFirstname(rs.getString("firstname"));
                    ue.setSurname(rs.getString("surname"));
                    ue.setPhoto(rs.getBytes("photo"));
                    ue.setPhotoSmall(rs.getBytes("photo_small"));
                    ue.setFullname(rs.getString("fullname"));
                    return Optional.of(ue);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserEntity update(UserEntity user) {
        return user;
    }

    @Override
    public void delete(UserEntity user) {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM public.user WHERE id = ?"
        )) {
            ps.setObject(1, user.getId());
            int rowsRemoved = ps.executeUpdate();
            if (rowsRemoved > 0) {
                logger.info("Успешно удалена строка с id: {}.",
                        user.getId());
            } else {
                logger.warn("Строка с id: {} не найдена. Удаление не выполнено.", user.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }
}
