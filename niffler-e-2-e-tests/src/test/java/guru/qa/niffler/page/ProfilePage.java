package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class ProfilePage {
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement nameInput = $("#name");
    private final SelenideElement categoryInput = $("#category");
    private final SelenideElement toggle = $(".MuiSwitch-input");
    private final SelenideElement saveChangesBtn = $("button[type='submit']");
    private final ElementsCollection categoryChip = $$(".MuiChip-root");

    @Nonnull
    @Step("Добавить новую категорию: {categoryName}")
    public ProfilePage addCategory(String category) {
        categoryInput.setValue(category).pressEnter();
        return this;
    }

    @Nonnull
    @Step("Ввести имя новой категории: {categoryName}")
    public ProfilePage setNewName(String name) {
        nameInput.clear();
        nameInput.val(name);
        return this;
    }

    @Nonnull
    @Step("Сохранить изменения в профиле")
    public ProfilePage saveChanges() {
        saveChangesBtn.click();
        return this;
    }

    @Nonnull
    @Step("Проверить наличие категории в хлебных крошках: {categoryName}")
    public ProfilePage checkCategoryExists(String category) {
        categoryChip.find(text(category)).shouldBe(visible);
        return this;
    }

    @Nonnull
    @Step("Включить отображение архивных категорий")
    public ProfilePage switchToggle() {
        toggle.click();
        return this;
    }
}
