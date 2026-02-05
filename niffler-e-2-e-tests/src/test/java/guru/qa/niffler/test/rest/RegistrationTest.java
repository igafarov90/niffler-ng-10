package guru.qa.niffler.test.rest;

import guru.qa.niffler.service.AuthApiClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import java.io.IOException;

import static org.apache.hc.core5.http.HttpStatus.SC_CREATED;

public class RegistrationTest {

    private final AuthApiClient authApiClient = new AuthApiClient();
    final String randomUsername = RandomDataUtils.randomName();

    @Test
    void newUserShouldRegisteredByApiCall() throws IOException {
        final Response<Void> response = authApiClient.register(randomUsername, "12345");
        Assertions.assertEquals(SC_CREATED, response.code());
    }
}
