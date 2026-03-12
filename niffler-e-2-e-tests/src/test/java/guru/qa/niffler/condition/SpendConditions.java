package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
import guru.qa.niffler.model.SpendJson;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.codeborne.selenide.CheckResult.accepted;
import static com.codeborne.selenide.CheckResult.rejected;

@ParametersAreNonnullByDefault
public class SpendConditions {

    @Nonnull
    public static WebElementsCondition spends(@Nonnull SpendJson... expectedSpends) {
        return new WebElementsCondition() {

            private final String expected = Arrays.stream(expectedSpends)
                    .map(s -> s.category().name() + "=" + s.amount() + " " + s.currency() + "|" + s.description() + "|" + formatDate(s.spendDate()))
                    .toList()
                    .toString();

            @Nonnull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedSpends)) {
                    throw new IllegalArgumentException("No expected spends given");
                }
                if (expectedSpends.length != elements.size()) {
                    final String message = String.format("List size mismatch (expected: %s, actual: %s)",
                            expectedSpends.length, elements.size());
                    return rejected(message, elements);
                }

                boolean passed = true;
                final Map<String, Map<String, String>> actualData = new HashMap<>();
                for (int i = 0; i < elements.size(); i++) {
                    final WebElement row = elements.get(i);
                    final SpendJson expectedSpend = expectedSpends[i];

                    final List<WebElement> cells = row.findElements(By.cssSelector("td"));
                    if (cells.size() < 4) {
                        return rejected("Row " + i + " has insufficient cells", elements);
                    }

                    final String categoryText = cells.get(1).getText().trim();
                    final String amountText = cells.get(2).getText().trim();
                    final String descriptionText = cells.get(3).getText().trim();
                    final String dateText = cells.get(4).getText().trim();

                    Map<String, String> rowData = new HashMap<>();
                    rowData.put("category", categoryText);
                    rowData.put("amount", amountText);
                    rowData.put("description", descriptionText);
                    rowData.put("date", dateText);
                    actualData.put("row" + i, rowData);

                    final String expectedAmount = String.format("%.0f ₽", expectedSpend.amount());
                    final String expectedDate = formatDate(expectedSpend.spendDate());

                    if (passed) {
                        passed = categoryText.equals(expectedSpend.category().name())
                                && amountText.equals(expectedAmount)
                                && descriptionText.equals(expectedSpend.description() != null ? expectedSpend.description() : "")
                                && dateText.equals(expectedDate);
                    }
                }

                if (!passed) {
                    return getFailedResult(expected, actualData);
                }
                return accepted();
            }

            @Override
            public String toString() {
                return expected;
            }
        };
    }

    @NotNull
    private static CheckResult getFailedResult(String expected, Map<String, Map<String, String>> actualData) {
        final String actual = actualData.values().stream()
                .map(m -> m.get("category") + "=" + m.get("amount") + "|" + m.get("description") + "|" + m.get("date"))
                .collect(Collectors.toList())
                .toString();
        final String message = String.format("Spends mismatch (expected: %s, actual: %s)", expected, actual);
        return rejected(message, actualData);
    }

    private static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        return sdf.format(date);
    }
}
