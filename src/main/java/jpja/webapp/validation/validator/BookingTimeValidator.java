package jpja.webapp.validation.validator;

import java.time.LocalTime;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jpja.webapp.validation.annotation.ValidBookingTime;

/**
 * Validator for the {@link ValidBookingTime} annotation.
 * 
 * <p>
 * This class ensures that a given booking time is valid by checking if it falls
 * within
 * the allowed time range, as specified by the {@link ValidBookingTime#start()}
 * and {@link ValidBookingTime#end()} parameters.
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
 * @ValidBookingTime(start = "09:00", end = "17:00", message = "Booking time must be between 9 AM and 5 PM.")
 * private LocalTime bookingTime;
 * }
 * </pre>
 * 
 * @author James Prial
 */
public class BookingTimeValidator implements ConstraintValidator<ValidBookingTime, LocalTime> {

    private LocalTime startTime;
    private LocalTime endTime;

    /**
     * Initializes the validator with parameters from the {@link ValidBookingTime}
     * annotation.
     * 
     * @param annotation the annotation instance containing configuration values
     */
    @Override
    public void initialize(ValidBookingTime annotation) {
        this.startTime = LocalTime.parse(annotation.start());
        this.endTime = LocalTime.parse(annotation.end());
    }

    /**
     * Validates the given {@link LocalTime} to ensure it falls within the allowed
     * time range.
     * 
     * <p>
     * If the time is {@code null}, it is considered valid, leaving null checks to
     * {@code @NotNull}.
     * </p>
     * 
     * @param bookingTime the time to validate
     * @param context     the validation context
     * @return {@code true} if the time is valid or {@code null}, {@code false}
     *         otherwise
     */
    @Override
    public boolean isValid(LocalTime bookingTime, ConstraintValidatorContext context) {
        if (bookingTime == null) {
            // Let other annotations like @NotNull handle null
            return true;
        }
        // Check if bookingTime is within the allowed window (startTime <= bookingTime
        // <= endTime)
        return !bookingTime.isBefore(startTime) && !bookingTime.isAfter(endTime);
    }
}
