package guru.qa.niffler.service;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.GhApi;
import io.qameta.allure.Step;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

import static org.apache.hc.core5.http.HttpStatus.SC_OK;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public final class GhApiClient extends RestClient {

    private static final String GH_TOKEN_ENV = "GITHUB_TOKEN";

    private final GhApi ghApi;

    public GhApiClient() {
        super(CFG.githubUrl());
        this.ghApi = create(GhApi.class);
    }

    @Step("Получить статус issue #{issueNumber} на GitHub")
    public @Nonnull String issueState(@Nonnull String issueNumber) {
        final Response<JsonNode> response;
        try {
            response = ghApi.issue(
                    "Bearer " + System.getenv(GH_TOKEN_ENV),
                    issueNumber
            ).execute();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
        return Objects.requireNonNull(response.body()).get("state").asText();
    }
}
