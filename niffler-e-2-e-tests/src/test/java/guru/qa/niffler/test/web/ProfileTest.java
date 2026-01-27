package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.CategoryExtension;
import guru.qa.niffler.jupiter.extension.TestMethodContextExtension;
import guru.qa.niffler.jupiter.extension.UserExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;


@ExtendWith(
        {
                TestMethodContextExtension.class,
          //      UserExtension.class,
            //    CategoryExtension.class,
                BrowserExtension.class
        }
)
public class ProfileTest {
    private final static String HEADER = "Profile";
    private final static Config CFG = Config.getInstance();
    ProfilePage profilePage;

    @Test
    @User(
            categories = @Category(archived = true)
    )
    void archivedCategoryShouldPresentInCategoriesList(UserJson user) {
        profilePage = open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .checkThatPageLoaded()
                .openProfilePage()
                .checkPageHeader(HEADER);
        profilePage.switchToggle();
        profilePage.checkCategoryExists(user.testData().categories().getFirst().name());
    }

    @User(
            categories = @Category()
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(UserJson user) {
        profilePage = open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .checkThatPageLoaded()
                .openProfilePage()
                .checkPageHeader(HEADER)
                .checkCategoryExists(user.testData().categories().getFirst().name());
    }
}
