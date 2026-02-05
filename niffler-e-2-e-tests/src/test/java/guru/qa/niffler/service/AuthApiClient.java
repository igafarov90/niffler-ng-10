package guru.qa.niffler.service;

import guru.qa.niffler.api.AuthApi;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import io.qameta.allure.Step;
import retrofit2.Response;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;

@ParametersAreNonnullByDefault
public final class AuthApiClient extends RestClient {

    private final AuthApi authApi;

    public AuthApiClient() {
        super(CFG.authUrl());
        this.authApi = create(AuthApi.class);
    }

    @Step("Регистрация пользователя: {username}")
    public Response<Void> register(String username, String password) throws IOException {
        authApi.requestRegisterForm().execute();
        return authApi.register(
                username,
                password,
                password,
                ThreadSafeCookieStore.INSTANCE.xsrfCookie()
        ).execute();
    }
}
