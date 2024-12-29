package jpja.webapp.model.dto;

/**
 * Data Transfer Object (DTO) for outgoing vendor data.
 * This class represents vendor information sent from the server to the client.
 * Extends {@link UserOutgoingDTO} to include vendor-specific attributes such as
 * name, phone number, business size, and maximum square footage capacity.
 * 
 * <p>Primarily intended for read-only operations where vendor data needs to be serialized 
 * and sent as part of a response.</p>
 * 
 * @author James Prial
 */
public class VendorOutgoingDTO extends UserOutgoingDTO implements VendorDTOInterface {

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
    public VendorOutgoingDTO() {
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
     * @param name the vendor's name to set
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
     * @param phone the vendor's phone number to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets the size of the vendor's business.
     *
     * @return the vendor's business size
     */
    public Integer getSize() {
        return size;
    }

    /**
     * Sets the size of the vendor's business.
     *
     * @param size the vendor's business size to set
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
