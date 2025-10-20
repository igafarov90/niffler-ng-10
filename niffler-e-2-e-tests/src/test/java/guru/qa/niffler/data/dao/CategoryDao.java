package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.CategoryEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryDao {
    CategoryEntity create(CategoryEntity category);

    Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName);

    Optional<CategoryEntity> findCategoryById(UUID id);

    void deleteCategory(CategoryEntity category);

    List<CategoryEntity> findAllByUserName(String username);
}
