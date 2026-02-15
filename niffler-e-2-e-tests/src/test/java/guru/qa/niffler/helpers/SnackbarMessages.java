package guru.qa.niffler.helpers;

public class SnackbarMessages {
    public static final String FRIEND_REMOVED = "Friend %s is deleted";
    public static final String FRIEND_ACCEPTED = "Invitation of %s accepted";
    public static final String FRIEND_DECLINED = "Invitation of %s is declined";
    public static final String ADDED_CATEGORY = "You've added new category: %s";
    public static final String PROFILE_UPDATED = "Profile successfully updated";
    public static final String SPENDING_CREATED = "New spending is successfully created";
    public static final String SPENDING_EDITED = "Spending is edited successfully";

    public static String friendRemoved(String username) {
        return String.format(FRIEND_REMOVED, username);
    }

    public static String friendAccepted(String username) {
        return String.format(FRIEND_ACCEPTED, username);
    }

    public static String friendDeclined(String username) {
        return String.format(FRIEND_DECLINED, username);
    }

    public static String addedCategory(String categoryName) {
        return String.format(ADDED_CATEGORY, categoryName);
    }
}
