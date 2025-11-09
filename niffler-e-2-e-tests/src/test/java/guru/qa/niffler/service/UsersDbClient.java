package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.*;
import guru.qa.niffler.data.entity.AuthUserEntity;
import guru.qa.niffler.data.entity.Authority;
import guru.qa.niffler.data.entity.AuthorityEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.model.UserJson;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

import static guru.qa.niffler.data.Databases.*;


public class UsersDbClient implements UsersClient {
    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public UserJson createUserSpringJdbc(UserJson user) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(user.username());
        authUser.setPassword(pe.encode("12345"));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);

        AuthUserEntity createdAuthUser = new AuthUserDaoSpringJdbc(dataSource(CFG.authJdbcUrl()))
                .create(authUser);
        AuthorityEntity[] userAuthorities = Arrays.stream(Authority.values()).map(
                e -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setUser(createdAuthUser);
                    ae.setAuthority(e);
                    return ae;
                }
        ).toArray(AuthorityEntity[]::new);

        new AuthAuthorityDaoSpringJdbc(dataSource(CFG.authJdbcUrl()))
                .create(userAuthorities);

        return UserJson.fromEntity(new UserDaoSpringJdbc(dataSource(CFG.userdataJdbcUrl()))
                .create(
                        UserEntity.fromJson(user)
                ));
    }


    @Override
    public UserJson createUser(UserJson user) {
        return UserJson.fromEntity(
                xaTransaction(
                        new XaFunction<>(
                                con -> {
                                    AuthUserEntity authUser = new AuthUserEntity();
                                    authUser.setUsername(user.username());
                                    authUser.setPassword("12345");
                                    authUser.setEnabled(true);
                                    authUser.setAccountNonExpired(true);
                                    authUser.setAccountNonLocked(true);
                                    authUser.setCredentialsNonExpired(true);
                                    new AuthUserDaoJdbc(con).create(authUser);
                                    new AuthAuthorityDaoJdbc(con).create(
                                            Arrays.stream(Authority.values())
                                                    .map(a -> {
                                                        AuthorityEntity ae = new AuthorityEntity();
                                                        ae.setUser(authUser);
                                                        ae.setAuthority(a);
                                                        return ae;
                                                    }).toArray(AuthorityEntity[]::new)
                                    );
                                    return null;
                                },
                                CFG.authJdbcUrl()
                        ),
                        new XaFunction<>(
                                con -> {
                                    UserEntity ue = new UserEntity();
                                    ue.setUsername(user.username());
                                    ue.setFullname(user.fullname());
                                    ue.setCurrency(user.currency());
                                    return new UserDaoJdbc(con).create(ue);
                                },
                                CFG.userdataJdbcUrl()
                        )
                )
        );
    }
}
