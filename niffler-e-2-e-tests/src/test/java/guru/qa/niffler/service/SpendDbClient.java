package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DataFilterValues;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDbClient implements SpendClient {

    private final CategoryDao categoryDao = new CategoryDaoJdbc();
    private final SpendDao spendDao = new SpendDaoJdbc();
    private static Config CFG = Config.getInstance();

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
            CFG.spendJdbcUrl()
    );

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
        return jdbcTxTemplate.execute(() -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(spendDao.create(spendEntity)
                    );
                }
        );
    }

    @Override
    public CategoryJson createCategory(CategoryJson category) {
        return jdbcTxTemplate.execute(() -> CategoryJson.fromEntity(
                        categoryDao.create(
                                CategoryEntity.fromJson(category)
                        )
                )
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
        return jdbcTxTemplate.execute(() -> categoryDao
                .findByUsernameAndCategoryName(username, categoryName)
                .map(CategoryJson::fromEntity));
    }


    @Override
    public Optional<CategoryJson> findCategoryById(UUID id) {
        return jdbcTxTemplate.execute(() -> categoryDao
                .findById(id)
                .map(CategoryJson::fromEntity));
    }

    @Override
    public CategoryJson updateCategory(CategoryJson category) {
        return jdbcTxTemplate.execute(() -> CategoryJson.fromEntity(
                        categoryDao.create(
                                CategoryEntity.fromJson(category))
                )
        );
    }

    @Override
    public void deleteCategory(CategoryJson category) {
        jdbcTxTemplate.execute(() -> {
                    categoryDao.delete(CategoryEntity.fromJson(category)
                    );
                    return null;
                }
        );
    }
}
