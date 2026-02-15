package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserApiClient;
import guru.qa.niffler.service.UserClient;
import org.junit.jupiter.api.Test;

public class UserTest {

    private final UserClient userClient = new UserApiClient();

    @User
    @Test
    public void shouldCreateUser(UserJson user) {
        userClient.createUser(user.username(), user.testData().password());
    }
}
