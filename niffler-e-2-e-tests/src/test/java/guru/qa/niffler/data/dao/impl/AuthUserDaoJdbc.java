package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.AuthUserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class AuthUserDaoJdbc implements AuthUserDao {
    private static Config CFG = Config.getInstance();

    private final Connection connection;
    private static PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public AuthUserDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    private static final Logger logger = LoggerFactory.getLogger(AuthUserDaoJdbc.class);

    @Override
    public AuthUserEntity create(AuthUserEntity user) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO public.user (" +
                        "username, " +
                        "password, " +
                        "enabled, " +
                        "account_non_expired, " +
                        "account_non_locked, " +
                        "credentials_non_expired) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, user.getUsername());
            ps.setString(2, passwordEncoder.encode(user.getPassword()));
            ps.setBoolean(3, user.getEnabled());
            ps.setBoolean(4, user.getAccountNonExpired());
            ps.setBoolean(5, user.getAccountNonLocked());
            ps.setBoolean(6, user.getCredentialsNonExpired());
            ps.executeUpdate();
            try (ResultSet resultSet = ps.getGeneratedKeys()) {
                if (resultSet.next()) {
                    UUID generatedKey = resultSet.getObject("id", UUID.class);
                    user.setId(generatedKey);
                }
                logger.info("Пользователь добавлен: id={}, username={}", user.getId(), user.getUsername());
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM public.user WHERE id = ?"
        )) {
            ps.setObject(1, id);
            ps.executeQuery();
            try (ResultSet resultSet = ps.getResultSet()) {
                if (resultSet.next()) {
                    AuthUserEntity ue = new AuthUserEntity();
                    ue.setId(resultSet.getObject("id", UUID.class));
                    ue.setUsername(resultSet.getString("username"));
                    ue.setPassword(resultSet.getString("password"));
                    ue.setEnabled(resultSet.getBoolean("enabled"));
                    ue.setAccountNonExpired(resultSet.getBoolean("account_non_expired"));
                    ue.setAccountNonLocked(resultSet.getBoolean("account_non_locked"));
                    ue.setCredentialsNonExpired(resultSet.getBoolean("credentials_non_expired"));
                    logger.info("Пользователь найден: id={}, username={}", ue.getId(), ue.getUsername());
                    return Optional.of(ue);
                } else {
                    logger.warn("Пользователь с id: {} не найден", id);
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(AuthUserEntity user) {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM public.user WHERE id = ?"
        )) {
            ps.setObject(1, user.getId());
            int rowsRemoved = ps.executeUpdate();
            if (rowsRemoved > 0) {
                logger.info("Пользователь успешно удален user_id: {}.",
                        user.getId());
            } else {
                logger.warn("Пользователь с user_id: {} не найден. Удаление не выполнено.", user.getId());
            }
        } catch (SQLException e) {
            logger.error("Ошибка при удалении пользователя: {}", user.getId(), e);
            throw new RuntimeException();
        }
    }

    @Override
    public AuthUserEntity update(AuthUserEntity user) {
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE public.user SET username = ?, " +
                        "account_non_expired = ?, " +
                        "account_non_locked = ?, " +
                        "credentials_non_expired = ?, " +
                        "enabled = ?, " +
                        "password = ? " +
                        "WHERE id = ?"
        )) {
            ps.setString(1, user.getUsername());
            ps.setBoolean(2, user.getAccountNonExpired());
            ps.setBoolean(3, user.getAccountNonLocked());
            ps.setBoolean(4, user.getCredentialsNonExpired());
            ps.setBoolean(5, user.getEnabled());
            ps.setString(6, passwordEncoder.encode(user.getPassword()));
            ps.setObject(7, user.getId());
           int count = ps.executeUpdate();
            if (count > 0) {
                logger.info("Пользователь обновлен: id={}, username={}", user.getId(), user.getUsername());
            } else {
                logger.warn("Пользователь: id={} не найден. Обновление не выполнено.", user.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }
}
