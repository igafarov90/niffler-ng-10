package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.AuthUserEntity;
import guru.qa.niffler.data.entity.SpendEntity;

import java.util.Optional;
import java.util.UUID;

public interface AuthUserDao {
    AuthUserEntity create(AuthUserEntity user);

    Optional<AuthUserEntity> findById(UUID id);

    void delete(AuthUserEntity user);

    AuthUserEntity update(AuthUserEntity user);
}
