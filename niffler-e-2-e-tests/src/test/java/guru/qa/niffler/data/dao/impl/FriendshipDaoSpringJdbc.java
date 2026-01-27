package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.FriendshipDao;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.mapper.FriendshipEntityRowMapper;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FriendshipDaoSpringJdbc implements FriendshipDao {
    private static final Config CFG = Config.getInstance();
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));

    @Override
    public void create(FriendshipEntity friendship) {
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO friendship (requester_id, addressee_id, status, created_date) VALUES (?, ?, ?, ?)");
            ps.setObject(1, friendship.getRequester().getId());
            ps.setObject(2, friendship.getAddressee().getId());
            ps.setString(3, friendship.getStatus().name());
            ps.setDate(4, new java.sql.Date(friendship.getCreatedDate().getTime()));
            return ps;
        });
    }

    @Override
    public Optional<FriendshipEntity> findById(UUID requesterId, UUID addresseeId) {
        try {
            FriendshipEntity entity = jdbcTemplate.queryForObject(
                    "SELECT * FROM friendship WHERE requester_id = ? AND addressee_id = ?",
                    FriendshipEntityRowMapper.instance,
                    requesterId,
                    addresseeId
            );
            return Optional.ofNullable(entity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<FriendshipEntity> findByRequester(UUID requesterId) {
        return jdbcTemplate.query(
                "SELECT * FROM friendship WHERE requester_id = ?",
                FriendshipEntityRowMapper.instance,
                requesterId
        );
    }

    @Override
    public List<FriendshipEntity> findByAddressee(UUID addresseeId) {
        return jdbcTemplate.query(
                "SELECT * FROM friendship WHERE addressee_id = ?",
                FriendshipEntityRowMapper.instance,
                addresseeId
        );
    }

    @Override
    public void delete(UUID requesterId, UUID addresseeId) {
        jdbcTemplate.update(
                "DELETE FROM friendship WHERE requester_id = ? OR addressee_id = ?",
                requesterId,
                addresseeId
        );
    }
}
