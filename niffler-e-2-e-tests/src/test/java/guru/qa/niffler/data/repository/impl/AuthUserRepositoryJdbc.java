package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class AuthUserRepositoryJdbc implements AuthUserRepository {

    private static final AuthUserDao authUserDao = new AuthUserDaoJdbc();
    private static final AuthAuthorityDao authorityDao = new AuthAuthorityDaoJdbc();

    @Nonnull
    @Override
    public AuthUserEntity create(AuthUserEntity user) {
        AuthUserEntity authUser = authUserDao.create(user);
        if (user.getAuthorities() != null) {
            for (AuthorityEntity authority : user.getAuthorities()) {
                authorityDao.create(authority);
            }
        }
        return authUser;
    }

    @Nonnull
    @Override
    public AuthUserEntity update(AuthUserEntity user) {
        return authUserDao.update(user);
    }

    @Nonnull
    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        return authUserDao.findById(id).map(userEntity -> {
            userEntity.setAuthorities(authorityDao.findByUserId((id)));
            return userEntity;
        });
    }

    @Nonnull
    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        return authUserDao.findByUsername(username).map(userEntity -> {
            userEntity.setAuthorities(authorityDao.findByUserId((userEntity.getId())));
            return userEntity;
        });
    }

    @Override
    public void remove(AuthUserEntity user) {
        if (user.getAuthorities() != null) {
            for (AuthorityEntity authority : user.getAuthorities()) {
                authorityDao.deleteByUserId(authority);
            }
            authUserDao.delete(user);
        }
    }
}
