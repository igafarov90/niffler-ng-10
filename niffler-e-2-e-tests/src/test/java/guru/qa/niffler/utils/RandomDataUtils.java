package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class RandomDataUtils {

    private RandomDataUtils() {
    }

    private static final Faker faker = new Faker();

    @Nonnull
    public static String randomUsername() {
        return faker.name().username();
    }

    @Nonnull
    public static String randomName() {
        return faker.name().firstName();
    }

    @Nonnull
    public static String randomSurname() {
        return faker.name().lastName();
    }

    @Nonnull
    public static Double randomAmount() {
        String digits = faker.number().digits(4);
        return Double.parseDouble(digits);
    }

    @Nonnull
    public static String randomCategoryName() {
        return faker.esports().game();
    }

    @Nonnull
    public static String randomSentence(int wordsCount) {
        return faker.lorem().sentence(wordsCount);
    }

    @Nonnull
    public static String randomPassword(int min, int max) {
        return faker.internet().password(min, max);
    }
}
