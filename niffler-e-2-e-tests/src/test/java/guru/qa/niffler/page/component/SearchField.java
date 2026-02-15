package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selectors.by;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class SearchField {

    private final SelenideElement self = $("form.MuiBox-root");
    private final SelenideElement searchInput = self.$(by("aria-label", "search"));
    private final SelenideElement clearBtn = self.$("#input-clear");

    @Nonnull
    @Step("Установить значение '{expectedText}' в поисковом поле")
    public SearchField search(String expectedText) {
        searchInput.setValue(expectedText).pressEnter();
        return this;
    }

    @Nonnull
    @Step("Очистить поисковое поле")
    public SearchField clearIfNotEmpty() {
        String currentValue = getValue();
        if (!currentValue.isEmpty()) {
            searchInput.clear();
        }
        return this;
    }

    public String getValue() {
        return searchInput.getValue();
    }
}
