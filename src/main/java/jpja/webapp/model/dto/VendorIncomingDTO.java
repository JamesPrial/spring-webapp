package jpja.webapp.model.dto;

/**
 * Data Transfer Object (DTO) for incoming vendor data.
 * This class is used to capture and validate vendor input during creation or updates.
 * Extends {@link UserIncomingDTO} to include vendor-specific attributes such as
 * name, phone number, business size, and maximum square footage capacity.
 * 
 * <p>Primarily intended for handling vendor-related requests where input data
 * is required to be validated and processed.</p>
 * 
 * @author James Prial
 */
public class VendorIncomingDTO extends UserIncomingDTO implements VendorDTOInterface {

    /**
     * The name of the vendor.
     */
    private String name;

    /**
     * The phone number of the vendor.
     */
    private String phone;

    /**
     * The size of the vendor's business (e.g., number of employees).
     */
    private Integer size;

    /**
     * The maximum square footage capacity the vendor can handle.
     */
    private Long max_sqft;

    /**
     * Default constructor.
     * Initializes all fields to {@code null}.
     */
    public VendorIncomingDTO() {
        super();
        this.name = null;
        this.phone = null;
        this.size = null;
        this.max_sqft = null;
    }

    /**
     * Gets the name of the vendor.
     *
     * @return the vendor's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the vendor.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the phone number of the vendor.
     *
     * @return the vendor's phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone number of the vendor.
     *
     * @param phone the phone number to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets the size of the vendor's business.
     *
     * @return the business size
     */
    public Integer getSize() {
        return size;
    }

    /**
     * Sets the size of the vendor's business.
     *
     * @param size the size to set
     */
    public void setSize(Integer size) {
        this.size = size;
    }

    /**
     * Gets the maximum square footage capacity the vendor can handle.
     *
     * @return the maximum square footage
     */
    public Long getMax_sqft() {
        return max_sqft;
    }

    /**
     * Sets the maximum square footage capacity the vendor can handle.
     *
     * @param max_sqft the maximum square footage to set
     */
    public void setMax_sqft(Long max_sqft) {
        this.max_sqft = max_sqft;
    }
}
