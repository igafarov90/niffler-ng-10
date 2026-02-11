package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class RegisterPage {

    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
    private final SelenideElement signUpBtn = $("#register-button");
    private final SelenideElement signInBtn = $("#register-button");
    private final SelenideElement successRegisterMessage = $(".form__paragraph.form__paragraph_success");
    private final SelenideElement helperText = $(".form__error");

    @Step("Ввести имя пользователя: {username}")
    @Nonnull
    public RegisterPage setUsername(String username) {
        usernameInput.val(username);
        return this;
    }

    @Step("Ввести пароль")
    @Nonnull
    public RegisterPage setPassword(String password) {
        passwordInput.val(password);
        return this;
    }

    @Step("Подтвердить пароль")
    @Nonnull
    public RegisterPage setSubmitPassword(String password) {
        passwordSubmitInput.val(password);
        return this;
    }

    @Step("Нажать кнопку 'Sign Up'")
    public void clickSignUp() {
        signUpBtn.click();
    }

    @Nonnull
    @Step("Заполнить и отправить форму регистрации: username={username}")
    public RegisterPage fillAndSubmitRegisterForm(String username, String password) {
        setUsername(username)
                .setPassword(password)
                .setSubmitPassword(password)
                .clickSignUp();
        return this;
    }

    @Nonnull
    @Step("Проверить сообщение об успешной регистрации: '{value}'")
    public RegisterPage checkSuccessRegisterMessage(String value) {
        successRegisterMessage.shouldHave(text(value));
        return this;
    }

    @Step("Перейти к форме входа после регистрации")
    @Nonnull
    public LoginPage submitRegistration() {
        signInBtn.click();
        return new LoginPage();
    }

    @Step("Проверить сообщение: '{value}'")
    @Nonnull
    public RegisterPage checkHelperMessage(String value) {
        helperText.shouldHave(text(value));
        return this;
    }
}