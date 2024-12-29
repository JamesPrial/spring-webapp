package jpja.webapp.model.dto;

/**
 * Interface representing a Data Transfer Object (DTO) for a vendor.
 * Extends {@link UserDTOInterface} to include vendor-specific attributes such as
 * name, phone number, company size, and maximum square footage capacity.
 * 
 * <p>Implementations of this interface are expected to encapsulate the data
 * required to represent and manipulate vendor information in the application.</p>
 * 
 * @author James Prial
 */
public interface VendorDTOInterface extends UserDTOInterface {

    /**
     * Gets the name of the vendor.
     *
     * @return the vendor name
     */
    String getName();

    /**
     * Gets the phone number of the vendor.
     *
     * @return the vendor phone number
     */
    String getPhone();

    /**
     * Gets the size of the vendor's business (e.g., number of employees).
     *
     * @return the vendor's size
     */
    Integer getSize();

    /**
     * Gets the maximum square footage capacity the vendor can handle.
     *
     * @return the maximum square footage
     */
    Long getMax_sqft();

    /**
     * Sets the name of the vendor.
     *
     * @param name the vendor name to set
     */
    void setName(String name);

    /**
     * Sets the phone number of the vendor.
     *
     * @param phone the vendor phone number to set
     */
    void setPhone(String phone);

    /**
     * Sets the size of the vendor's business.
     *
     * @param size the vendor's size to set
     */
    void setSize(Integer size);

    /**
     * Sets the maximum square footage capacity the vendor can handle.
     *
     * @param max_sqft the maximum square footage to set
     */
    void setMax_sqft(Long max_sqft);
}
