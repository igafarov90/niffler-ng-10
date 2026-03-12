package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
import guru.qa.niffler.model.Bubble;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.CheckResult.accepted;
import static com.codeborne.selenide.CheckResult.rejected;

@ParametersAreNonnullByDefault
public class StatConditions {

    @Nonnull
    public static WebElementsCondition statBubbles(@Nonnull Bubble... expectedBubbles) {
        return new WebElementsCondition() {

            private final String expected = Arrays.stream(expectedBubbles)
                    .map(c -> c.color().rgb + "=" + c.text())
                    .toList()
                    .toString();

            @Nonnull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedBubbles)) {
                    throw new IllegalArgumentException("No expected colors given");
                }
                if (expectedBubbles.length != elements.size()) {
                    final String message = String.format("List size mismatch (expected: %s, actual: %s)",
                            expectedBubbles.length, elements.size());
                    return rejected(message, elements);
                }

                boolean passed = true;
                final Map<String, String> actualRgbaWithText = new HashMap<>();
                for (int i = 0; i < elements.size(); i++) {
                    final WebElement elementToCheck = elements.get(i);

                    final String colorToCheck = expectedBubbles[i].color().rgb;
                    final String textToCheck = expectedBubbles[i].text();
                    final String textElement = elementToCheck.getText();
                    final String rgbaElement = elementToCheck.getCssValue("background-color");
                    actualRgbaWithText.put(rgbaElement, textElement);

                    if (passed) {
                        passed = rgbaElement.equals(colorToCheck) && textToCheck.equals(textElement);
                    }
                }
                if (!passed) {
                    return getFailedResult(expected, actualRgbaWithText);
                }
                return accepted();
            }

            @Override
            public String toString() {
                return expected;
            }
        };
    }

    @Nonnull
    public static WebElementsCondition statBubblesInAnyOrder(@Nonnull Bubble... expectedBubbles) {
        return new WebElementsCondition() {

            private final String expected = Arrays.stream(expectedBubbles)
                    .map(c -> c.color().rgb + "=" + c.text())
                    .toList()
                    .toString();

            @Nonnull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedBubbles)) {
                    throw new IllegalArgumentException("No expected colors given");
                }

                if (expectedBubbles.length != elements.size()) {
                    final String message = String.format("List size mismatch (expected: %s, actual: %s)",
                            expectedBubbles.length, elements.size());
                    return rejected(message, elements);
                }

                Map<String, String> actualRgbaWithText = getActualElementsByMap(elements);
                boolean passed = true;
                for (Bubble expectedBubble : expectedBubbles) {
                    String actualText = actualRgbaWithText.get(expectedBubble.color().rgb);

                    if (actualText == null || !actualText.equals(expectedBubble.text())) {
                        passed = false;
                        break;
                    }
                }
                if (!passed) {
                    return getFailedResult(expected, actualRgbaWithText);
                }
                return accepted();
            }

            @Override
            public String toString() {
                return expected;
            }
        };

    }

    public static WebElementsCondition statBubblesContains(Bubble... expectedBubbles) {
        return new WebElementsCondition() {
            private final String expected = Arrays.stream(expectedBubbles)
                    .map(c -> c.color().rgb + "=" + c.text())
                    .toList()
                    .toString();

            @Override
            @Nonnull
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedBubbles)) {
                    throw new IllegalArgumentException("No expected colors given");
                }

                Map<String, String> actualRgbaWithText = getActualElementsByMap(elements);
                boolean passed = true;
                for (Bubble expectedBubble : expectedBubbles) {
                    String actualText = actualRgbaWithText.get(expectedBubble.color().rgb);

                    if (actualText == null || !actualText.equals(expectedBubble.text())) {
                        passed = false;
                        break;
                    }
                }
                if (!passed) {
                    return getFailedResult(expected, actualRgbaWithText);
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
    private static CheckResult getFailedResult(String expected, Map<String, String> actualRgbaWithText) {
        final String actualRgba = actualRgbaWithText.toString();
        final String message = String.format("List colors mismatch (expected: %s, actual: %s)", expected, actualRgba);
        return rejected(message, actualRgba);
    }

    @NotNull
    private static Map<String, String> getActualElementsByMap(List<WebElement> elements) {
        Map<String, String> actualRgbaListWithText = new HashMap<>();
        for (final WebElement elementToCheck : elements) {
            final String textElement = elementToCheck.getText();
            final String rgbaElement = elementToCheck.getCssValue("background-color");
            actualRgbaListWithText.put(rgbaElement, textElement);
        }
        return actualRgbaListWithText;
    }
}
