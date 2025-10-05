package guru.qa.niffler.service;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DataFilterValues;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.apache.hc.core5.http.HttpStatus.SC_ACCEPTED;
import static org.apache.hc.core5.http.HttpStatus.SC_OK;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpendApiClient implements SpendClient {

    private static final Config CFG = Config.getInstance();

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(CFG.spendUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final SpendApi spendApi = retrofit.create(SpendApi.class);

    @Override
    public SpendJson getSpend(String id) {
        final Response<SpendJson> response;
        try {
            response = spendApi.getSpend(id)
                    .execute();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
        return response.body();
    }

    @Override
    public SpendJson getAllSpends(CurrencyValues filterCurrency, DataFilterValues dataFilterValues) {
        final Response<SpendJson> response;
        try {
            response = spendApi.getAllSpends(filterCurrency, dataFilterValues)
                    .execute();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
        return response.body();
    }

    @Override
    public SpendJson createSpend(SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi.createSpend(spend)
                    .execute();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_ACCEPTED, response.code());
        return response.body();

    }

    @Override
    public SpendJson editSpend(SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi.editSpend(spend)
                    .execute();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
        return response.body();
    }

    @Override
    public void deleteSpend(List<String> ids) {
        final Response<Void> response;
        try {
            response = spendApi.deleteSpends(ids)
                    .execute();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
    }

    public CategoryJson createCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.createCategory(category)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
        return response.body();
    }

    public Response<CategoryJson> updateCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.updateCategory(category)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
        return response;
    }

    public List<CategoryJson> getAllCategories(String username, boolean excludeArchive) {
        final Response<List<CategoryJson>> response;
        try {
            response = spendApi.getAllCategoriesByUsername(username, excludeArchive)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
        return response.body();
    }


    public Optional<CategoryJson> findCategoryByNameAndUsername(String categoryName, String username) {
        throw new UnsupportedOperationException("Not implemented :(");
    }
}
