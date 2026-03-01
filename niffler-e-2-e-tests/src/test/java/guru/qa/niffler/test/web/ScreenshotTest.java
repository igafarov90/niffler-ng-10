package guru.qa.niffler.test.web;


import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.helpers.SnackbarMessages;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.TestMethodContextExtension;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.open;

@ExtendWith(
        {
                TestMethodContextExtension.class,
                BrowserExtension.class
        }
)
public class ScreenshotTest {

    private static final Config CFG = Config.getInstance();

    @DisplayName("Диаграмма статистики должна быть пустой для нового пользователя")
    @ScreenShotTest("img/empty_chart.png")
    @User
    void shouldReturnEmptyStatForNewUser(UserJson user, BufferedImage expected) throws IOException {
        open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .checkThatPageLoaded()
                .assertChartScreenshotMatches(expected);
    }

    @DisplayName("Диаграмма статистики должна стать пустой после удаления траты")
    @ScreenShotTest("img/empty_chart.png")
    @User(spendings = {
            @Spending(
                    amount = 1000,
                    description = "RandomDataUtils",
                    category = "Категория 1",
                    currency = CurrencyValues.RUB)
    })
    void shouldReturnEmptyStatAfterDelete(UserJson user, BufferedImage expected) throws IOException {
        open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .checkThatPageLoaded()
                .getSpendingTableComponent()
                .deleteSpending(user.testData().spendings().getFirst().description())
                .checkSnackBarText(SnackbarMessages.SPENDING_DELETED)
                .assertChartScreenshotMatches(expected)
                .checkLegendCount(0);
    }

    @DisplayName("Диаграмма статистики должна обновится после добавления новой траты")
    @ScreenShotTest("img/chart_before_create_new_spendings.png")
    @User(spendings = {
            @Spending(
                    amount = 1000,
                    description = "RandomDataUtils",
                    category = "Категория 1",
                    currency = CurrencyValues.RUB)
    })
    void shouldReturnNewStatAfterEditNewSpend(UserJson user, BufferedImage expected) throws IOException {
        String description = RandomDataUtils.randomSentence(2);
        Double amount = 1550.00;
        open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .checkThatPageLoaded()
                .getHeader()
                .addSpendingPage()
                .setAmount(amount)
                .setCategory(RandomDataUtils.randomCategoryName())
                .setSpendingDescription(description)
                .save()
                .checkSnackBarText(SnackbarMessages.SPENDING_CREATED)
                .assertChartStatisticsIsUpdated(expected)
                .checkLegendCount(2);
    }

    @DisplayName("Диаграмма статистики не должна обновиться после добавления траты в архив")
    @User(
            spendings = {
                    @Spending(
                            amount = 1000,
                            description = "RandomDataUtils",
                            category = "Категория 1",
                            currency = CurrencyValues.RUB
                    ),
                    @Spending(amount = 2000,
                            description = "RandomDataUtils",
                            category = "Категория 2",
                            currency = CurrencyValues.RUB
                    )
            }
    )
    @ScreenShotTest("img/chart_after_archiving_spendings.png")
    void shouldReturnNewStatAfterArchiveSpend(UserJson user, BufferedImage expected) throws IOException {
        open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .checkThatPageLoaded()
                .getHeader()
                .toProfilePage()
                .checkCategoryExists(user.testData().spendings().getFirst().category().name())
                .archiveCategory(user.testData().spendings().getFirst().category().name())
                .checkSnackBarText(user.testData().spendings().getFirst().category().name())
                .returnToMainPage()
                .assertChartScreenshotMatches(expected)
                .checkLegendCount(2)
                .checkCategoryInLegendContainsText("Archived");
    }

    @User
    @ScreenShotTest(value = "img/profile_icon.png")
    @DisplayName("Проверка загрузки аватара профиля")
    void shouldUploadNewProfileIcon(UserJson user, BufferedImage expected) throws IOException {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .checkThatPageLoaded()
                .getHeader()
                .toProfilePage()
                .uploadProfileImage("img/avatar.png")
                .assertProfileIconIsAdded(expected);
    }

    @ScreenShotTest(value = "img/profile_icon.png", rewriteExpected = true)
    @DisplayName("Проверка иконки профиля (перезапись expected)")
    @User
    void shouldUploadNewProfileIconRewriteExpected(UserJson user, BufferedImage expected) throws IOException {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .checkThatPageLoaded()
                .getHeader()
                .toProfilePage()
                .rewriteProfileIcon(expected);
    }
}
