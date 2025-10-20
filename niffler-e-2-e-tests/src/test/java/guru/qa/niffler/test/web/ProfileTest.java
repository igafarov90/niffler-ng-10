package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.CategoryExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;

@ExtendWith({BrowserExtension.class, CategoryExtension.class})
public class ProfileTest {
    private final static String HEADER = "Profile";
    private final static String USERNAME = "iegafarov";
    private final static String PASSWORD = "iegafarov";
    private final static Config CFG = Config.getInstance();
    ProfilePage profilePage;

    @BeforeEach
    public void setUp() {
        profilePage = open(CFG.frontUrl(), LoginPage.class)
                .login(USERNAME, PASSWORD)
                .checkThatPageLoaded()
                .openProfilePage()
                .checkPageHeader(HEADER);
    }

    @User(
            username = USERNAME,
            categories = {
                    @Category(archived = true)
            }
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
        profilePage.switchToggle();
        profilePage.checkCategoryExists(category.name());
    }

    @User(
            username = USERNAME,
            categories = {
                    @Category()
            }
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        profilePage.checkCategoryExists(category.name());
    }
}
