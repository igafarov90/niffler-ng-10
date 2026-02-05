package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class EditSpendingPage extends BasePage<EditSpendingPage> {
    private final SelenideElement descriptionInput = $("#description");
    private final SelenideElement saveBtn = $("#save");
    private final SelenideElement amountInput = $("#amount");
    private final SelenideElement categoryInput = $("#category");

    @Step("Ввести новое описание траты: '{description}'")
    public EditSpendingPage setSpendingDescription(@Nonnull String description) {
        descriptionInput.val(description);
        return this;
    }

    @Step("Сохранить изменения траты")
    public MainPage save() {
        saveBtn.click();
        return new MainPage();
    }

    @Step("Ввести стоимость {amount}")
    public EditSpendingPage setAmount(@Nonnull Double amount) {
        amountInput.setValue(amount.toString());
        return this;
    }

    @Step("Ввести категорию {category}")
    public EditSpendingPage setCategory(@Nonnull String category) {
        categoryInput.setValue(category);
        return this;
    }
}
