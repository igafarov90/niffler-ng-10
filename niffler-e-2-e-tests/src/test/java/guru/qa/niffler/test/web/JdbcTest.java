package guru.qa.niffler.test.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.*;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.impl.*;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.jupiter.extension.SpendClientInjector;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendClient;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UserDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

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

    @Test
    void createUserHibernateTest() {
        UserDbClient usersDbClient = new UserDbClient();
        UserJson user = usersDbClient.createUser(
                "RND9", "12345");
        usersDbClient.createOutcomeInvitations(user, 1);
        usersDbClient.createIncomeInvitations(user, 1);
        usersDbClient.createFriends(user, 1);

        ObjectMapper mapper = new ObjectMapper();
        UserEntity u1 = mapper.convertValue(user, UserEntity.class);

        JdbcTransactionTemplate txTemplate = new JdbcTransactionTemplate(CFG.userdataJdbcUrl());
        txTemplate.execute(() -> {
            new UserRepositoryJdbc().remove(u1);
            return null;
        });
    }

    @Test
    void updateSpendJdbcTest() {
        XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
                CFG.spendJdbcUrl()
        );
        xaTransactionTemplate.execute(() -> {
            Optional<SpendEntity> spend = new SpendDaoJdbc().findById(UUID.fromString("1ee2ac3b-41bb-40d1-ab4f-2931b7b11973"));
            Optional<SpendEntity> spend2 = new SpendDaoJdbc().findByUsernameAndSpendDescription("cat", "test");
            SpendEntity sp = spend2.get();
            System.out.println(sp.getId());
            sp.setDescription("descr");
            SpendEntity spend4 = new SpendDaoJdbc().update(sp);
            return null;
        });
    }

    @Test
    void updateSpendSpringJdbcTest() {
        XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
                CFG.spendJdbcUrl()
        );
        xaTransactionTemplate.execute(() -> {
            Optional<SpendEntity> spend = new SpendDaoSpringJdbc().findById(UUID.fromString("1ee2ac3b-41bb-40d1-ab4f-2931b7b11973"));
            Optional<SpendEntity> spend2 = new SpendDaoSpringJdbc().findByUsernameAndSpendDescription("cat", "descr");
            SpendEntity sp = spend2.get();
            sp.setDescription("descr2");
            new SpendDaoSpringJdbc().update(sp);
            return null;
        });
    }

    @Test
    void updateSpendRepositoryJdbcTest() {
        XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
                CFG.spendJdbcUrl()
        );
        xaTransactionTemplate.execute(() -> {
            new SpendRepositoryJdbc().findById(UUID.fromString("1ee2ac3b-41bb-40d1-ab4f-2931b7b11973"));
            Optional<SpendEntity> spend2 = new SpendRepositoryJdbc().findByUsernameAndSpendDescription("cat", "descr2");
            SpendEntity sp = spend2.get();
            sp.setDescription("descr3");
            new SpendRepositoryJdbc().update(sp);
            return null;
        });
    }

    @Test
    void updateSpendRepositorySpringJdbcTest() {
        XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
                CFG.spendJdbcUrl()
        );
        xaTransactionTemplate.execute(() -> {
            new SpendRepositorySpringJdbc().findById(UUID.fromString("1ee2ac3b-41bb-40d1-ab4f-2931b7b11973"));
            Optional<SpendEntity> spend2 = new SpendRepositorySpringJdbc().findByUsernameAndSpendDescription("cat", "descr3");
            SpendEntity sp = spend2.get();
            sp.setDescription("descr4");
            new SpendRepositorySpringJdbc().update(sp);
            return null;
        });
    }

    @Test
    void updateSpendRepositoryHibernateTest() {
        XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
                CFG.spendJdbcUrl()
        );
        xaTransactionTemplate.execute(() -> {
            new SpendRepositoryHibernate().findById(UUID.fromString("1ee2ac3b-41bb-40d1-ab4f-2931b7b11973"));
            Optional<SpendEntity> spend2 = new SpendRepositoryHibernate().findByUsernameAndSpendDescription("cat", "descr4");
            SpendEntity sp = spend2.get();
            sp.setDescription("descr5");
            new SpendRepositoryHibernate().update(sp);
            return null;
        });
    }

    @Test
    void createSpendBySpendClientTest() {
        SpendClient spendClient = new SpendDbClient();
        SpendJson spendJson = spendClient.create(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                RandomDataUtils.randomCategoryName(),
                                "duck",
                                false
                        ),
                        CurrencyValues.RUB,
                        1000.0,
                        "cat-name-tx",
                        "duck"
                )
        );
        System.out.println(spendJson);
    }

    @Test
    void updateSpendBySpendClientTest() {
        SpendClient spendClient = new SpendDbClient();
        SpendJson spendJson = spendClient.update(
                new SpendJson(
                        UUID.fromString("1ee2ac3b-41bb-40d1-ab4f-2931b7b11973"),
                        new Date(),
                        new CategoryJson(
                                UUID.fromString("83a7ad98-0af9-4e13-9849-5cecd399db0a"),
                                RandomDataUtils.randomCategoryName(),
                                "duck",
                                false
                        ),
                        CurrencyValues.RUB,
                        1000.0,
                        "cat-name-tx",
                        "duck"
                )
        );
        System.out.println(spendJson);
    }

    @Test
    void removeCategoryTest() {
        SpendClient spendClient = new SpendDbClient();
        CategoryJson toCreate = new CategoryJson(
                null,
                "Direct Test18",
                "duck",
                false
        );
        CategoryJson createdJson = spendClient.createCategory(toCreate);
        spendClient.removeCategory(createdJson);
    }

    @Test
    void removeSpendTest() {
        SpendClient spendClient = new SpendDbClient();
        SpendJson toCreate = new SpendJson(
                null,
                new Date(),
                new CategoryJson(
                        null,
                        RandomDataUtils.randomCategoryName(),
                        "duck",
                        false
                ),
                CurrencyValues.RUB,
                1000.0,
                RandomDataUtils.randomSentence(3),
                "duck"
        );
        SpendJson createdSpend = spendClient.create(toCreate);

        SpendJson toUpdate = new SpendJson(
                createdSpend.id(),
                createdSpend.spendDate(),
                createdSpend.category(),
                createdSpend.currency(),
                2000.0,
                RandomDataUtils.randomSentence(1),
                createdSpend.username()
        );
        spendClient.update(toUpdate);
    }

    @Test
    void updateSpendToArchiveTest() {
        SpendClient spendClient = new SpendDbClient();
        SpendJson toCreate = new SpendJson(
                null,
                new Date(),
                new CategoryJson(
                        null,
                        RandomDataUtils.randomCategoryName(),
                        "duck",
                        false
                ),
                CurrencyValues.RUB,
                1000.0,
                RandomDataUtils.randomSentence(3),
                "duck"
        );
        SpendJson createdSpend = spendClient.create(toCreate);
        CategoryJson createdCategory = createdSpend.category();
        spendClient.remove(createdSpend);
        spendClient.removeCategory(createdCategory);
    }

    SpendClient spendClient;

    @ExtendWith(SpendClientInjector.class)
    @Test
    void createSpendWithInjectorTest() {
        SpendJson spendJson = spendClient.create(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                RandomDataUtils.randomCategoryName(),
                                "duck",
                                false
                        ),
                        CurrencyValues.RUB,
                        1000.0,
                        "cat-name-tx",
                        "duck"
                )
        );
        System.out.println(spendJson);
    }

}







