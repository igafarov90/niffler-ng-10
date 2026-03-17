package guru.qa.niffler.page.component;


import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.condition.StatConditions;
import guru.qa.niffler.model.Bubble;
import guru.qa.niffler.utils.ScreenDiffResult;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ParametersAreNonnullByDefault
public class StatComponent extends BaseComponent<StatComponent> {

    public StatComponent() {
        super($("#stat"));
    }

    private final ElementsCollection bubbles = self.$("#legend-container").$$("li");
    private final SelenideElement chart = $("canvas[role='img']");

    @Nonnull
    public BufferedImage chartScreenshot() throws IOException {
        return ImageIO.read(Objects.requireNonNull(chart.screenshot()));

    }

    @Step("Проверить визуальное совпадение диаграммы")
    @Nonnull
    public StatComponent assertChartScreenshotMatches(BufferedImage expected) throws IOException {
        Selenide.sleep(2000);
        BufferedImage actual = chartScreenshot();

        assertFalse(new ScreenDiffResult(actual, expected
        ), "Screen comparison failure");
        return this;
    }

    @Step("Убедится, что диаграмма статистики обновлена")
    @Nonnull
    public StatComponent assertChartStatisticsIsUpdated(BufferedImage expected) throws IOException {
        Selenide.sleep(2000);
        BufferedImage actual = chartScreenshot();

        assertTrue(new ScreenDiffResult(actual, expected
        ), "Screen comparison failure");
        return this;
    }

    @Step("Проверить кол-во категорий в легенде под статистикой равно {count}")
    @Nonnull
    public StatComponent checkBubblesCount(int count) {
        Objects.equals(bubbles.size(), count);
        return this;
    }

    @Step("Проверить цвет и текст баббла статистики {0}")
    @Nonnull
    public StatComponent checkBubbles(Bubble... expectedBubbles) {
        bubbles.should(StatConditions.statBubbles(expectedBubbles));
        return this;
    }

    @Step("Проверить цвет и текст у конкретного баббла статистики {0}")
    @Nonnull
    public StatComponent checkBubblesContains(Bubble... expectedBubbles) {
        bubbles.should(StatConditions.statBubblesContains(expectedBubbles));
        return this;
    }

    @Step("Проверить цвет и текст бабблов статистики в произвольном порядке {0}")
    @Nonnull
    public StatComponent checkBubblesInAnyOrder(Bubble... expectedBubbles) {
        bubbles.should(StatConditions.statBubblesInAnyOrder(expectedBubbles));
        return this;
    }
}
