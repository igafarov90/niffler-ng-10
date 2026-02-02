package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.spend.SpendEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface SpendDao {

    @Nonnull
    SpendEntity create(SpendEntity spend);

    Optional<SpendEntity> findById(UUID id);

    void delete(SpendEntity spend);

    @Nonnull
    List<SpendEntity> findAll();

    @Nonnull
    SpendEntity update(SpendEntity spend);

    Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description);
}
