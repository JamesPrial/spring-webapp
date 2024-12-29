package jpja.webapp.model.dto;

import java.util.Set;

/**
 * Interface representing a Data Transfer Object (DTO) for a customer.
 * Extends {@link UserDTOInterface} to include customer-specific attributes such
 * as
 * first name, last name, phone number, and associated addresses.
 * 
 * <p>
 * Implementations of this interface are expected to encapsulate the data
 * required to represent and manipulate customer information in the application.
 * 
 * @author James Prial
 */
public interface CustomerDTOInterface extends UserDTOInterface {

    /**
     * Gets the first name of the customer.
     *
     * @return the first name
     */
    String getFirstName();

    /**
     * Gets the last name of the customer.
     *
     * @return the last name
     */
    String getLastName();

    /**
     * Gets the phone number of the customer.
     *
     * @return the phone number
     */
    String getPhone();

    /**
     * Gets the addresses associated with the customer.
     *
     * @return a set of address identifiers
     */
    Set<String> getAddresses();

    /**
     * Sets the first name of the customer.
     *
     * @param name the first name to set
     */
    void setFirstName(String name);

    /**
     * Sets the last name of the customer.
     *
     * @param name the last name to set
     */
    void setLastName(String name);

    /**
     * Sets the phone number of the customer.
     *
     * @param phone the phone number to set
     */
    void setPhone(String phone);

    /**
     * Sets the addresses associated with the customer.
     *
     * @param addresses a set of address identifiers to associate with the customer
     */
    void setAddresses(Set<String> addresses);
}
