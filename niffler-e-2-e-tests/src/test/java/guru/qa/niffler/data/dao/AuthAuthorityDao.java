package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.AuthorityEntity;

import java.util.List;
import java.util.UUID;

public interface AuthAuthorityDao {
    AuthorityEntity create(AuthorityEntity authority);

    List<AuthorityEntity> findByUserId(UUID id);

    void deleteByUserId(AuthorityEntity authority);
}
