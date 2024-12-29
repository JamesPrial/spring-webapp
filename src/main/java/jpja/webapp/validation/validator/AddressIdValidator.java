package jpja.webapp.validation.validator;

import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jpja.webapp.model.entities.Customer;
import jpja.webapp.service.CustomerService;
import jpja.webapp.validation.annotation.ValidAddressId;

/**
 * Validator for the {@link ValidAddressId} annotation.
 * 
 * <p>
 * This class ensures that a given address ID is valid and belongs to the
 * currently authenticated customer.
 * The validation logic uses the {@link CustomerService} to retrieve the
 * currently authenticated customer and check
 * whether the address belongs to them.
 * </p>
 * 
 * <p>
 * Null address IDs are considered valid, allowing {@code @NotNull} to handle
 * null checks separately.
 * </p>
 * 
 * <p>
 * Example Usage:
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
@Component
public class AddressIdValidator implements ConstraintValidator<ValidAddressId, Long> {

    private final CustomerService customerService;

    /**
     * Constructs the validator with a {@link CustomerService} dependency.
     * 
     * @param customerService the service used to check the address ownership
     */
    public AddressIdValidator(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Validates that the provided address ID is valid and belongs to the current
     * customer.
     * 
     * <p>
     * If the address ID is {@code null}, it is considered valid, leaving null
     * checks to {@code @NotNull}.
     * </p>
     * 
     * @param addressId the address ID to validate
     * @param context   the validation context
     * @return {@code true} if the address ID is valid or {@code null},
     *         {@code false} otherwise
     */
    @Override
    public boolean isValid(Long addressId, ConstraintValidatorContext context) {
        if (addressId == null) {
            return true; // Let @NotNull handle null
        }

        Customer currentCustomer = customerService.getCurrentCustomer(); // e.g., from SecurityContextHolder

        // Check if the address belongs to the user
        return customerService.addressBelongsToUser(currentCustomer, addressId);
    }
}
