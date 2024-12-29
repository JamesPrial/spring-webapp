package jpja.webapp.validation.annotation;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jpja.webapp.validation.validator.BookingTimeValidator;

/**
 * Custom validation annotation to validate that a booking time falls within
 * a specified range of start and end times.
 * 
 * <p>
 * This annotation can be applied to fields, methods, parameters, and type use
 * to ensure
 * that the provided booking time is valid based on the defined range.
 * </p>
 * 
 * <p>
 * The validation logic is implemented in the {@link BookingTimeValidator}
 * class.
 * </p>
 * 
 * <p>
 * Usage Example:
 * </p>
 * 
 * <pre>
 * {@code
 * @ValidBookingTime(start = "06:00", end = "18:00")
 * private LocalTime bookingTime;
 * }
 * </pre>
 * 
 * @author James Prial
 */
@Documented
@Constraint(validatedBy = BookingTimeValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBookingTime {

    /**
     * Default error message to be returned if validation fails.
     * 
     * @return the error message
     */
    String message() default "Invalid booking time";

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
     * Specifies the start time of the valid booking range.
     * Default value is "08:00" (8:00 AM).
     * 
     * @return the start time of the valid range
     */
    String start() default "08:00";

    /**
     * Specifies the end time of the valid booking range.
     * Default value is "20:00" (8:00 PM).
     * 
     * @return the end time of the valid range
     */
    String end() default "20:00";
}
