package guru.qa.niffler.service;

import guru.qa.niffler.api.UsersApi;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.utils.RandomDataUtils;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UsersApiClient implements UsersClient {

    private static final Config CFG = Config.getInstance();
    private static final CookieManager cm = new CookieManager(null, CookiePolicy.ACCEPT_ALL);

    private final Retrofit userRetrofit = new Retrofit.Builder()
            .baseUrl(CFG.userdataUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final UsersApi usersApi = userRetrofit.create(UsersApi.class);
    AuthApiClient authApiClient = new AuthApiClient();

    @Override
    public UserJson createUser(String username, String password) {
        Response<UserJson> response;
        try {
            Response<Void> authResponse = authApiClient.register(username, password);
            assertEquals(HTTP_OK, authResponse.code());
            response = usersApi.currentUser(username).execute();
            assertEquals(HTTP_OK, response.code());
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        return response.body();
    }

    @Override
    public List<UserJson> createIncomeInvitations(UserJson targetUser, int count) {
        List<UserJson> result = new ArrayList<>();
        try {
            if (count > 0) {
                for (int i = 0; i < count; i++) {
                    UserJson newUser = createUser(RandomDataUtils.randomUsername(), "12345");
                    Response<UserJson> response = usersApi.sendInvitation(targetUser.username(), newUser.username()).execute();
                    assertEquals(HTTP_OK, response.code());
                    result.add(newUser);
                }
            }
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        return result;
    }

    @Override
    public List<UserJson> createOutcomeInvitations(UserJson targetUser, int count) {
        List<UserJson> result = new ArrayList<>();
        try {
            if (count > 0) {
                for (int i = 0; i < count; i++) {
                    UserJson newUser = createUser(RandomDataUtils.randomUsername(), "12345");
                    Response<UserJson> responseSendInvitation = usersApi.sendInvitation(newUser.username(), targetUser.username()).execute();
                    assertEquals(HTTP_OK, responseSendInvitation.code());
                    result.add(targetUser);
                }
            }
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        return result;
    }

    @Override
    public List<UserJson> createFriends(UserJson targetUser, int count) {
        List<UserJson> result = new ArrayList<>();
        try {
            if (count > 0) {
                for (int i = 0; i < count; i++) {
                    UserJson newUser = createUser(RandomDataUtils.randomUsername(), "12345");
                    Response<UserJson> responseSendInvitation = usersApi.sendInvitation(newUser.username(), targetUser.username()).execute();
                    assertEquals(HTTP_OK, responseSendInvitation.code());
                    Response<UserJson> responseAcceptInvitation = usersApi.acceptInvitation(targetUser.username(), newUser.username()).execute();
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

