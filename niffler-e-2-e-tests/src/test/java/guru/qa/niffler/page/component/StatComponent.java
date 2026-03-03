package guru.qa.niffler.page.component;


import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.utils.ScreenDiffResult;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ParametersAreNonnullByDefault
public class StatComponent extends BaseComponent<StatComponent>{

    public StatComponent() {
        super($("#stat"));
    }

    private final ElementsCollection bubbles = self.$("#legend-container").$$("li");
    private final SelenideElement statisticCanvas = $("canvas[role='img']");

    @Step("Проверить визуальное совпадение диаграммы")
    @Nonnull
    public StatComponent assertChartScreenshotMatches(BufferedImage expected) throws IOException {
        Selenide.sleep(2000);
        BufferedImage actual = ImageIO.read(Objects.requireNonNull(statisticCanvas.screenshot()));

        assertFalse(new ScreenDiffResult(actual, expected));
        return this;
    }

    @Step("Убедится, что диаграмма статистики обновлена")
    @Nonnull
    public StatComponent assertChartStatisticsIsUpdated(BufferedImage expected) throws IOException {
        Selenide.sleep(2000);
        BufferedImage actual = ImageIO.read(Objects.requireNonNull(statisticCanvas.screenshot()));

        assertTrue(new ScreenDiffResult(actual, expected));
        return this;
    }

    @Step("Проверить текст в ячейках статистики {0}")
    @Nonnull
    public StatComponent checkStatisticBubblesContains(String... texts) {
        bubbles.should(CollectionCondition.texts(texts));
        return this;
    }

    @Step("Проверить кол-во категорий в легенде под статистикой равно {count}")
    @Nonnull
    public StatComponent checkBubblesCount(int count) {
        Objects.equals(bubbles.size(), count);
        return this;
    }

    @Step("Проверить цвет в ячейке статистики {0}")
    @Nonnull
    public StatComponent checkStatisticBubblesContains(Color color) {
        bubbles.first().should(color(color));
        return this;
    }
}
