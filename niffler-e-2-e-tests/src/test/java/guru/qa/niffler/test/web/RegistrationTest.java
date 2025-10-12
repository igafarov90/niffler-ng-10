package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.helpers.ErrorMessages;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.RegisterPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class RegistrationTest {
    private final Faker faker = new Faker();

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
        String username = faker.name().username();
        String password = faker.internet().password();
        registerPage.fillAndSubmitRegisterForm(username, password);
        registerPage.checkSuccessRegisterMessage(SUCCESS_REGISTER_MESSAGE);
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername(){
        String password = faker.internet().password(3,12);
        registerPage.fillAndSubmitRegisterForm(EXISTING_USER, password);
        String message = ErrorMessages.usernameAlreadyExists(EXISTING_USER);
        registerPage.checkHelperMessage(message);
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual(){
        String username = faker.name().username();
        String password = faker.internet().password();
        String confirmPassword = faker.internet().password();
        registerPage.setSubmitPassword(confirmPassword)
                .setPassword(password)
                .setUsername(username)
                .clickSignUp();
        String message = ErrorMessages.getPasswordMatchError();
        registerPage.checkHelperMessage(message);
    }
}
