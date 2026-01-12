package guru.qa.niffler.test.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.FriendshipDao;
import guru.qa.niffler.data.dao.impl.*;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositorySpringJdbc;
import guru.qa.niffler.data.repository.impl.UserRepositoryJdbc;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JdbcTest {

    private static final Config CFG = Config.getInstance();

    @Test
    void jdbcWithoutTransactionTest() {
        String username = RandomDataUtils.randomUsername();
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(username);
        authUser.setPassword("1258");
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);

        AuthorityEntity authority = new AuthorityEntity();
        authority.setAuthority(Authority.write);

        UserEntity userData = new UserEntity();
        userData.setFullname(null);
        userData.setFirstname(null);
        userData.setSurname(null);
        userData.setUsername(authUser.getUsername());
        userData.setCurrency(CurrencyValues.RUB);

        try (Connection authUserConn = DataSources.dataSource(CFG.authJdbcUrl()).getConnection();
             Connection userDataConn = DataSources.dataSource(CFG.userdataJdbcUrl()).getConnection()) {
            authUser = new AuthUserDaoJdbc().create(authUser);
            userData = new UserDaoJdbc().create(userData);
            authority.setUser(authUser);
            new AuthAuthorityDaoJdbc().create(authority);

            Optional<AuthUserEntity> foundAuthUser = new AuthUserDaoJdbc().findById(authUser.getId());
            assertTrue(foundAuthUser.isPresent());
            assertEquals(username, foundAuthUser.get().getUsername());

            Optional<UserEntity> foundUserData = new UserDaoJdbc().findById(userData.getId());
            assertTrue(foundUserData.isPresent());
            assertEquals(username, foundUserData.get().getUsername());
        } catch (SQLException ex) {
            throw new RuntimeException();
        }
    }

    @Test
    void jdbcWithTransactionTest() {
        String username = RandomDataUtils.randomUsername();
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(username);
        authUser.setPassword("1258");
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);

        AuthorityEntity authority = new AuthorityEntity();
        authority.setAuthority(Authority.write);

        UserEntity userData = new UserEntity();
        userData.setFullname(null);
        userData.setFirstname(null);
        userData.setSurname(null);
        userData.setUsername(authUser.getUsername());
        userData.setCurrency(CurrencyValues.RUB);

        XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
                CFG.authJdbcUrl(),
                CFG.userdataJdbcUrl()
        );

        xaTransactionTemplate.execute(() -> {
            new AuthUserDaoJdbc().create(authUser);
            new UserDaoJdbc().create(userData);
            authority.setUser(authUser);
            new AuthAuthorityDaoJdbc().create(authority);
            return null;
        });

        Optional<AuthUserEntity> foundAuthUser = new AuthUserDaoSpringJdbc().findById(authUser.getId());
        assertTrue(foundAuthUser.isPresent());
        assertEquals(username, foundAuthUser.get().getUsername());

        Optional<UserEntity> foundUserData = new UserDaoSpringJdbc().findById(userData.getId());
        assertTrue(foundUserData.isPresent());
        assertEquals(username, foundUserData.get().getUsername());
    }

    @Test
    void springJdbcWithTransactionTest() {
        String username = RandomDataUtils.randomUsername();
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(username);
        authUser.setPassword("1258");
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);

        AuthorityEntity authority = new AuthorityEntity();
        authority.setAuthority(Authority.write);

        UserEntity userData = new UserEntity();
        userData.setFullname(null);
        userData.setFirstname(null);
        userData.setSurname(null);
        userData.setUsername(authUser.getUsername());
        userData.setCurrency(CurrencyValues.RUB);

        XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
                CFG.authJdbcUrl(),
                CFG.userdataJdbcUrl()
        );
        xaTransactionTemplate.execute(() -> {
            new AuthUserDaoSpringJdbc().create(authUser);
            new UserDaoSpringJdbc().create(userData);
            authority.setUser(authUser);
            new AuthAuthorityDaoSpringJdbc().create(authority);
            return null;
        });

        Optional<AuthUserEntity> foundAuthUser = new AuthUserDaoSpringJdbc().findById(authUser.getId());
        assertTrue(foundAuthUser.isPresent());
        assertEquals(username, foundAuthUser.get().getUsername());

        Optional<UserEntity> foundUserData = new UserDaoSpringJdbc().findById(userData.getId());
        assertTrue(foundUserData.isPresent());
        assertEquals(username, foundUserData.get().getUsername());
    }

    @Test
    void springJdbcWithoutTransactionTest() {
        String username = RandomDataUtils.randomUsername();
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(username);
        authUser.setPassword("1258");
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);

        AuthorityEntity authority = new AuthorityEntity();
        authority.setAuthority(Authority.write);

        UserEntity userData = new UserEntity();
        userData.setFullname(null);
        userData.setFirstname(null);
        userData.setSurname(null);
        userData.setUsername(authUser.getUsername());
        userData.setCurrency(CurrencyValues.RUB);

        new AuthUserDaoSpringJdbc().create(authUser);
        new UserDaoSpringJdbc().create(userData);
        authority.setUser(authUser);
        new AuthAuthorityDaoSpringJdbc().create(authority);

        Optional<AuthUserEntity> foundAuthUser = new AuthUserDaoSpringJdbc().findById(authUser.getId());
        assertTrue(foundAuthUser.isPresent());
        assertEquals(username, foundAuthUser.get().getUsername());

        Optional<UserEntity> foundUserData = new UserDaoSpringJdbc().findById(userData.getId());
        assertTrue(foundUserData.isPresent());
        assertEquals(username, foundUserData.get().getUsername());
    }

    @Test
    void createUserSpringJdbcTest() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUser(
                new UserJson(
                        null,
                        "valentin-119",
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        "null",
                        "null"
                )
        );
    }

    @Test
    void createUserJdbcTest() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUser(
                new UserJson(
                        null,
                        "valentin-61",
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        "null",
                        "null"
                )
        );
    }

    @Test
    void createFriendshipJdbcRepositoryTest() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user1 = usersDbClient.createUser(
                new UserJson(
                        null,
                        RandomDataUtils.randomName(),
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        "null",
                        "null"
                )
        );
        UserJson user2 = usersDbClient.createUser(
                new UserJson(
                        null,
                        RandomDataUtils.randomName(),
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        "null",
                        "null"
                )
        );
        ObjectMapper mapper = new ObjectMapper();
        UserEntity u1 = mapper.convertValue(user1, UserEntity.class);
        UserEntity u2 = mapper.convertValue(user2, UserEntity.class);

        UserRepository ur = new UserRepositoryJdbc();
        ur.addFriend(u1, u2);

        FriendshipDao fr = new FriendshipDaoJdbc();
        Optional<FriendshipEntity> friendship = fr.findById(user1.id(), user2.id());
        assertTrue(friendship.isPresent());
        assertEquals(friendship.get().getRequester(), u1);
        assertEquals(friendship.get().getAddressee(), u2);
    }

    @Test
    void testCreateUserRepositorySpringJdbcTest() {
        JdbcTransactionTemplate txTemplate = new JdbcTransactionTemplate(CFG.userdataJdbcUrl());
        AuthUserEntity user = txTemplate.execute(() -> {
            AuthUserEntity authUser = new AuthUserEntity();
            authUser.setUsername(RandomDataUtils.randomUsername());
            authUser.setPassword("1234");
            authUser.setEnabled(true);
            authUser.setAccountNonExpired(true);
            authUser.setAccountNonLocked(true);
            authUser.setCredentialsNonExpired(true);
            authUser.setAuthorities(
                    Arrays.stream(Authority.values()).map(
                            e -> {
                                AuthorityEntity ae = new AuthorityEntity();
                                ae.setUser(authUser);
                                ae.setAuthority(e);
                                return ae;
                            }
                    ).toList()
            );
            return new AuthUserRepositorySpringJdbc().create(authUser);
        });

        Optional<AuthUserEntity> foundUser = txTemplate.execute(() ->
                new AuthUserRepositorySpringJdbc().findById(user.getId()));

        assertTrue(foundUser.isPresent());
        assertEquals(user.getUsername(), foundUser.get().getUsername());
        assertEquals(2, foundUser.get().getAuthorities().size());
        assertTrue(foundUser.get().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority() == Authority.read));
        assertTrue(foundUser.get().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority() == Authority.write));
    }
}







