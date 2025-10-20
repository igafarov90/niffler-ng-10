package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.SpendEntity;

import java.util.Optional;
import java.util.UUID;

public interface SpendDao {
    SpendEntity create(SpendEntity spend);

    Optional<SpendEntity> findSpendById(UUID id);

    void deleteSpend(SpendEntity spend);
}
