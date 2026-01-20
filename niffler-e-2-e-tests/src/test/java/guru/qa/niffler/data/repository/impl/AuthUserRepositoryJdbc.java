package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.mapper.AuthUserEntityRowMapper;
import guru.qa.niffler.data.repository.AuthUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class AuthUserRepositoryJdbc implements AuthUserRepository {

    private static final Config CFG = Config.getInstance();

    private static final Logger logger = LoggerFactory.getLogger(AuthUserRepositoryJdbc.class);

    @Override
    public AuthUserEntity create(AuthUserEntity user) {
        try (PreparedStatement userPs = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"user\" (" +
                        "username, " +
                        "password, " +
                        "enabled, " +
                        "account_non_expired, " +
                        "account_non_locked, " +
                        "credentials_non_expired) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        );
             PreparedStatement authorityPs = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                     "INSERT INTO authority (user_id, authority) VALUES (?, ?)")
        ) {
            userPs.setString(1, user.getUsername());
            userPs.setString(2, user.getPassword());
            userPs.setBoolean(3, user.getEnabled());
            userPs.setBoolean(4, user.getAccountNonExpired());
            userPs.setBoolean(5, user.getAccountNonLocked());
            userPs.setBoolean(6, user.getCredentialsNonExpired());
            userPs.executeUpdate();
            final UUID generatedKey;
            try (ResultSet resultSet = userPs.getGeneratedKeys()) {
                if (resultSet.next()) {
                    generatedKey = resultSet.getObject("id", UUID.class);
                    user.setId(generatedKey);

                    for (AuthorityEntity a : user.getAuthorities()) {
                        authorityPs.setObject(1, generatedKey);
                        authorityPs.setString(2, a.getAuthority().name());
                        authorityPs.addBatch();
                        logger.info("полномочия добавлены: id={}", a.getAuthority().name());
                    }
                    authorityPs.executeBatch();
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
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT a.id as authority_id, " +
                        "a.authority, " +
                        "a.user_id as id, " +
                        "u.username, " +
                        "u.password, " +
                        "u.enabled, " +
                        "u.account_non_expired, " +
                        "u.account_non_locked, " +
                        "u.credentials_non_expired " +
                        "FROM \"user\" u join authority a on u.id = a.user_id WHERE u.id = ?"
        )) {
            ps.setObject(1, id);
            ps.executeQuery();
            try (ResultSet resultSet = ps.getResultSet()) {
                AuthUserEntity user = null;
                List<AuthorityEntity> authorities = new ArrayList<>();
                while (resultSet.next()) {
                    if (user == null) {
                        user = AuthUserEntityRowMapper.instance.mapRow(resultSet, 1);
                    }

                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setUser(user);
                    ae.setId(resultSet.getObject("a.id", UUID.class));
                    ae.setAuthority(Authority.valueOf(resultSet.getString("authority")));

                    authorities.add(ae);

                    AuthUserEntity ue = new AuthUserEntity();
                    ue.setId(resultSet.getObject("id", UUID.class));
                    ue.setUsername(resultSet.getString("username"));
                    ue.setPassword(resultSet.getString("password"));
                    ue.setEnabled(resultSet.getBoolean("enabled"));
                    ue.setAccountNonExpired(resultSet.getBoolean("account_non_expired"));
                    ue.setAccountNonLocked(resultSet.getBoolean("account_non_locked"));
                    ue.setCredentialsNonExpired(resultSet.getBoolean("credentials_non_expired"));
                }
                if (user == null) {
                    return Optional.empty();
                } else {
                    user.setAuthorities(authorities);
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
