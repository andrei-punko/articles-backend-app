package by.andd3dfx.services.validators;

import by.andd3dfx.dto.ArticleUpdateDto;
import java.lang.reflect.Field;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OnlyOneFieldModifiedValidator implements ConstraintValidator<OnlyOneFieldModified, ArticleUpdateDto> {

    @Override
    public boolean isValid(ArticleUpdateDto articleUpdateDto, ConstraintValidatorContext context) {
        int count = 0;
        ArticleUpdateDto.class.getDeclaredFields();
        for (Field field : ArticleUpdateDto.class.getDeclaredFields()) {
            try {
                final boolean isAccessible = field.canAccess(articleUpdateDto);
                field.setAccessible(true);
                final Object o = field.get(articleUpdateDto);
                if (o != null) {
                    count++;
                }
                field.setAccessible(isAccessible);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return count == 1;
    }
}
