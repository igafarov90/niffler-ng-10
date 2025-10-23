package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.AuthUserEntity;
import guru.qa.niffler.data.entity.Authority;
import guru.qa.niffler.data.entity.AuthorityEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {
    private static Config CFG = Config.getInstance();

    private final Connection connection;

    public AuthAuthorityDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    private static final Logger logger = LoggerFactory.getLogger(AuthAuthorityDaoJdbc.class);

    @Override
    public AuthorityEntity create(AuthorityEntity authority) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO public.authority (user_id, authority) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setObject(1, authority.getUser().getId());
            ps.setString(2, authority.getAuthority().name());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    UUID generatedKey = rs.getObject("id", UUID.class);
                    authority.setId(generatedKey);
                }
                logger.info("Полномочие {} добавлено для пользователя: {}",
                        authority.getAuthority(), authority.getUser().getId());
                return authority;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthorityEntity> findByUserId(UUID id) {
        List<AuthorityEntity> authorities = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM public.authority WHERE user_id = ?"
        )) {
            ps.setObject(1, id);
            ps.executeQuery();
            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setId(rs.getObject("id", UUID.class));

                    String authorityStr = rs.getString("authority");
                    Authority authority = Authority.valueOf(authorityStr);
                    ae.setAuthority(authority);

                    AuthUserEntity ue = new AuthUserEntity();
                    ue.setId(rs.getObject("user_id", UUID.class));
                    ae.setUser(ue);

                    authorities.add(ae);
                }
                if (!authorities.isEmpty()) {
                    logger.info("Найдено полномочий для пользователя {}: {}", id, authorities.size());

                } else {
                    logger.warn("Полномочия для пользователя с id: {} не найдены", id);
                }
                return authorities;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteByUserId(AuthorityEntity authority) {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM public.authority WHERE user_id = ?"
        )) {
            ps.setObject(1, authority.getUser().getId());
            int rowsRemoved = ps.executeUpdate();
            if (rowsRemoved > 0) {
                logger.info("Успешно удалены полномочия для user_id: {}.",
                        authority.getUser().getId());
            } else {
                logger.warn("Полномочия для user_id: {} не найдены. Удаление не выполнено.", authority.getUser().getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }
}
