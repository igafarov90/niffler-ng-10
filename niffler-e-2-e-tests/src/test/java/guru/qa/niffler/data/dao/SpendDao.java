package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.SpendEntity;

import java.util.Optional;
import java.util.UUID;

public interface SpendDao {
    SpendEntity create(SpendEntity spend);

    Optional<SpendEntity> findById(UUID id);

    void delete(SpendEntity spend);
}
