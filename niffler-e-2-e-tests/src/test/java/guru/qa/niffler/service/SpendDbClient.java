package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
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

public class SpendDbClient implements SpendClient {

    private final SpendDao spendDao = new SpendDaoJdbc();
    private final CategoryDao categoryDao = new CategoryDaoJdbc();

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
        SpendEntity spendEntity = SpendEntity.fromJson(spend);
        if (spendEntity.getCategory().getId() == null) {
            CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory());
            spendEntity.setCategory(categoryEntity);
        }
        return SpendJson.fromEntity(
                spendDao.create(spendEntity)
        );
    }

    public CategoryJson createCategory(CategoryJson category) {
        CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
        return CategoryJson.fromEntity(
                categoryDao.create(categoryEntity)
        );
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
        Optional<CategoryEntity> categoryEntity = categoryDao.findByUsernameAndCategoryName(username, categoryName);
        return categoryEntity.map(CategoryJson::fromEntity);
    }

    @Override
    public Optional<CategoryJson> findCategoryById(UUID id) {
        Optional<CategoryEntity> categoryEntity = categoryDao.findById(id);
        return categoryEntity.map(CategoryJson::fromEntity);
    }

    @Override
    public CategoryJson updateCategory(CategoryJson updateJson) {
        throw new UnsupportedOperationException("Not implemented :(");
    }

    @Override
    public void deleteCategory(CategoryJson category) {
        CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
        categoryDao.delete(categoryEntity);
    }
}
