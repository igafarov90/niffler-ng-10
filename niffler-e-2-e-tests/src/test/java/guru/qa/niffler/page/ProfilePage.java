package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.utils.ScreenDiffResult;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ParametersAreNonnullByDefault
public class ProfilePage extends BasePage<ProfilePage> {
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement nameInput = $("#name");
    private final SelenideElement categoryInput = $("#category");
    private final SelenideElement toggle = $(".MuiSwitch-input");
    private final SelenideElement saveChangesBtn = $("button[type='submit']");
    private final ElementsCollection categoryChip = $$(".MuiChip-root");
    private final SelenideElement submitArchive = $$("button.MuiButtonBase-root").findBy(text("Archive"));
    private final SelenideElement uploadNewPictureInput = $("input[type='file']");


    private SelenideElement archiveCategoryButton(String category) {
        return getCategoryElement(category).find("[aria-label=\"Archive category\"]");
    }

    private SelenideElement getCategoryElement(String category) {
        return $$("span[class*='MuiChip-label']")
                .findBy(text(category))
                .parent().parent();
    }

    @Nonnull
    @Step("Добавить новую категорию: {categoryName}")
    public ProfilePage addCategory(String category) {
        categoryInput.setValue(category).pressEnter();
        return this;
    }

    @Nonnull
    @Step("Ввести имя новой категории: {categoryName}")
    public ProfilePage setNewName(String name) {
        nameInput.clear();
        nameInput.val(name);
        return this;
    }

    @Nonnull
    @Step("Сохранить изменения в профиле")
    public ProfilePage saveChanges() {
        saveChangesBtn.click();
        return this;
    }

    @Nonnull
    @Step("Проверить наличие категории в хлебных крошках: {categoryName}")
    public ProfilePage checkCategoryExists(String category) {
        categoryChip.find(text(category)).shouldBe(visible);
        return this;
    }

    @Nonnull
    @Step("Включить отображение архивных категорий")
    public ProfilePage switchToggle() {
        toggle.click();
        return this;
    }

    @Step("Архивировать категорию: {category}")
    public ProfilePage archiveCategory(String category) {
        archiveCategoryButton(category).click();
        submitArchive.click();
        return this;
    }

    @Step("Вернуться на главную страницу")
    public MainPage returnToMainPage() {
        return header.toMainPage();
    }

    @Step("Загрузка изображения профиля")
    public ProfilePage uploadProfileImage(String path) {
        uploadNewPictureInput.uploadFromClasspath(path);
        saveChangesBtn.click();
        return this;
    }

    @Step("Убедиться в добавлении фото для профиля")
    public void assertProfileIconIsAdded(BufferedImage expected) throws IOException {
        BufferedImage actual = ImageIO.read($$(".MuiAvatar-root").get(1).$("img").screenshot());
        assertTrue(new ScreenDiffResult(actual, expected));
    }

    @Step("Перезапись аватара в случае смены стилей")
    public void rewriteProfileIcon(BufferedImage expected) throws IOException {
        BufferedImage actual = ImageIO.read($$(".MuiAvatar-root").get(1).screenshot());
        assertFalse(new ScreenDiffResult(actual, expected));
    }
}

