package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class Toast {

    private final SelenideElement container = $(".MuiAlert-message");

    @Nonnull
    @Step("Проверить текст всплывающего сообщения")
    public Toast shouldHaveText(String text) {
        container.shouldHave(text(text));
        return this;
    }

    @Nonnull
    @Step("Проверить отображение всплывающего сообщения")
    public Toast shouldBeVisible() {
        container.shouldBe(visible);
        return this;
    }
}
