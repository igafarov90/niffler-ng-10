package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.impl.SpendRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class SpendDbClient implements SpendClient {

    private final SpendRepository spendRepository = new SpendRepositoryHibernate();
    private static Config CFG = Config.getInstance();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.spendJdbcUrl());

    @Nonnull
    @Override
    @Step("Создать категорию: {category.name}")
    public CategoryJson createCategory(CategoryJson category) {
        return Objects.requireNonNull(xaTransactionTemplate.execute(() -> {
            CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
            spendRepository.createCategory(categoryEntity);
            return CategoryJson.fromEntity(categoryEntity);
        }));
    }

    @Override
    @Step("Найти категорию пользователя: username={username}, name={name}")
    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return spendRepository.findCategoryByUsernameAndCategoryName(username, categoryName)
                .map(CategoryJson::fromEntity);
    }

    @Override
    @Step("Найти категорию по ID: {id}")
    public Optional<CategoryJson> findCategoryById(UUID id) {
        return spendRepository.findCategoryById(id)
                .map(CategoryJson::fromEntity);
    }

    @Override
    @Step("Найти трату по ID: {id}")
    public Optional<SpendJson> findById(UUID id) {
        return spendRepository.findById(id)
                .map(SpendJson::fromEntity);
    }

    @Override
    @Step("Найти трату пользователя: username={username}, description={description}")
    public Optional<SpendJson> findByUsernameAndSpendDescription(String username, String description) {
        return spendRepository.findByUsernameAndSpendDescription(username, description)
                .map(SpendJson::fromEntity);
    }

    @Override
    @Step("Удалить трату ID: {spend.id}")
    public void remove(SpendJson spend) {
        xaTransactionTemplate.execute(() -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);
            spendRepository.remove(spendEntity);
            return null;
        });
    }

    @Override
    @Step("Удалить категорию ID: {category.id}")
    public void removeCategory(CategoryJson category) {
        xaTransactionTemplate.execute(() -> {
            CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
            spendRepository.removeCategory(categoryEntity);
            return null;
        });
    }

    @Nonnull
    @Override
    @Step("Создать трату: {spend.description}")
    public SpendJson create(SpendJson spend) {
        return Objects.requireNonNull(xaTransactionTemplate.execute(() -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);
            spendRepository.create(spendEntity);
            return SpendJson.fromEntity(spendEntity);
        }));
    }

    @Nonnull
    @Override
    @Step("Обновить трату ID: {spend.id}")
    public SpendJson update(SpendJson spend) {
        return Objects.requireNonNull(xaTransactionTemplate.execute(() -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);
            spendRepository.update(spendEntity);
            return SpendJson.fromEntity(spendEntity);
        }));
    }

    @Nonnull
    @Override
    @Step("Обновить категорию ID: {category.id}")
    public CategoryJson updateCategory(CategoryJson category) {
        return Objects.requireNonNull(xaTransactionTemplate.execute(() -> {
            CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
            spendRepository.updateCategory(categoryEntity);
            return CategoryJson.fromEntity(categoryEntity);
        }));
    }
}
