package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.service.SpendClient;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoryExtension implements BeforeEachCallback,
        AfterTestExecutionCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);
    private final SpendClient spendClient = new SpendDbClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                User.class
        ).ifPresent(userAnnotation -> {
            Category[] categories = userAnnotation.categories();
            if (categories.length > 0) {

                Category categoryAnnotation = categories[0];
                CategoryJson created = spendClient.createCategory(
                        new CategoryJson(
                                null,
                                RandomDataUtils.randomCategoryName(),
                                userAnnotation.username(),
                                categoryAnnotation.archived())
                );
                context.getStore(NAMESPACE)
                        .put(context.getUniqueId(), created);
            }
        });
    }


    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return parameterContext.getParameter()
                .getType()
                .isAssignableFrom(CategoryJson.class);
    }

    @Override
    public CategoryJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), CategoryJson.class);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        CategoryJson category = context.getStore(NAMESPACE)
                .get(context.getUniqueId(), CategoryJson.class);
        if (category != null)
            spendClient.removeCategory(category);
    }
}
