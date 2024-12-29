package jpja.webapp.validation.validator;

import java.util.Set;

import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jpja.webapp.model.dto.ModifierDTO;
import jpja.webapp.service.BookingService;
import jpja.webapp.validation.annotation.ValidModifierId;

/**
 * Validator for the {@link ValidModifierId} annotation.
 * 
 * <p>
 * This class ensures that a given modifier ID is valid by checking if it exists
 * within the
 * set of allowed modifiers of the specified type, as retrieved from the
 * {@link BookingService}.
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
 * @ValidModifierId(type = "TYPE", message = "Invalid modifier ID for type TYPE")
 * private Long modifierId;
 * }
 * </pre>
 * 
 * @author James Prial
 */
@Component
public class ModifierIdValidator implements ConstraintValidator<ValidModifierId, Long> {

    private String type;

    private final BookingService bookingService;

    /**
     * Constructor for injecting required services.
     * 
     * @param bookingService the service used to retrieve valid modifiers
     */
    public ModifierIdValidator(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Initializes the validator with parameters from the {@link ValidModifierId}
     * annotation.
     * 
     * @param annotation the annotation instance containing configuration values
     */
    @Override
    public void initialize(ValidModifierId annotation) {
        this.type = annotation.type();
    }

    /**
     * Validates the given modifier ID to ensure it exists within the set of allowed
     * modifiers
     * for the specified type.
     * 
     * <p>
     * If the modifier ID is {@code null}, it is considered valid, leaving null
     * checks to {@code @NotNull}.
     * </p>
     * 
     * @param modifierId the ID to validate
     * @param context    the validation context
     * @return {@code true} if the modifier ID is valid or {@code null},
     *         {@code false} otherwise
     */
    @Override
    public boolean isValid(Long modifierId, ConstraintValidatorContext context) {
        if (modifierId == null) {
            return true; // Let @NotNull handle null
        }
        Set<ModifierDTO> modifiers = bookingService.getModifiersByType(type);
        for (ModifierDTO mod : modifiers) {
            if (mod.getId().equals(modifierId)) {
                return true;
            }
        }
        return false;
    }
}
