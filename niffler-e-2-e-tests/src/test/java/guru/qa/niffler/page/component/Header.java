package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.*;
import io.qameta.allure.Step;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selectors.byTagAndText;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class Header extends BaseComponent<Header> {

    private final SelenideElement profileIcon = self.$("[data-testid='PersonIcon']");
    private final SelenideElement profileItem = $("a[href='/profile']");
    private final SelenideElement friendsItem = $("a[href='/people/friends']");
    private final SelenideElement allPeopleItem = $("a[href='/people/all']");
    private final SelenideElement sigOutItem = $(byTagAndText("li", "Sign out"));
    private final SelenideElement mainIcon = self.$("a[href='/main']");
    private final SelenideElement newSpendingBtnIcon = self.$("a[href='/spending']");

    public Header() {
        super($("#root header"));
    }

    @Step("Перейти к странице 'Friends'")
    public FriendsPage toFriendsPage() {
        profileIcon.click();
        friendsItem.click();
        return new FriendsPage();
    }

    @Step("Перейти к странице 'All people'")
    public PeoplePage toAllPeoplePage() {
        profileIcon.click();
        allPeopleItem.click();
        return new PeoplePage();
    }

    @Step("Перейти к странице 'Profile'")
    public ProfilePage toProfilePage() {
        profileIcon.click();
        profileItem.click();
        return new ProfilePage();
    }

    @Step("Перейти к странице 'Login'")
    public LoginPage signOut() {
        profileIcon.click();
        sigOutItem.click();
        return new LoginPage();
    }

    @Step("Перейти к странице 'Spendings'")
    public EditSpendingPage addSpendingPage() {
        newSpendingBtnIcon.click();
        return new EditSpendingPage();
    }

    @Step("Перейти к странице 'Main'")
    public MainPage toMainPage() {
        mainIcon.click();
        return new MainPage();
    }
}
