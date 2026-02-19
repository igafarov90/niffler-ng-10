package guru.qa.niffler.test.rest;

import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserApiClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.parallel.Isolated;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Execution(ExecutionMode.SAME_THREAD)
@Isolated
public class LastTests {

    private final UserApiClient userApiClient = new UserApiClient();

    @Test
    void shouldReturnNonEmptyUsersList() {
        final List<UserJson> response = userApiClient.getAllUsers("");
        assertThat(response).isNotEmpty();
    }
}
