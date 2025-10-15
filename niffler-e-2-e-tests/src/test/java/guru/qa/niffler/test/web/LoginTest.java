package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.helpers.ErrorMessages;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class LoginTest {
    private final Faker faker = new Faker();

    private final static String USER_NAME = "iegafarov";
    private final static String PASSWORD = "iegafarov";
    private static final Config CFG = Config.getInstance();
    private final static String HEADER = "Log In";

    private LoginPage loginPage;

    @BeforeEach
    void setUp() {
        loginPage = Selenide.open(CFG.frontUrl(), LoginPage.class);
    }

    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        Selenide.open(CFG.frontUrl(), LoginPage.class);
        loginPage.login(USER_NAME, PASSWORD)
                .checkThatPageLoaded()
                .checkThatPageHaveComponentsStatisticsAndHistory();
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        String fakePassword = faker.internet().password();
        loginPage = loginPage.loginWithError(USER_NAME, fakePassword)
                .checkPageHeader(HEADER)
                .checkErrorMessage(ErrorMessages.getBadCredentialsError());
    }
}
