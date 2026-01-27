package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.TestMethodContextExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;

@ExtendWith(
        {
                TestMethodContextExtension.class,
                BrowserExtension.class
        }
)
public class FriendsTest {
    private static final Config CFG = Config.getInstance();

    @User(friends = 1)
    @Test
    void friendsShouldBePresentInFriendsTable(UserJson user) {
        open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .openFriendsPage()
                .checkThatFriendIsVisible(user.testData().friends().getFirst().username());
    }

    @User
    @Test
    void friendsTableShouldBeEmptyForNewUser(UserJson user) {
        open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .openFriendsPage()
                .checkThatFriendTableIsEmpty()
                .checkThatNoUsersMessageIsPresent();
    }

    @User(incomeInvitations = 1)
    @Test
    void incomeInvitationBePresentInFriendsTable(UserJson user) {
        open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .openFriendsPage()
                .findUser(user.testData().incomeInvitations().getFirst().username())
                .checkFriendsRequest(user.testData().incomeInvitations().getFirst().username());
    }

    @User(outcomeInvitations = 1)
    @Test
    void outcomeInvitationBePresentInFriendsTable(UserJson user) {
        open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .openFriendsPage()
                .clickAllPeopleTab()
                .findUser(user.testData().outcomeInvitations().getFirst().username())
                .checkStatusWaiting(user.testData().outcomeInvitations().getFirst().username());
    }
}
