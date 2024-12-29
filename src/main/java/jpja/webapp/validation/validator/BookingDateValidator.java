package jpja.webapp.validation.validator;

import java.time.LocalDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jpja.webapp.validation.annotation.ValidBookingDate;

/**
 * Validator for the {@link ValidBookingDate} annotation.
 * 
 * <p>
 * This class ensures that a given date is valid for booking by checking if it
 * is at least a certain
 * number of days in the future, as specified by the
 * {@link ValidBookingDate#days()} parameter.
 * </p>
 * 
 * <p>
 * Null values are considered valid to allow separate handling of nullability
 * using {@code @NotNull}.
 * </p>
 * 
 * <p>
 * Example Usage:
 * </p>
 * 
 * <pre>
 * {@code
 * @ValidBookingDate(days = 7, message = "Booking date must be at least 7 days in the future.")
 * private LocalDate bookingDate;
 * }
 * </pre>
 * 
 * @author James Prial
 */
public class BookingDateValidator implements ConstraintValidator<ValidBookingDate, LocalDate> {

    private int days;

    /**
     * Initializes the validator with parameters from the {@link ValidBookingDate}
     * annotation.
     * 
     * @param annotation the annotation instance containing configuration values
     */
    @Override
    public void initialize(ValidBookingDate annotation) {
        // Read 'days' from the annotation
        this.days = annotation.days();
    }

    /**
     * Validates the given {@link LocalDate} to ensure it is at least the required
     * number of days in the future.
     * 
     * <p>
     * If the date is {@code null}, it is considered valid, leaving null checks to
     * {@code @NotNull}.
     * </p>
     * 
     * @param date    the date to validate
     * @param context the validation context
     * @return {@code true} if the date is valid or {@code null}, {@code false}
     *         otherwise
     */
    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (date == null) {
            // Allow @NotNull or other annotations to handle null
            return true;
        }

        LocalDate now = LocalDate.now();
        LocalDate minDate = now.plusDays(days);

        // Must be at least 'days' in the future
        return date.isAfter(minDate) || date.isEqual(minDate);
    }
}
