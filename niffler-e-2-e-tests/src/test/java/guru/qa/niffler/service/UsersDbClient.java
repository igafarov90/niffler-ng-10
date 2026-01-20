package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.UserRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.utils.RandomDataUtils;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;


public class UsersDbClient implements UsersClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final UserRepository userRepository = new UserRepositoryHibernate();

    private final AuthUserRepository authUserRepository = new AuthUserRepositoryHibernate();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    @Override
    public UserJson createUser(String username, String password) {
        return xaTransactionTemplate.execute(() -> {
                    AuthUserEntity authUser = authUserEntity(username, password);
                    authUserRepository.create(authUser);
                    return UserJson.fromEntity(
                            userRepository.create(userEntity(username)));
                }
        );
    }

    public static UserEntity userEntity(String username) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setCurrency(CurrencyValues.RUB);
        return userEntity;
    }

    private static AuthUserEntity authUserEntity(String username, String password) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(username);
        authUser.setPassword(pe.encode(password));
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
        return authUser;
    }

    @Override
    public void createIncomeInvitations(UserJson targetUser, int count) {
        if (count > 0) {
            UserEntity targetEntity = userRepository.findById(
                    targetUser.id()
            ).orElseThrow();
            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                            String username = RandomDataUtils.randomUsername();
                            AuthUserEntity authUser = authUserEntity(username, pe.encode("12345"));
                            authUserRepository.create(authUser);
                            UserEntity addressee = userRepository.create(userEntity(username));
                            userRepository.sendInvitation(targetEntity, addressee);
                            return null;
                        }
                );
            }
        }
    }

    @Override
    public void createOutcomeInvitations(UserJson targetUser, int count) {
        if (count > 0) {
            UserEntity targetEntity = userRepository.findById(
                    targetUser.id()
            ).orElseThrow();
            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                            String username = RandomDataUtils.randomUsername();
                            AuthUserEntity authUser = authUserEntity(username, pe.encode("12345"));
                            authUserRepository.create(authUser);
                            UserEntity requester = userRepository.create(userEntity(username));
                            userRepository.sendInvitation(requester,targetEntity);
                            return null;
                        }
                );
            }
        }
    }

    @Override
    public void createFriends(UserJson targetUser, int count) {
        if (count > 0) {
            UserEntity targetEntity = userRepository.findById(
                    targetUser.id()
            ).orElseThrow();

            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                            String username = RandomDataUtils.randomUsername();
                            AuthUserEntity authUser = authUserEntity(username, pe.encode("12345"));
                            authUserRepository.create(authUser);
                            UserEntity addressee = userRepository.create(userEntity(username));
                            userRepository.addFriend(targetEntity, addressee);
                            return null;
                        }
                );
            }
        }
    }
}
