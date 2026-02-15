package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SearchField;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class PeoplePage extends BasePage<PeoplePage> {

    private final Header header = new Header();
    private final SearchField searchField = new SearchField();
    private final SelenideElement allPeopleTable = $(".MuiTable-root");
    private final ElementsCollection allPeopleRows = allPeopleTable.$$("tbody tr");

    @Nonnull
    @Step("Поиск пользователя с никнеймом {username}")
    public PeoplePage searchPerson(String username) {
        searchField.search(username);
        return this;
    }

    @Nonnull
    @Step("Проверить исходящее приглашение пользователю {username}")
    public PeoplePage checkOutcomeInvitationShouldBeVisible(String username) {
        searchField.search(username);
        allPeopleRows.findBy(text(username))
                .shouldBe(visible)
                .$(".MuiChip-label")
                .shouldHave(text("Waiting..."));
        return this;
    }
}
