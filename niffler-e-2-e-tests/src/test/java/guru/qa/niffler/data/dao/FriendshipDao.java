package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.userdata.FriendshipEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface FriendshipDao {

    void create(FriendshipEntity friendship);
    @Nonnull
    Optional<FriendshipEntity> findById(UUID requesterId, UUID addresseeId);

    @Nonnull
    List<FriendshipEntity> findByRequester(UUID requesterId);

    @Nonnull
    List<FriendshipEntity> findByAddressee(UUID requesterId);

    void delete(UUID requesterId, UUID addresseeId);
}
