package jpja.webapp.validation.annotation;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jpja.webapp.validation.validator.AddressIdValidator;

/**
 * Custom validation annotation to validate that an address ID is valid and
 * belongs to the user.
 * 
 * <p>
 * This annotation can be applied to fields, methods, parameters, and type use
 * to ensure
 * that the provided address ID meets the defined validation criteria.
 * </p>
 * 
 * <p>
 * The validation logic is implemented in the {@link AddressIdValidator} class.
 * </p>
 * 
 * <p>
 * Usage Example:
 * </p>
 * 
 * <pre>
 * {@code
 * @ValidAddressId
 * private Long addressId;
 * }
 * </pre>
 * 
 * @author James Prial
 */
@Documented
@Constraint(validatedBy = AddressIdValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAddressId {

    /**
     * Default error message to be returned if validation fails.
     * 
     * @return the error message
     */
    String message() default "Invalid address or address does not belong to the user";

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
}
