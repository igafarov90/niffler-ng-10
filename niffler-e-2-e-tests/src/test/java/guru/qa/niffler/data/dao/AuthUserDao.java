package guru.qa.niffler.data.dao;


import guru.qa.niffler.data.entity.auth.AuthUserEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface AuthUserDao {

    @Nonnull
    AuthUserEntity create(AuthUserEntity user);
    @Nonnull
    Optional<AuthUserEntity> findById(UUID id);

    void delete(AuthUserEntity user);
    @Nonnull
    AuthUserEntity update(AuthUserEntity user);

    @Nonnull
    List<AuthUserEntity> findAll();
    @Nonnull
    Optional<AuthUserEntity> findByUsername(String username);
}
