package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.*;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.Connection;
import java.sql.SQLException;

public class ChainedTransactionTest {

    private static final Config CFG = Config.getInstance();

    @Test
    public void successTransactionJdbcTest() {
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

        DataSourceTransactionManager authUserManager = new DataSourceTransactionManager(
                DataSources.dataSource(CFG.authJdbcUrl())
        );

        DataSourceTransactionManager userDataManager = new DataSourceTransactionManager(
                DataSources.dataSource(CFG.userdataJdbcUrl())
        );

        ChainedTransactionManager chainedTxManager = new ChainedTransactionManager(authUserManager, userDataManager);
        TransactionTemplate txTemplate = new TransactionTemplate(chainedTxManager);

        txTemplate.execute(status -> {
            try (Connection authUserConn = authUserManager.getDataSource().getConnection();
                 Connection userDataConn = userDataManager.getDataSource().getConnection()) {
                new AuthUserDaoJdbc().create(authUser);
                new UserDaoJdbc().create(userData);
                authority.setUser(authUser);
                new AuthAuthorityDaoJdbc().create(authority);
                return null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void shouldFailedTransactionSpringJdbcTest() {
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

        DataSourceTransactionManager authUserManager = new DataSourceTransactionManager(
                DataSources.dataSource(CFG.authJdbcUrl())
        );

        DataSourceTransactionManager userDataManager = new DataSourceTransactionManager(
                DataSources.dataSource(CFG.userdataJdbcUrl())
        );

        ChainedTransactionManager chainedTxManager = new ChainedTransactionManager(authUserManager, userDataManager);
        TransactionTemplate txTemplate = new TransactionTemplate(chainedTxManager);


        txTemplate.execute(status -> {
            try (Connection authUserConn = authUserManager.getDataSource().getConnection();
                 Connection userDataConn = userDataManager.getDataSource().getConnection()) {
                new AuthUserDaoSpringJdbc().create(authUser);
                new UserDaoSpringJdbc().create(userData);
                authority.setUser(null);
                new AuthAuthorityDaoSpringJdbc().create(authority);
                return null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
