package guru.qa.niffler.service;

import com.google.common.base.Stopwatch;
import guru.qa.niffler.api.UserApi;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.utils.RandomDataUtils;
import io.qameta.allure.Step;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public final class UserApiClient extends RestClient implements UserClient {

    private final UserApi userApi;
    private final AuthApiClient authApiClient;

    public UserApiClient() {
        super(CFG.userdataUrl());
        this.userApi = create(UserApi.class);
        this.authApiClient = new AuthApiClient();
    }

    @Nonnull
    @Override
    @Step("Создать нового пользователя с именем: {username}")
    public UserJson createUser(String username, String password) {
        try {
            authApiClient.register(username, password);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        Stopwatch sw = Stopwatch.createStarted();
        int maxWaitTime = 6000;

        Response<UserJson> response;

        while (sw.elapsed(TimeUnit.MILLISECONDS) < maxWaitTime) {
            try {
                response = userApi.currentUser(username).execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            UserJson user = response.body();
            if (user != null && user.id() != null) {
                return user;
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        throw new RuntimeException(
                String.format("Пользователь '%s' не был создан за %d мс", username, maxWaitTime)
        );
    }

    @Nonnull
    @Override
    @Step("POST /internal/invitations/send - Создать входящие приглашения для {targetUser.username}, количество: {count})")
    public List<UserJson> createIncomeInvitations(UserJson targetUser, int count) {
        List<UserJson> result = new ArrayList<>();
        try {
            if (count > 0) {
                for (int i = 0; i < count; i++) {
                    UserJson newUser = createUser(RandomDataUtils.randomUsername(), "12345");
                    Response<UserJson> response = userApi.sendInvitation(targetUser.username(), newUser.username()).execute();
                    assertEquals(HTTP_OK, response.code());
                    result.add(newUser);
                }
            }
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        return result;
    }

    @Nonnull
    @Override
    @Step("POST /internal/invitations/send - Отправить приглашение для {targetUser.username}, количество: {count}")
    public List<UserJson> createOutcomeInvitations(UserJson targetUser, int count) {
        List<UserJson> result = new ArrayList<>();
        try {
            if (count > 0) {
                for (int i = 0; i < count; i++) {
                    UserJson newUser = createUser(RandomDataUtils.randomUsername(), "12345");
                    Response<UserJson> responseSendInvitation = userApi.sendInvitation(newUser.username(), targetUser.username()).execute();
                    assertEquals(HTTP_OK, responseSendInvitation.code());
                    result.add(targetUser);
                }
            }
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        return result;
    }

    @Nonnull
    @Override
    @Step("Создать друзей для {targetUser.username}, количество: {count}")
    public List<UserJson> createFriends(UserJson targetUser, int count) {
        List<UserJson> result = new ArrayList<>();
        try {
            if (count > 0) {
                for (int i = 0; i < count; i++) {
                    UserJson newUser = createUser(RandomDataUtils.randomUsername(), "12345");
                    Response<UserJson> responseSendInvitation = userApi.sendInvitation(newUser.username(), targetUser.username()).execute();
                    assertEquals(HTTP_OK, responseSendInvitation.code());
                    Response<UserJson> responseAcceptInvitation = userApi.acceptInvitation(targetUser.username(), newUser.username()).execute();
                    assertEquals(HTTP_OK, responseAcceptInvitation.code());
                    result.add(newUser);
                }
            }
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        return result;
    }
}

