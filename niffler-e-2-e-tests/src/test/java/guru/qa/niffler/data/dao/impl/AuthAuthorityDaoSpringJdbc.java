package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.AuthorityEntity;
import guru.qa.niffler.data.mapper.AuthAuthorityEntityRowMapper;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class AuthAuthorityDaoSpringJdbc implements AuthAuthorityDao {

    public AuthAuthorityDaoSpringJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private DataSource dataSource;

    @Override
    public void create(AuthorityEntity... authority) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.batchUpdate(
                "INSERT INTO public.authority (user_id, authority) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setObject(1, authority[i].getUser().getId());
                        ps.setObject(2, authority[i].getAuthority().name());
                    }

                    @Override
                    public int getBatchSize() {
                        return authority.length;
                    }
                }
        );
    }

    @Override
    public List<AuthorityEntity> findByUserId(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(
                "SELECT * FROM public.authority WHERE id = ?",
                AuthAuthorityEntityRowMapper.instance,
                id
        );
    }

    @Override
    public void deleteByUserId(AuthorityEntity authority) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(
                "DELETE FROM authority WHERE id = ?",
                authority.getUser().getId()
        );
    }

    @Override
    public List<AuthorityEntity> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(
                "SELECT * FROM authority",
                AuthAuthorityEntityRowMapper.instance
        );
    }
}
