package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.DataFilterValues;
import guru.qa.niffler.page.EditSpendingPage;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class SpendingTable {

    private final SelenideElement self = $("#spendings");

    private final SelenideElement currencyBox = self.$("#currency");
    private final SelenideElement periodBox = self.$("#period");
    private final SelenideElement deleteBtn = self.$("#delete");
    private final SelenideElement popup = $("div[role='dialog']");

    private final ElementsCollection periodOptions = self.$$(".MuiMenu-list");
    private final ElementsCollection spendingRows = self.$("tbody").$$("tr");

    private final SearchField searchField = new SearchField();


    @Step("Выбрать период трат: {period}")
    public SpendingTable selectPeriod(@Nonnull DataFilterValues period) {
        periodBox.click();
        $(byAttribute("data-value", period.name())).click();
        return this;
    }

    @Step("Изменить трату: {description}")
    public EditSpendingPage editSpending(@Nonnull String description) {
        searchSpendingByDescription(description);
        var row = spendingRows.find(text(description));
        row.$$("td").get(5).click();
        return new EditSpendingPage();
    }

    @Step("Удалить трату {description}")
    public SpendingTable deleteSpending(@Nonnull String description) {
        searchSpendingByDescription(description);
        var row = spendingRows.find(text(description));
        row.$$("td").get(0).click();
        deleteBtn.click();
        popup.find(byText("Delete")).click();
        return this;
    }

    @Step("Найти в таблице трату {description}")
    public SpendingTable searchSpendingByDescription(@Nonnull String description) {
        searchField.search(description);
        return this;
    }

    @Step("Проверить, что в таблице присутствуют траты")
    public SpendingTable checkTableContains(@Nonnull String... expectedSpends) {
        for (int i = 0; i < expectedSpends.length; i++) {
            var expected = expectedSpends[i];
            searchField.clearIfNotEmpty();
            searchSpendingByDescription(expected);
            spendingRows.find(text(expected));
        }
        return this;
    }

    @Step("Проверить, что размер таблицы равен {expectedSize}")
    public @Nonnull SpendingTable checkTableSize(int expectedSize) {
        spendingRows.should(size(expectedSize));
        return this;
    }
}
