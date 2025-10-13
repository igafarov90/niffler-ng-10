package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.helpers.ErrorMessages;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.RegisterPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class RegistrationTest {

    private static final String SUCCESS_REGISTER_MESSAGE = "Congratulations! You've registered!";
    private static final String EXISTING_USER = "iegafarov";
    private static final Config CFG = Config.getInstance();
    private RegisterPage registerPage;

    @BeforeEach
    void setUp(){
        LoginPage loginPage = Selenide.open(CFG.frontUrl(), LoginPage.class);
        registerPage = loginPage.openRegistrationPage();
    }

    @Test
    void shouldRegisterNewUser(){
        String username = RandomDataUtils.randomUsername();
        String password = RandomDataUtils.randomPassword(3,12);
        registerPage.fillAndSubmitRegisterForm(username, password);
        registerPage.checkSuccessRegisterMessage(SUCCESS_REGISTER_MESSAGE);
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername(){
        String password = RandomDataUtils.randomPassword(3,12);
        registerPage.fillAndSubmitRegisterForm(EXISTING_USER, password);
        String message = ErrorMessages.usernameAlreadyExists(EXISTING_USER);
        registerPage.checkHelperMessage(message);
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual(){
        String username = RandomDataUtils.randomUsername();
        String password = RandomDataUtils.randomPassword(3,12);
        String confirmPassword = RandomDataUtils.randomPassword(3,12);
        registerPage.setSubmitPassword(confirmPassword)
                .setPassword(password)
                .setUsername(username)
                .clickSignUp();
        String message = ErrorMessages.getPasswordMatchError();
        registerPage.checkHelperMessage(message);
    }
}
