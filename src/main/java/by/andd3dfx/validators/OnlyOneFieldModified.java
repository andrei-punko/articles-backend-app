package by.andd3dfx.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OnlyOneFieldModifiedValidator.class)
public @interface OnlyOneFieldModified {

    String message() default "{Only one field should be modified at once}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] fields() default {};
}
