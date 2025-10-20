package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserDao {
    UserEntity createUser(UserEntity user);

    Optional<UserEntity> findUserByUsername(String username);

    Optional<UserEntity> findUserById(UUID id);

    void deleteUser(UserEntity user);
}
