package jpja.webapp.model.dto;

import java.util.Set;

/**
 * Data Transfer Object (DTO) for outgoing customer data.
 * This class is used to represent customer information sent from the server
 * to the client, including first name, last name, phone number, and associated
 * addresses.
 * Extends {@link UserOutgoingDTO} to inherit common user-related attributes.
 * 
 * <p>
 * Primarily used for read-only operations where customer data needs to
 * be serialized and sent as a response.
 * </p>
 * 
 * @author James Prial
 */
public class CustomerOutgoingDTO extends UserOutgoingDTO implements CustomerDTOInterface {

    /**
     * The first name of the customer.
     */
    private String firstName;

    /**
     * The last name of the customer.
     */
    private String lastName;

    /**
     * The phone number of the customer.
     */
    private String phone;

    /**
     * A set of addresses associated with the customer.
     */
    private Set<String> addresses;

    /**
     * Default constructor.
     * Initializes all fields to {@code null}.
     */
    public CustomerOutgoingDTO() {
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
     * Sets the first name of the customer.
     *
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
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
     * Sets the last name of the customer.
     *
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
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
     * Sets the phone number of the customer.
     *
     * @param phone the phone number to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
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
     * Sets the addresses associated with the customer.
     *
     * @param addresses a set of addresses to associate with the customer
     */
    public void setAddresses(Set<String> addresses) {
        this.addresses = addresses;
    }
}
