package by.andd3dfx.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistingAuthorValidator.class)
public @interface ExistingAuthor {

    String message() default "{Unknown author}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
