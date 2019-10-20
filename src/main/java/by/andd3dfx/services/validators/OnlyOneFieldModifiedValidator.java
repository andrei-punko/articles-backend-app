package by.andd3dfx.services.validators;

import by.andd3dfx.dto.ArticleDto;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OnlyOneFieldModifiedValidator implements ConstraintValidator<OnlyOneFieldModified, ArticleDto> {

    private List<String> fieldNames = new ArrayList<>();

    @Override
    public void initialize(OnlyOneFieldModified constraintAnnotation) {
        this.fieldNames = Arrays.asList(constraintAnnotation.fields());
    }

    @Override
    public boolean isValid(ArticleDto article, ConstraintValidatorContext context) {
        int count = 0;
        for (String fieldName : fieldNames) {
            try {
                final Field field = ArticleDto.class.getDeclaredField(fieldName);
                final boolean isAccessible = field.isAccessible();
                field.setAccessible(true);
                final Object o = field.get(article);
                if (o != null) {
                    count++;
                }
                field.setAccessible(isAccessible);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return count == 1;
    }
}
