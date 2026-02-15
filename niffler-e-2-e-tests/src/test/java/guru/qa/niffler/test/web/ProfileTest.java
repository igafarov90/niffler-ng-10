package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.TestMethodContextExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.helpers.SnackbarMessages.PROFILE_UPDATED;


@ExtendWith(
        {
                TestMethodContextExtension.class,
                BrowserExtension.class
        }
)
public class ProfileTest {

    private final static Config CFG = Config.getInstance();

    @DisplayName("Архивная категория должна отображаться в списке категорий")
    @Test
    @User(
            categories = @Category(archived = true)
    )
    void archivedCategoryShouldPresentInCategoriesList(UserJson user) {
        open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .checkThatPageLoaded()
                .getHeader()
                .toProfilePage()
                .switchToggle()
                .checkCategoryExists(user.testData().categories().getFirst().name());
    }

    @DisplayName("Активная категория отображена в списке категорий")
    @User(
            categories = @Category()
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(UserJson user) {
        open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .checkThatPageLoaded()
                .getHeader()
                .toProfilePage()
                .checkCategoryExists(user.testData().categories().getFirst().name());
    }

    @DisplayName("Редактирование профиля пользователя")
    @User
    @Test
    void shouldEditUserProfile(UserJson user) {
        String category = RandomDataUtils.randomCategoryName();
        open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .checkThatPageLoaded()
                .getHeader()
                .toProfilePage()
                .setNewName(RandomDataUtils.randomName())
                .addCategory(category)
                .saveChanges()
                .checkSnackBarText(PROFILE_UPDATED);
    }
}