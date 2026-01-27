package guru.qa.niffler.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.by;
import static com.codeborne.selenide.Selectors.byTagAndText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class FriendsPage {

    private final ElementsCollection friendsRows = $$("#friends tr");
    private final ElementsCollection requestsRows = $$("#requests tr");
    private final SelenideElement noUsersMessage = $(byTagAndText("p", "There are no users yet"));
    private final SelenideElement acceptBtn = $(byTagAndText("span", "Accept"));
    private final SelenideElement allPeopleTab = $("a[href='/people/all']");
    private final ElementsCollection peopleRows = $$("#all tr");
    private final SelenideElement chipWaiting = $(byTagAndText("span", "Waiting..."));
    private final SelenideElement searchInput = $(by("aria-label", "search"));


    public FriendsPage checkThatFriendIsVisible(String expectedFriendName) {
        friendsRows.findBy(Condition.text(expectedFriendName))
                .shouldBe(Condition.visible);
        return this;
    }

    public FriendsPage checkFriendsRequest(String user) {
        requestsRows.findBy(Condition.text(user))
                .shouldBe(Condition.visible);
        return this;
    }

    public FriendsPage checkStatusWaiting(String user) {
        String text = chipWaiting.getText();
        peopleRows.findBy(Condition.text(user))
                .shouldHave(Condition.visible)
                .shouldHave(Condition.text(text));
        return this;
    }

    public FriendsPage checkThatFriendTableIsEmpty() {
        friendsRows.shouldBe(CollectionCondition.empty);
        return this;
    }

    public FriendsPage checkThatNoUsersMessageIsPresent() {
        noUsersMessage.isEnabled();
        return this;
    }

    public FriendsPage clickAllPeopleTab() {
        allPeopleTab.click();
        return this;
    }

    public FriendsPage findUser(String user) {
        searchInput.setValue(user).pressEnter();
        return this;
    }
}
