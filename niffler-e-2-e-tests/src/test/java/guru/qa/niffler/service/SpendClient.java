package guru.qa.niffler.service;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DataFilterValues;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.Optional;

public interface SpendClient {
    SpendJson getSpend(String id);

    SpendJson getAllSpends(CurrencyValues filterCurrency, DataFilterValues dataFilterValues);

    SpendJson createSpend(SpendJson spend);

    SpendJson editSpend(SpendJson spend);

    void deleteSpend(List<String> ids);

    CategoryJson createCategory(CategoryJson category);

    Optional<CategoryJson> findCategoryByNameAndUsername(String categoryName, String username);
}
