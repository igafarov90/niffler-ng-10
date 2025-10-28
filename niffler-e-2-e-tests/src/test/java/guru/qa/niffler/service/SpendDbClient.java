package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DataFilterValues;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.transaction;

public class SpendDbClient implements SpendClient {

    private static Config CFG = Config.getInstance();

    @Override
    public SpendJson getSpend(String id) {
        throw new UnsupportedOperationException("Not implemented :(");
    }

    @Override
    public SpendJson getAllSpends(CurrencyValues filterCurrency, DataFilterValues dataFilterValues) {
        throw new UnsupportedOperationException("Not implemented :(");
    }

    @Override
    public SpendJson createSpend(SpendJson spend) {
        return transaction(connection -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = new CategoryDaoJdbc(connection)
                                .create(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(
                            new SpendDaoJdbc(connection).create(spendEntity)
                    );
                },
                CFG.spendJdbcUrl()
        );
    }

    public CategoryJson createCategory(CategoryJson category) {
        return transaction(connection -> {
            return CategoryJson.fromEntity(new CategoryDaoJdbc(connection).create(CategoryEntity.fromJson(category)));
        }, CFG.spendJdbcUrl());
    }

    @Override
    public SpendJson editSpend(SpendJson spend) {
        throw new UnsupportedOperationException("Not implemented :(");
    }

    @Override
    public void deleteSpend(List<String> ids) {
        throw new UnsupportedOperationException("Not implemented :(");
    }

    @Override
    public Optional<CategoryJson> findCategoryByNameAndUsername(String username, String categoryName) {
        return transaction(connection -> {
            return new CategoryDaoJdbc(connection).findByUsernameAndCategoryName(username, categoryName)
                    .map(CategoryJson::fromEntity);
        }, CFG.spendJdbcUrl());
    }


    @Override
    public Optional<CategoryJson> findCategoryById(UUID id) {
        return transaction(connection -> {
            return new CategoryDaoJdbc(connection).findById(id)
                    .map(CategoryJson::fromEntity);
        }, CFG.spendJdbcUrl());
    }

    @Override
    public CategoryJson updateCategory(CategoryJson updateJson) {
        throw new UnsupportedOperationException("Not implemented :(");
    }

    @Override
    public void deleteCategory(CategoryJson category) {
        transaction(connection -> {
            new CategoryDaoJdbc(connection).delete(CategoryEntity.fromJson(category));
            return null;
        }, CFG.spendJdbcUrl());
    }
}
