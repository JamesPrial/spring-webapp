package jpja.webapp.validation.annotation;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jpja.webapp.validation.validator.ModifierIdValidator;

/**
 * Custom validation annotation to validate that a modifier ID is valid and
 * matches a specified type.
 * 
 * <p>
 * This annotation can be applied to fields, methods, parameters, and type use
 * to ensure
 * that the provided modifier ID exists and corresponds to the given
 * {@code type}.
 * </p>
 * 
 * <p>
 * The validation logic is implemented in the {@link ModifierIdValidator} class.
 * </p>
 * 
 * <p>
 * Usage Example:
 * </p>
 * 
 * <pre>
 * {@code
 * @ValidModifierId(type = "STATUS")
 * private Long modifierId;
 * }
 * </pre>
 * 
 * @author James Prial
 */
@Documented
@Constraint(validatedBy = ModifierIdValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidModifierId {

    /**
     * Default error message to be returned if validation fails.
     * 
     * @return the error message
     */
    String message() default "Invalid modifier id";

    /**
     * Groups for which this constraint is applied.
     * 
     * @return the validation groups
     */
    Class<?>[] groups() default {};

    /**
     * Payload that can be used by clients of the Jakarta Bean Validation API to
     * assign custom metadata to a constraint.
     * 
     * @return the payload
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * Specifies the type of the {@code BookingModifier} to check against.
     * Default value is {@code "TYPE"}.
     * 
     * @return the modifier type
     */
    String type() default "TYPE";
}
