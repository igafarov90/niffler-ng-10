package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.utils.RandomDataUtils;
import io.qameta.allure.Step;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@ParametersAreNonnullByDefault
public final class UserDbClient implements UserClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    private static final String DEFAULT_PASSWORD = "12345";

    private final UserRepository userRepository = UserRepository.getInstance();

    private final AuthUserRepository authUserRepository = AuthUserRepository.getInstance();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    @Nonnull
    @Override
    @Step("Создать нового пользователя с именем: {username}")
    public UserJson createUser(String username, String password) {
        return Objects.requireNonNull(xaTransactionTemplate.execute(() -> {
                    AuthUserEntity authUser = authUserEntity(username, password);
                    authUserRepository.create(authUser);
                    return UserJson.fromEntity(
                            userRepository.create(userEntity(username)));
                }
        ));
    }

    @Nonnull
    @Override
    @Step("Создать {count} входящих приглашений для пользователя: {targetUser.username}")
    public List<UserJson> createIncomeInvitations(UserJson targetUser, int count) {
        final List<UserJson> result = new ArrayList<>();
        if (count > 0) {
            UserEntity targetEntity = userRepository.findById(
                    targetUser.id()
            ).orElseThrow();
            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                            String username = RandomDataUtils.randomUsername();
                            AuthUserEntity authUser = authUserEntity(username, pe.encode(DEFAULT_PASSWORD));
                            authUserRepository.create(authUser);
                            UserEntity requester = userRepository.create(userEntity(username));
                            userRepository.sendInvitation(targetEntity, requester);
                            result.add(UserJson.fromEntity(requester));
                            result.add(UserJson.fromEntity(requester));
                            return null;
                        }
                );
            }
        }
        return result;
    }

    @Nonnull
    @Override
    @Step("Создать {count} исходящих приглашений от пользователя: {targetUser.username}")
    public List<UserJson> createOutcomeInvitations(UserJson targetUser, int count) {
        final List<UserJson> result = new ArrayList<>();
        if (count > 0) {
            UserEntity targetEntity = userRepository.findById(
                    targetUser.id()
            ).orElseThrow();

            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                            String username = RandomDataUtils.randomUsername();
                            AuthUserEntity authUser = authUserEntity(username, pe.encode(DEFAULT_PASSWORD));
                            authUserRepository.create(authUser);
                            UserEntity addressee = userRepository.create(userEntity(username));
                            userRepository.sendInvitation(addressee, targetEntity);
                            result.add(UserJson.fromEntity(addressee));
                            return null;
                        }
                );
            }
        }
        return result;
    }

    @Nonnull
    @Override
    public List<UserJson> createFriends(UserJson targetUser, int count) {
        final List<UserJson> result = new ArrayList<>();
        if (count > 0) {
            UserEntity targetEntity = userRepository.findById(
                    targetUser.id()
            ).orElseThrow();

            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                            String username = RandomDataUtils.randomUsername();
                            AuthUserEntity authUser = authUserEntity(username, pe.encode(DEFAULT_PASSWORD));
                            authUserRepository.create(authUser);
                            UserEntity addressee = userRepository.create(userEntity(username));
                            userRepository.addFriend(targetEntity, addressee);
                            result.add(UserJson.fromEntity(addressee));
                            return null;
                        }
                );
            }
        }
        return result;
    }

    @Nonnull
    private static UserEntity userEntity(String username) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setCurrency(CurrencyValues.RUB);
        return userEntity;
    }

    @Nonnull
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
}
