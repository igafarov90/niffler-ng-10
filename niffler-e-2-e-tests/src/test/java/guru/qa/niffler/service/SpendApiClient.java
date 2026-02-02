package guru.qa.niffler.service;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import io.qameta.allure.Step;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.*;

import static org.apache.hc.core5.http.HttpStatus.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class SpendApiClient implements SpendClient {

    private static final Config CFG = Config.getInstance();

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(CFG.spendUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final SpendApi spendApi = retrofit.create(SpendApi.class);

    @Nonnull
    @Step("GET /internal/spends/{id} - Получить трату по ID: {id}")
    public SpendJson getSpend(String id) {
        final Response<SpendJson> response;
        try {
            response = spendApi.getSpend(id)
                    .execute();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
        return Objects.requireNonNull(response.body());
    }

    @Nonnull
    @Step("GET /internal/spends/all - Получить траты пользователя: {username}")
    public List<SpendJson> getAllSpends(String username,
                                        @Nullable CurrencyValues currency,
                                        @Nullable String from,
                                        @Nullable String to) {
        final Response<List<SpendJson>> response;
        try {
            response = spendApi.getAllSpends(username, currency, from, to)
                    .execute();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
        return response.body() != null
                ? response.body()
                : Collections.emptyList();
    }

    @Nonnull
    @Step("POST /internal/spends/add - Создать трату: {spend.description}")
    public SpendJson createSpend(SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi.createSpend(spend)
                    .execute();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_CREATED, response.code());
        return Objects.requireNonNull(response.body());
    }

    @Nonnull
    @Step("PATCH /internal/spends/edit - Редактировать трату: {spend.description}")
    public SpendJson editSpend(SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi.editSpend(spend)
                    .execute();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
        return Objects.requireNonNull(response.body());
    }

    @Step("DELETE /internal/spends/remove - Удалить траты пользователя: {username}")
    public void deleteSpend(String username, List<String> ids) {
        final Response<Void> response;
        try {
            response = spendApi.deleteSpends(username, ids)
                    .execute();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_ACCEPTED, response.code());
    }

    @Nonnull
    @Step("POST /internal/categories/add - Создать категорию: {category.name}")
    public CategoryJson createCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.createCategory(category)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
        return Objects.requireNonNull(response.body());
    }

    @Nonnull
    @Step("PATCH /internal/categories/update - Обновить категорию: {category.name}")
    public CategoryJson updateCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.updateCategory(category)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
        return Objects.requireNonNull(response.body());
    }

    @Nonnull
    @Step("GET /internal/categories/all - Получить все категории пользователя: {username}")
    public List<CategoryJson> getAllCategories(String username) {
        final Response<List<CategoryJson>> response;
        try {
            response = spendApi.getAllCategoriesByUsername(username)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
        return response.body() != null
                ? response.body()
                : Collections.emptyList();
    }

    @Nonnull
    @Override
    public SpendJson create(SpendJson spend) {
        throw new UnsupportedOperationException("Not implemented :(");
    }

    @Nonnull
    @Override
    public SpendJson update(SpendJson spend) {
        throw new UnsupportedOperationException("Not implemented :(");
    }

    public void deleteCategory(CategoryJson json) {
        throw new UnsupportedOperationException("Not implemented :(");
    }

    public Optional<CategoryJson> findCategoryByNameAndUsername(String categoryName, String username) {
        throw new UnsupportedOperationException("Not implemented :(");
    }

    public Optional<CategoryJson> findCategoryById(UUID id) {
        throw new UnsupportedOperationException("Not implemented :(");
    }

    @Override
    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String name) {
        throw new UnsupportedOperationException("Not implemented :(");
    }

    @Override
    public Optional<SpendJson> findById(UUID id) {
        throw new UnsupportedOperationException("Not implemented :(");
    }

    @Override
    public Optional<SpendJson> findByUsernameAndSpendDescription(String username, String description) {
        throw new UnsupportedOperationException("Not implemented :(");
    }

    @Override
    public void remove(SpendJson spend) {
        throw new UnsupportedOperationException("Not implemented :(");
    }

    @Override
    public void removeCategory(CategoryJson category) {
        throw new UnsupportedOperationException("Not implemented :(");
    }
}
