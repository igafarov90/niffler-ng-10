package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
    private final SelenideElement signUpBtn = $("#register-button");
    private final SelenideElement signInBtn = $("#register-button");
    private final SelenideElement successRegisterMessage = $(".form__paragraph.form__paragraph_success");
    private final SelenideElement helperText = $(".form__error");


    public RegisterPage setUsername(String username) {
        usernameInput.val(username);
        return this;
    }

    public RegisterPage setPassword(String password) {
        passwordInput.val(password);
        return this;
    }

    public RegisterPage setSubmitPassword(String password) {
        passwordSubmitInput.val(password);
        return this;
    }

    public void clickSignUp() {
        signUpBtn.click();
    }

    public RegisterPage fillAndSubmitRegisterForm(String username, String password) {
        setUsername(username)
                .setPassword(password)
                .setSubmitPassword(password)
                .clickSignUp();
        return this;
    }

    public RegisterPage checkSuccessRegisterMessage(String value) {
        successRegisterMessage.shouldHave(text(value));
        return this;
    }

    public LoginPage submitRegistration() {
        signInBtn.click();
        return new LoginPage();
    }

    public RegisterPage checkHelperMessage(String value) {
        helperText.shouldHave(text(value));
        return this;
    }
}