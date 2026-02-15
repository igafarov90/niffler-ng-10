package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class LoginPage extends BasePage<LoginPage> {
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement submitBtn = $("#login-button");
    private final SelenideElement registerBtn = $("#register-button");
    private final SelenideElement header = $("h1");
    private final SelenideElement errorMessage = $(".form__error");

    @Nonnull
    @Step("Логин в систему пользователя под пользователем с логином {username} и паролем {password}")
    public MainPage login(String username, String password) {
        usernameInput.val(username);
        passwordInput.val(password);
        submitBtn.click();
        return new MainPage();
    }

    @Nonnull
    @Step("Логин несуществующего пользователя в системе")
    public LoginPage loginWithError(String username, String password) {
        usernameInput.val(username);
        passwordInput.val(password);
        submitBtn.click();
        return this;
    }

    @Nonnull
    @Step("Открыть страницу регистрации")
    public RegisterPage openRegistrationPage() {
        registerBtn.click();
        return new RegisterPage();
    }

    @Nonnull
    @Step("Проверить заголовок страницы регистрации")
    public LoginPage checkPageHeader(String value) {
        header.shouldHave(text(value));
        return this;
    }

    @Nonnull
    @Step("Проверить сообщение об ошибке при логине")
    public LoginPage checkErrorMessage(String value) {
        errorMessage.shouldHave(text(value));
        return this;
    }
}
