package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.helpers.ErrorMessages;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@ExtendWith(BrowserExtension.class)
public class LoginTest {

    private static final Config CFG = Config.getInstance();
    private final static String HEADER = "Log In";

@User
    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
                .checkThatPageLoaded()
                .checkThatPageHaveComponentsStatisticsAndHistory();
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        String fakePassword = RandomDataUtils.randomPassword(3,12);
        Selenide.open(CFG.frontUrl(), LoginPage.class)
        .loginWithError(randomUsername(), fakePassword)
                .checkPageHeader(HEADER)
                .checkErrorMessage(ErrorMessages.getBadCredentialsError());
    }
}
