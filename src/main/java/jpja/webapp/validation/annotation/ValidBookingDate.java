package jpja.webapp.validation.annotation;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jpja.webapp.validation.validator.BookingDateValidator;

/**
 * Custom validation annotation to validate that a booking date is at least a
 * specified
 * number of days in the future.
 * 
 * <p>
 * This annotation can be applied to fields, methods, parameters, and type use
 * to ensure
 * that the provided booking date adheres to the defined criteria.
 * </p>
 * 
 * <p>
 * The validation logic is implemented in the {@link BookingDateValidator}
 * class.
 * </p>
 * 
 * <p>
 * Usage Example:
 * </p>
 * 
 * <pre>
 * {@code
 * @ValidBookingDate(days = 7)
 * private LocalDate bookingDate;
 * }
 * </pre>
 * 
 * @author James Prial
 */
@Documented
@Constraint(validatedBy = BookingDateValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBookingDate {

    /**
     * Default error message to be returned if validation fails.
     * 
     * <p>
     * The message can include the {@code {days}} placeholder, which will be
     * replaced with
     * the actual number of days specified in the annotation.
     * </p>
     * 
     * @return the error message
     */
    String message() default "Must be at least {days} days in the future";

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
     * Specifies the minimum number of days in the future the booking date must be.
     * 
     * @return the minimum number of days in the future
     */
    int days() default 1;
}
