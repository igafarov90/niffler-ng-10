package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SpendingTable;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byTagAndText;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class MainPage extends BasePage<MainPage> {

    private final SelenideElement spendingTable = $("#spendings");
    private final SelenideElement statisticsComponent = $(byTagAndText("h2", "Statistics"));
    private final SelenideElement historyOfSpendingsComponent =
            $(byTagAndText("h2", "History of Spendings"));

    private final SpendingTable spendingTableComponent = new SpendingTable();

    @Nonnull
    public SpendingTable getSpendingTableComponent() {
        return spendingTableComponent;
    }

    @Nonnull
    public Header getHeader() {
        return header;
    }

    @Nonnull
    @Step("Проверить загрузку главной страницы")
    public MainPage checkThatPageLoaded() {
        spendingTable.should(visible);
        return this;
    }

    @Nonnull
    @Step("Проверить наличие разделов 'Statistics' и 'History of spendings' на главной странице")
    public MainPage checkThatPageHaveComponentsStatisticsAndHistory() {
        statisticsComponent.should(visible);
        historyOfSpendingsComponent.should(visible);
        return this;
    }

    @Nonnull
    @Step("Переход на страницу редактирования spending")
    public EditSpendingPage editSpending(String description) {
        spendingTable.$$("tbody tr").find(text(description)).$$("td").get(5).click();
        return new EditSpendingPage();
    }
}
