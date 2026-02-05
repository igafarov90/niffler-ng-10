package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public abstract class BasePage<T extends BasePage<?>> {

    protected final Header header = new Header();

    private final SelenideElement snackbar = $(".MuiAlert-message");

    @SuppressWarnings("unchecked")
    @Step("Проверить текст всплывающего сообщения")
    public T checkSnackBarText(@Nonnull String message) {
        snackbar.shouldBe(visible).shouldHave(Condition.text(message));
        return (T) this;
    }
}
