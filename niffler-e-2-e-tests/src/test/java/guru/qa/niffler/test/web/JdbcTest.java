package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.entity.AuthUserEntity;
import guru.qa.niffler.data.entity.Authority;
import guru.qa.niffler.data.entity.AuthorityEntity;
import guru.qa.niffler.service.SpendDbClient;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static guru.qa.niffler.data.Databases.xaTransaction;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class JdbcTest {
    SpendDbClient spendDbClient = new SpendDbClient();

    private static final Config CFG = Config.getInstance();

    @Test
    void userWithAuthoritiesFullCrudTest() {
        AuthUserEntity authUserEntity = new AuthUserEntity();
        authUserEntity.setUsername("test-true");
        authUserEntity.setPassword("1258");
        authUserEntity.setEnabled(true);
        authUserEntity.setAccountNonExpired(true);
        authUserEntity.setAccountNonLocked(true);
        authUserEntity.setCredentialsNonExpired(true);

        AuthorityEntity reader = new AuthorityEntity();
        reader.setAuthority(Authority.read);

        AuthorityEntity writer = new AuthorityEntity();
        writer.setAuthority(Authority.write);

        xaTransaction(new Databases.XaFunction<>(connection -> {
                    AuthUserEntity created = new AuthUserDaoJdbc(connection).create(authUserEntity);
                    authUserEntity.setId(created.getId());
                    return created;
                },
                        CFG.authJdbcUrl()),

                new Databases.XaFunction<>(connection -> {
                    authUserEntity.setUsername("test-true-updated");
                    return new AuthUserDaoJdbc(connection).update(authUserEntity);
                },
                        CFG.authJdbcUrl()),

                new Databases.XaFunction<>(connection -> {
                    reader.setUser(authUserEntity);
                    new AuthAuthorityDaoJdbc(connection).create(reader);

                    writer.setUser(authUserEntity);
                    new AuthAuthorityDaoJdbc(connection).create(writer);

                    return null;
                },
                        CFG.authJdbcUrl()),

                new Databases.XaFunction<>(connection -> {
                    reader.setUser(authUserEntity);
                    new AuthAuthorityDaoJdbc(connection).deleteByUserId(reader);
                    return null;
                }, CFG.authJdbcUrl()),

                new Databases.XaFunction<>(connection -> {
                    new AuthUserDaoJdbc(connection).delete(authUserEntity);
                    return null;
                },
                        CFG.authJdbcUrl()),

                new Databases.XaFunction<>(connection -> {
                    Optional<AuthUserEntity> foundUser =
                            new AuthUserDaoJdbc(connection).findById(authUserEntity.getId());
                    assertFalse(foundUser.isPresent());
                    return null;
                },
                        CFG.authJdbcUrl())
        );
    }

    @Test
    void shouldRollbackWhenDeletingUserWithExistingAuthorities() {
        AuthUserEntity authUserEntity = new AuthUserEntity();
        authUserEntity.setUsername("test-true");
        authUserEntity.setPassword("1258");
        authUserEntity.setEnabled(true);
        authUserEntity.setAccountNonExpired(true);
        authUserEntity.setAccountNonLocked(true);
        authUserEntity.setCredentialsNonExpired(true);

        AuthorityEntity reader = new AuthorityEntity();
        reader.setAuthority(Authority.read);

        xaTransaction(new Databases.XaFunction<>(connection -> {
                    AuthUserEntity created = new AuthUserDaoJdbc(connection).create(authUserEntity);
                    authUserEntity.setId(created.getId());
                    return created;
                },
                        CFG.authJdbcUrl()),

                new Databases.XaFunction<>(connection -> {
                    reader.setUser(authUserEntity);
                    new AuthAuthorityDaoJdbc(connection).create(reader);

                    return null;
                },
                        CFG.authJdbcUrl()),

                new Databases.XaFunction<>(connection -> {
                    new AuthUserDaoJdbc(connection).delete(authUserEntity);
                    return null;
                },
                        CFG.authJdbcUrl())
        );
    }
}




