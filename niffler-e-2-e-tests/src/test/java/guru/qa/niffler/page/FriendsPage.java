package guru.qa.niffler.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.SearchField;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selectors.byTagAndText;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class FriendsPage extends BasePage<FriendsPage> {

    private final ElementsCollection friendsRows = $$("#friends tr");
    private final ElementsCollection requestsRows = $$("#requests tr");
    private final SelenideElement noUsersMessage = $(byTagAndText("p", "There are no users yet"));
    private final SelenideElement acceptBtn = $(byText("Accept"));
    private final SelenideElement declineBtn = $(byText("Decline"));
    private final SelenideElement allPeopleTab = $("a[href='/people/all']");
    private final ElementsCollection peopleRows = $$("#all tr");
    private final SelenideElement chipWaiting = $(byTagAndText("span", "Waiting..."));
    private final SelenideElement popUp = $("[role='dialog']");

    private final SearchField searchField = new SearchField();

    @Nonnull
    @Step("Проверить, что друг '{expectedFriendName}' отображается в списке друзей")
    public FriendsPage checkThatFriendIsVisible(String expectedFriendName) {
        searchField.search(expectedFriendName);
        friendsRows.findBy(Condition.text(expectedFriendName))
                .shouldBe(Condition.visible);
        return this;
    }

    @Nonnull
    @Step("Проверить наличие входящего запроса в друзья от пользователя: '{user}'")
    public FriendsPage checkFriendsRequest(@Nonnull String user) {
        requestsRows.findBy(Condition.text(user))
                .shouldBe(Condition.visible);
        return this;
    }

    @Nonnull
    @Step("Проверить, что у пользователя '{user}' статус 'В ожидании'")
    public FriendsPage checkStatusWaiting(String user) {
        String text = chipWaiting.getText();
        peopleRows.findBy(Condition.text(user))
                .shouldHave(Condition.visible)
                .shouldHave(Condition.text(text));
        return this;
    }

    @Nonnull
    @Step("Проверить, что таблица друзей пустая")
    public FriendsPage checkThatFriendTableIsEmpty() {
        friendsRows.shouldBe(CollectionCondition.empty);
        return this;
    }

    @Nonnull
    @Step("Проверить наличие сообщения 'There are no users yet'")
    public FriendsPage checkThatNoUsersMessageIsPresent() {
        noUsersMessage.isEnabled();
        return this;
    }

    @Nonnull
    @Step("Перейти на вкладку 'All people'")
    public FriendsPage clickAllPeopleTab() {
        allPeopleTab.click();
        return this;
    }

    @Nonnull
    @Step("Поиск друга {friendName}")
    public FriendsPage searchFriend(String friendName) {
        searchField.search(friendName);
        return this;
    }

    @Nonnull
    @Step("Принять дружбу")
    public FriendsPage acceptFriendRequest() {
        acceptBtn.click();
        return this;
    }

    @Nonnull
    @Step("Отклонить дружбу")
    public FriendsPage declineFriendRequest() {
        declineBtn.click();
        popUp.find(byText("Decline")).click();
        return this;
    }
}
