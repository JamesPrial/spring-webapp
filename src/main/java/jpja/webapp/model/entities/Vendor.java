package jpja.webapp.model.entities;

import java.util.Set;

import jakarta.persistence.*;

/**
 * Entity representing a vendor.
 * A vendor is a type of user that provides services and is associated with multiple bookings.
 * This class extends the {@link User} class to include vendor-specific attributes,
 * such as business name, phone number, team size, and maximum square footage capacity.
 * 
 * <p>This entity maps to the "vendor_info" table in the database and maintains a
 * one-to-many relationship with the {@link Booking} entity.</p>
 * 
 * @author James Prial
 */
@Entity
@Table(name = "vendor_info")
public class Vendor extends User {

    /**
     * The business name of the vendor.
     * This field is unique and optional.
     */
    @Column(nullable = true, unique = true)
    private String name;

    /**
     * The phone number of the vendor.
     * This field is unique, optional, and limited to 15 characters.
     */
    @Column(columnDefinition = "VARCHAR(15)", nullable = true, unique = true)
    private String phone;

    /**
     * The team size of the vendor, represented as a tiny integer.
     */
    @Column(columnDefinition = "TINYINT", nullable = true)
    private Integer size;

    /**
     * The maximum square footage the vendor can handle, represented as an unsigned integer.
     */
    @Column(columnDefinition = "INT UNSIGNED", nullable = true)
    private Long max_sqft;

    /**
     * The set of bookings associated with the vendor.
     * Represented as a one-to-many relationship mapped by the "vendor" field in the {@link Booking} entity.
     */
    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL)
    private Set<Booking> vendorBookings;

    /**
     * Default constructor.
     * Initializes fields to default values.
     */
    public Vendor() {
        super();
        this.name = null;
        this.phone = null;
        this.size = null;
        this.max_sqft = null;
    }

    /**
     * Constructs a vendor with the specified email, username, password, and role.
     *
     * @param email the email of the vendor
     * @param username the username of the vendor
     * @param password the password of the vendor
     * @param role the role of the vendor
     */
    public Vendor(String email, String username, String password, Role role) {
        super(email, username, password, role);
        this.name = null;
        this.phone = null;
        this.size = null;
        this.max_sqft = null;
    }

    /**
     * Gets the business name of the vendor.
     *
     * @return the vendor's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the business name of the vendor.
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
     * Gets the team size of the vendor.
     *
     * @return the team size
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets the team size of the vendor.
     *
     * @param size the team size to set
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Gets the maximum square footage the vendor can handle.
     *
     * @return the maximum square footage
     */
    public Long getMax_sqft() {
        return max_sqft;
    }

    /**
     * Sets the maximum square footage the vendor can handle.
     *
     * @param max_sqft the maximum square footage to set
     */
    public void setMax_sqft(Long max_sqft) {
        this.max_sqft = max_sqft;
    }

    /**
     * Gets the set of bookings associated with the vendor.
     *
     * @return the set of vendor bookings
     */
    public Set<Booking> getVendorBookings() {
        return vendorBookings;
    }
}
