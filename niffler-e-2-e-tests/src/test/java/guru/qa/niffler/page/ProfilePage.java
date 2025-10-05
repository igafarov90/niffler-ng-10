package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProfilePage {
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement nameInput = $("#name");
    private final SelenideElement categoryInput = $("#category");
    private final SelenideElement toggle = $(".MuiSwitch-input");
    private final SelenideElement saveChangesBtn = $("button[type='submit']");
    private final SelenideElement header = $("h2");
    private final ElementsCollection categoryChip = $$(".MuiChip-root");

    public ProfilePage addCategory(String category) {
        categoryInput.setValue(category).pressEnter();
        return this;
    }

    public ProfilePage setNewName(String name) {
        nameInput.clear();
        nameInput.val(name);
        return this;
    }

    public ProfilePage saveChanges() {
        saveChangesBtn.click();
        return this;
    }

    public ProfilePage checkPageHeader(String value){
        header.shouldHave(text(value));
        return this;
    }

    public ProfilePage checkCategoryExists(String category) {
        categoryChip.find(text(category)).shouldBe(visible);
        return this;
    }

    public ProfilePage switchToggle(){
        toggle.click();
        return this;
    }
}
