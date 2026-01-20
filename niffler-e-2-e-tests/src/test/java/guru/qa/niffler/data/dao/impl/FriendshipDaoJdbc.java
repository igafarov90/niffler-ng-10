package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.FriendshipDao;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.mapper.FriendshipEntityRowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class FriendshipDaoJdbc implements FriendshipDao {
    private static final Config CFG = Config.getInstance();

    @Override
    public void create(FriendshipEntity friendship) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "INSERT INTO friendship (requester_id, addressee_id, status, created_date) VALUES (?, ?, ?, ?)")) {
            ps.setObject(1, friendship.getRequester().getId());
            ps.setObject(2, friendship.getAddressee().getId());
            ps.setString(3, friendship.getStatus().name());
            ps.setDate(4, new java.sql.Date(friendship.getCreatedDate().getTime()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<FriendshipEntity> findById(UUID requesterId, UUID addresseeId) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM friendship WHERE requester_id = ? AND addressee_id = ?")) {
            ps.setObject(1, requesterId);
            ps.setObject(2, addresseeId);

            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    return Optional.of(FriendshipEntityRowMapper.instance.mapRow(rs, 1));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<FriendshipEntity> findByRequester(UUID requesterId) {
        List<FriendshipEntity> friendships = new ArrayList<>();
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM friendship WHERE requester_id = ?")) {
            ps.setObject(1, requesterId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    friendships.add(FriendshipEntityRowMapper.instance.mapRow(rs, rs.getRow()));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return friendships;
    }

    @Override
    public List<FriendshipEntity> findByAddressee(UUID addresseeId) {
        List<FriendshipEntity> friendships = new ArrayList<>();
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM friendship WHERE addressee_id = ?")) {
            ps.setObject(1, addresseeId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    friendships.add(FriendshipEntityRowMapper.instance.mapRow(rs, rs.getRow()));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return friendships;
    }

    @Override
    public void delete(UUID requesterId, UUID addresseeId) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "DELETE FROM friendship WHERE requester_id = ? OR addressee_id = ?")) {
            ps.setObject(1, requesterId);
            ps.setObject(2, addresseeId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
