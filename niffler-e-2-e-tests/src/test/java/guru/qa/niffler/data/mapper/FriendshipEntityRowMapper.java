package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class FriendshipEntityRowMapper implements RowMapper<FriendshipEntity> {

    public static FriendshipEntityRowMapper instance = new FriendshipEntityRowMapper();

    public FriendshipEntityRowMapper() {
    }

    @Nonnull
    @Override
    public FriendshipEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        FriendshipEntity fe = new FriendshipEntity();

        UserEntity requester = new UserEntity();
        requester.setId(rs.getObject("requester_id", UUID.class));

        UserEntity addressee = new UserEntity();
        addressee.setId(rs.getObject("addressee_id", UUID.class));

        fe.setRequester(requester);
        fe.setAddressee(addressee);
        fe.setCreatedDate(rs.getDate("created_date"));
        fe.setStatus(FriendshipStatus.valueOf(rs.getString("status")));

        return fe;
    }

}
