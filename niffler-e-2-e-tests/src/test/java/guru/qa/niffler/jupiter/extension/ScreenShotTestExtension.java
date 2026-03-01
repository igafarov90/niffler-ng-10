package guru.qa.niffler.jupiter.extension;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.model.allure.ScreenDif;
import io.qameta.allure.Allure;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class ScreenShotTestExtension implements BeforeEachCallback, AfterEachCallback,
        ParameterResolver, TestExecutionExceptionHandler {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(ScreenShotTestExtension.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), ScreenShotTest.class)
                .ifPresent(anno -> {
                    context.getStore(NAMESPACE).put(context.getUniqueId(), anno.value());
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return
                AnnotationSupport.isAnnotated(extensionContext.getRequiredTestMethod(), ScreenShotTest.class) &&
                        parameterContext.getParameter().getType().isAssignableFrom(BufferedImage.class);
    }

    @SneakyThrows
    @Override
    public BufferedImage resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return ImageIO.read(new ClassPathResource(
                extensionContext.getRequiredTestMethod()
                        .getAnnotation(ScreenShotTest.class)
                        .value()
        ).getInputStream());
    }

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        BufferedImage expected = getExpected();
        BufferedImage actual = getActual();
        BufferedImage diff = getDiff();

        ScreenShotTest annotation = context.getRequiredTestMethod().getAnnotation(ScreenShotTest.class);

        ScreenDif screenDif = new ScreenDif(
                "data:image/png;base64," + Base64.getEncoder().encodeToString(imageToBytes(expected)),
                "data:image/png;base64," + Base64.getEncoder().encodeToString(imageToBytes(actual)),
                "data:image/png;base64," + Base64.getEncoder().encodeToString(imageToBytes(diff))
        );

        Allure.addAttachment(
                "Screenshot diff",
                "application/vnd.allure.image.diff",
                mapper.writeValueAsString(screenDif)
        );

        if (annotation != null && annotation.rewriteExpected()) {
            if (getActual() != null) {
                String projectRoot = System.getProperty("user.dir");
                boolean inRootProject = new File(projectRoot, "niffler-e-2-e-tests").exists();
                File output;

                if (inRootProject) {
                    output = new File(projectRoot + "/niffler-e-2-e-tests/src/test/resources/" + annotation.value());
                } else {
                    output = new File(projectRoot + "/src/test/resources/" + annotation.value());
                }
                ImageIO.write(getActual(), "png", output);
                System.out.println("Expected screenshot was rewritten: " + output.getAbsolutePath());

                throw new AssertionError(
                        "Expected screenshot was rewritten. Please re-run the test to verify the new baseline."
                );
            } else {
                System.out.println("Cannot rewrite expected screenshot: actual image is null");
            }
        }
        throw throwable;
    }

    public static void setExpected(BufferedImage expected) {
        TestMethodContextExtension.context().getStore(NAMESPACE).put("expected", expected);
    }

    public static void setActual(BufferedImage actual) {
        TestMethodContextExtension.context().getStore(NAMESPACE).put("actual", actual);
    }

    public static void setDiff(BufferedImage diff) {
        TestMethodContextExtension.context().getStore(NAMESPACE).put("diff", diff);
    }

    public static BufferedImage getExpected() {
        return TestMethodContextExtension.context().getStore(NAMESPACE).get("expected", BufferedImage.class);
    }

    public static BufferedImage getActual() {
        return TestMethodContextExtension.context().getStore(NAMESPACE).get("actual", BufferedImage.class);
    }

    public static BufferedImage getDiff() {
        return TestMethodContextExtension.context().getStore(NAMESPACE).get("diff", BufferedImage.class);
    }

    private static byte[] imageToBytes(BufferedImage image) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, ".png", outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afterEach(ExtensionContext context) {

    }
}
