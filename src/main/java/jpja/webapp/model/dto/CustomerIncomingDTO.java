package jpja.webapp.model.dto;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Data Transfer Object (DTO) representing incoming customer data.
 * This class is used to capture and validate customer input during creation or
 * updates.
 * Extends {@link UserIncomingDTO} to include customer-specific fields such as
 * first name, last name, phone number, and addresses.
 * 
 * <p>
 * Validation annotations are applied to ensure the integrity of the input data.
 * </p>
 * 
 * @author James Prial
 */
public class CustomerIncomingDTO extends UserIncomingDTO implements CustomerDTOInterface {

    /**
     * The first name of the customer.
     * Must not be blank.
     */
    @NotBlank
    private String firstName;

    /**
     * The last name of the customer.
     * Must not be blank.
     */
    @NotBlank
    private String lastName;

    /**
     * The phone number of the customer.
     * Must be a valid 10-digit phone number matching the pattern
     * {@code ^[0-9]{10}$}.
     */
    @Pattern(regexp = "^[0-9]{10}$", message = "Must be a valid 10-digit phone number")
    private String phone;

    /**
     * A set of addresses associated with the customer.
     */
    private Set<String> addresses;

    /**
     * Default constructor.
     * Initializes all fields to {@code null}.
     */
    public CustomerIncomingDTO() {
        super();
        this.firstName = null;
        this.lastName = null;
        this.phone = null;
        this.addresses = null;
    }

    /**
     * Gets the first name of the customer.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the last name of the customer.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the phone number of the customer.
     *
     * @return the phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Gets the addresses associated with the customer.
     *
     * @return a set of addresses
     */
    public Set<String> getAddresses() {
        return addresses;
    }

    /**
     * Sets the first name of the customer.
     *
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets the last name of the customer.
     *
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Sets the phone number of the customer.
     *
     * @param phone the phone number to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Sets the addresses associated with the customer.
     *
     * @param addresses a set of addresses to associate with the customer
     */
    public void setAddresses(Set<String> addresses) {
        this.addresses = addresses;
    }
}
