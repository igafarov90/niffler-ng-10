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

    @Nonnull
    @Step("Ввести новое описание траты: '{description}'")
    public EditSpendingPage setSpendingDescription(String description) {
        descriptionInput.val(description);
        return this;
    }

    @Nonnull
    @Step("Сохранить изменения траты")
    public MainPage save() {
        saveBtn.click();
        return new MainPage();
    }

    @Nonnull
    @Step("Ввести стоимость {amount}")
    public EditSpendingPage setAmount(Double amount) {
        amountInput.setValue(amount.toString());
        return this;
    }

    @Nonnull
    @Step("Ввести категорию {category}")
    public EditSpendingPage setCategory(String category) {
        categoryInput.setValue(category);
        return this;
    }
}
