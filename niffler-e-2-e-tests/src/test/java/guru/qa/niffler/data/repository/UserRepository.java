package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.impl.UserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.UserRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.UserRepositorySpringJdbc;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface UserRepository {

    @Nonnull
    static UserRepository getInstance() {
        return switch (System.getProperty("repository", "jpa")) {
            case "jpa" -> new UserRepositoryHibernate();
            case "jdbc" -> new UserRepositoryJdbc();
            case "sjdbc" -> new UserRepositorySpringJdbc();
            default -> throw new IllegalStateException(
                    "Unknown repository: " + System.getProperty("repository"));
        };
    }

    @Nonnull
    UserEntity create(UserEntity user);
    @Nonnull
    Optional<UserEntity> findById(UUID id);
    @Nonnull
    Optional<UserEntity> findByUsername(String username);

    @Nonnull
    UserEntity update(UserEntity user);

    void sendInvitation(UserEntity requester, UserEntity addressee);

    void addFriend(UserEntity requester, UserEntity addressee);

    void remove(UserEntity user);
}
