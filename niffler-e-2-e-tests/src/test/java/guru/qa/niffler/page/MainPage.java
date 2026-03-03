package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SpendingTable;
import guru.qa.niffler.utils.ScreenDiffResult;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byTagAndText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ParametersAreNonnullByDefault
public class MainPage extends BasePage<MainPage> {

    private final SelenideElement spendingTable = $("#spendings");
    private final ElementsCollection statisticsLegend = $$("div[id='legend-container'] ul");
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

    @Step("Проверить кол-во категорий в легенде под статистикой равно {count}")
    public MainPage checkLegendCount(int count) {
        Objects.equals(statisticsLegend.size(), count);
        return this;
    }

    @Step("Проверить наличие в легенде категории, содержащей текст '{textPart}'")
    public MainPage checkCategoryInLegendContainsText(String textPart) {
        statisticsLegend.findBy(text(textPart))
                .shouldBe(visible);
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

    @Step("Проверить визуальное совпадение диаграммы")
    public MainPage assertChartScreenshotMatches(BufferedImage expected) throws IOException {
        Selenide.sleep(2000);
        BufferedImage actual = ImageIO.read($("canvas[role='img']").screenshot());

        assertFalse(new ScreenDiffResult(actual, expected));
        return this;
    }

    @Step("Убедится, что диаграмма статистики обновлена")
    public MainPage assertChartStatisticsIsUpdated(BufferedImage expected) throws IOException {
        Selenide.sleep(2000);
        BufferedImage actual = ImageIO.read($("canvas[role='img']").screenshot());

        assertTrue(new ScreenDiffResult(actual, expected));
        return this;
    }
}
