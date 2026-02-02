package guru.qa.niffler.service;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface SpendClient {
    @Nonnull
    SpendJson create(SpendJson spend);

    @Nonnull
    SpendJson update(SpendJson spend);

    @Nonnull
    CategoryJson updateCategory(CategoryJson category);

    @Nonnull
    CategoryJson createCategory(CategoryJson category);

    Optional<CategoryJson> findCategoryById(UUID id);

    Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String name);

    Optional<SpendJson> findById(UUID id);

    Optional<SpendJson> findByUsernameAndSpendDescription(String username, String description);

    void remove(SpendJson spend);

    void removeCategory(CategoryJson category);
}
