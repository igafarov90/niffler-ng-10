package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
  private final SelenideElement usernameInput = $("#username");
  private final SelenideElement passwordInput = $("#password");
  private final SelenideElement submitBtn = $("#login-button");
  private final SelenideElement registerBtn = $("#register-button");
  private final SelenideElement header = $("h1");
  private final SelenideElement helperMessage = $(".form__error");

  public MainPage login(String username, String password) {
    usernameInput.val(username);
    passwordInput.val(password);
    submitBtn.click();
    return new MainPage();
  }

  public LoginPage loginWithError(String username, String password) {
    usernameInput.val(username);
    passwordInput.val(password);
    submitBtn.click();
    return this;
  }

  public RegisterPage openRegistrationPage(){
    registerBtn.click();
    return new RegisterPage();
  }

  public LoginPage checkPageHeader(String value){
    header.shouldHave(text(value));
    return this;
  }

  public LoginPage checkHelperMessage(String value){
    helperMessage.shouldHave(text(value));
    return this;
  }
}
