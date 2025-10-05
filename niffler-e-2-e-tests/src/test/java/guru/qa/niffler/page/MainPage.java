package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byTagAndText;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
  private final SelenideElement spendingTable = $("#spendings");
  private final SelenideElement statisticsComponent =  $(byTagAndText("h2", "Statistics"));
  private final SelenideElement historyOfSpendingsComponent =
          $(byTagAndText("h2", "History of Spendings"));
  private final SelenideElement profileIcon = $("[data-testid='PersonIcon']");
  private final SelenideElement profileItem = $("a[href='/profile']");

  public MainPage checkThatPageLoaded() {
    spendingTable.should(visible);
    return this;
  }
  public MainPage checkThatPageHaveComponentsStatisticsAndHistory() {
    statisticsComponent.should(visible);
    historyOfSpendingsComponent.should(visible);
    return this;
  }

  public EditSpendingPage editSpending(String description) {
    spendingTable.$$("tbody tr").find(text(description)).$$("td").get(5).click();
    return new EditSpendingPage();
  }

  public MainPage checkThatTableContains(String description) {
    spendingTable.$$("tbody tr").find(text(description)).should(visible);
    return this;
  }

  public ProfilePage openProfilePage(){
    profileIcon.click();
    profileItem.click();
    return new ProfilePage();
  }


}
