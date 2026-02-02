package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.TestMethodContextExtension;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DataFilterValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;

@ExtendWith(
        {
                TestMethodContextExtension.class,
                BrowserExtension.class
        }
)
public class SpendingTest {

    private static final Config CFG = Config.getInstance();

    @User(
            spendings = @Spending(
                    category = "Учеба",
                    amount = 89900,
                    currency = CurrencyValues.RUB,
                    description = "Обучение Niffler 2.0 юбилейный поток!"
            )
    )
    @Test
    void spendingDescriptionShouldBeEditedByTableAction(UserJson user) {
        final String spendDescription = user.testData().spendings().getFirst().description();
        final String newDescription = "Обучение Niffler Next Generation";

        open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .editSpending(spendDescription)
                .setSpendingDescription(newDescription)
                .save()
                .getSpendingTableComponent()
                .checkTableContains(newDescription);
    }

    @DisplayName("Фильтрация таблицы трат, за период 'неделя' ")
    @User(
            spendings = {
                    @Spending(
                            amount = 1000,
                            description = "RandomDataUtils",
                            category = "Категория 1",
                            currency = CurrencyValues.RUB)
            }
    )
    @Test
    void shouldFilterSpendingsByPeriod(UserJson user) {
        open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getSpendingTableComponent()
                .checkTableSize(1)
                .selectPeriod(DataFilterValues.WEEK)
                .checkTableSize(1);
    }

    @Test
    @DisplayName("Добавление новой траты")
    @User
    public void addNewSpending(UserJson user) {
        String description = RandomDataUtils.randomSentence(2);
        open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .checkThatPageLoaded()
                .getHeader()
                .addSpendingPage()
                .setAmount(RandomDataUtils.randomAmount())
                .setCategory(RandomDataUtils.randomCategoryName())
                .setSpendingDescription(description)
                .save()
                .getSpendingTableComponent()
                .checkTableContains(description);
    }
}
