package guru.qa.niffler.helpers;

public class ErrorMessages {
    private static final String USERNAME_EXISTS_ERROR = "Username `%s` already exists";
    private static final String PASSWORD_MATCH_ERROR = "Passwords should be equal";
    private static final String BAD_CREDENTIALS_ERROR = "Неверные учетные данные пользователя";

    public static String usernameAlreadyExists(String username) {
        return String.format(USERNAME_EXISTS_ERROR, username);
    }

    public static String getPasswordMatchError() {
        return PASSWORD_MATCH_ERROR;
    }

    public static String getBadCredentialsError(){
        return BAD_CREDENTIALS_ERROR;
    }
}
