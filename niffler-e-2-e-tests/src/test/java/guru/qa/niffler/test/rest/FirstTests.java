package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserApiClient;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Order(1)
public class FirstTests {

    private final UserApiClient userApiClient = new UserApiClient();

    @Test
    void shouldReturnEmptyUsersList(){
        final List<UserJson> response = userApiClient.getAllUsers("");
        assertEquals(0, response.size());
    }
}
