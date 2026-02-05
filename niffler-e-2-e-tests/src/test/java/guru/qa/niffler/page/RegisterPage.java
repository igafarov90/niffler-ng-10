package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class RegisterPage extends BasePage<RegisterPage> {

    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
    private final SelenideElement signUpBtn = $("#register-button");
    private final SelenideElement signInBtn = $("#register-button");
    private final SelenideElement successRegisterMessage = $(".form__paragraph.form__paragraph_success");
    private final SelenideElement helperText = $(".form__error");

    @Step("Ввести имя пользователя: {username}")
    public RegisterPage setUsername(@Nonnull String username) {
        usernameInput.val(username);
        return this;
    }

    @Step("Ввести пароль")
    public RegisterPage setPassword(@Nonnull String password) {
        passwordInput.val(password);
        return this;
    }

    @Step("Подтвердить пароль")
    public RegisterPage setSubmitPassword(@Nonnull String password) {
        passwordSubmitInput.val(password);
        return this;
    }

    @Step("Нажать кнопку 'Sign Up'")
    public RegisterPage clickSignUp() {
        signUpBtn.click();
        return this;
    }

    @Step("Заполнить и отправить форму регистрации: username={username}")
    public RegisterPage fillAndSubmitRegisterForm(String username, String password) {
        setUsername(username)
                .setPassword(password)
                .setSubmitPassword(password)
                .clickSignUp();
        return this;
    }

    @Step("Проверить сообщение об успешной регистрации: '{value}'")
    public RegisterPage checkSuccessRegisterMessage(@Nonnull String value) {
        successRegisterMessage.shouldHave(text(value));
        return this;
    }

    @Step("Перейти к форме входа после регистрации")
    public LoginPage submitRegistration() {
        signInBtn.click();
        return new LoginPage();
    }

    @Step("Проверить сообщение: '{value}'")
    public RegisterPage checkHelperMessage(@Nonnull String value) {
        helperText.shouldHave(text(value));
        return this;
    }
}