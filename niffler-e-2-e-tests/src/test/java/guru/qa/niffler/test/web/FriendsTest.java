package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.helpers.SnackbarMessages;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.TestMethodContextExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
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

public class FriendsTest {
    private static final Config CFG = Config.getInstance();

    @DisplayName("дружбы отображается при поиске в таблице 'friends'")
    @User(friends = 1)
    @Test
    void friendsShouldBePresentInFriendsTable(UserJson user) {
        open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader()
                .toFriendsPage()
                .checkThatFriendIsVisible(user.testData().friends().getFirst().username());
    }

    @DisplayName("Таблице 'friends' должна быть пуста для нового пользователя")
    @User
    @Test
    void friendsTableShouldBeEmptyForNewUser(UserJson user) {
        open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader()
                .toFriendsPage()
                .checkThatFriendTableIsEmpty()
                .checkThatNoUsersMessageIsPresent();
    }

    @DisplayName("Входящий запрос дружбы отображается при поиске в таблице 'friends'")
    @User(incomeInvitations = 1)
    @Test
    void incomeInvitationBePresentInFriendsTable(UserJson user) {
        open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader()
                .toFriendsPage()
                .searchFriend(user.testData().incomeInvitations().getFirst().username())
                .checkFriendsRequest(user.testData().incomeInvitations().getFirst().username());
    }

    @DisplayName("Прием заявки в друзья")
    @User(incomeInvitations = 1)
    @Test
    void acceptIncomingFriendRequest(UserJson user) {
        System.out.println(user.testData().incomeInvitations().getFirst().username());
        open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader()
                .toFriendsPage()
                .searchFriend(user.testData().incomeInvitations().getFirst().username())
                .checkFriendsRequest(user.testData().incomeInvitations().getFirst().username())
                .acceptFriendRequest()
                .checkSnackBarText(SnackbarMessages.friendAccepted(user.testData().incomeInvitations().getFirst().username()));
    }

    @DisplayName("Отклонение заявки в друзья")
    @User(incomeInvitations = 1)
    @Test
    void declineIncomingFriendRequest (UserJson user) {
        System.out.println(user.testData().incomeInvitations().getFirst().username());
        open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader()
                .toFriendsPage()
                .searchFriend(user.testData().incomeInvitations().getFirst().username())
                .checkFriendsRequest(user.testData().incomeInvitations().getFirst().username())
                .declineFriendRequest()
                .checkSnackBarText(SnackbarMessages.friendDeclined(user.testData().incomeInvitations().getFirst().username()));
    }

    @DisplayName("Исходящий запрос дружбы отображается при поиске в таблице 'all people'")
    @User(outcomeInvitations = 1)
    @Test
    void outcomeInvitationBePresentInAllPeopleTable(UserJson user) {
        open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader()
                .toFriendsPage()
                .clickAllPeopleTab()
                .searchFriend(user.testData().outcomeInvitations().getFirst().username())
                .checkStatusWaiting(user.testData().outcomeInvitations().getFirst().username());
    }
}
