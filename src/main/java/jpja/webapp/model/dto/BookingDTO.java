package jpja.webapp.model.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

/**
 * Data Transfer Object (DTO) for representing booking information.
 * This class encapsulates the details of a booking, including identifiers
 * for the customer and vendor, booking time and date, location, price, status,
 * and any additional modifiers.
 * 
 * <p>
 * Provides methods for accessing and modifying booking data, along with
 * implementations for {@code equals()} and {@code hashCode()} for comparison
 * purposes.
 * 
 * @author James Prial
 */
public class BookingDTO implements DTOInterface {

    /**
     * The unique identifier for the booking.
     */
    private Long id;

    /**
     * The identifier for the customer associated with the booking.
     */
    private String customerIdentifier;

    /**
     * The identifier for the vendor associated with the booking.
     */
    private String vendorIdentifier;

    /**
     * The location of the booking, represented as an {@link AddressDTO}.
     */
    private AddressDTO location;

    /**
     * The timestamp when the booking was created.
     */
    private LocalDateTime createdAt;

    /**
     * The date of the booking.
     */
    private LocalDate bookingDate;

    /**
     * The time of the booking.
     */
    private LocalTime bookingTime;

    /**
     * The price of the booking.
     */
    private Double price;

    /**
     * A set of modifiers associated with the booking, such as additional services
     * or options.
     */
    private Set<String> modifiers;

    /**
     * The current status of the booking (e.g., "PENDING", "CONFIRMED").
     */
    private String status;

    /**
     * Default constructor.
     * Initializes all fields to {@code null}.
     */
    public BookingDTO() {
        this.id = null;
        this.customerIdentifier = null;
        this.vendorIdentifier = null;
        this.location = null;
        this.createdAt = null;
        this.bookingDate = null;
        this.bookingTime = null;
        this.price = null;
        this.modifiers = null;
        this.status = null;
    }

    /**
     * Gets the unique identifier of the booking.
     *
     * @return the booking ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the booking.
     *
     * @param id the booking ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the customer identifier.
     *
     * @return the customer identifier
     */
    public String getCustomerIdentifier() {
        return customerIdentifier;
    }

    /**
     * Sets the customer identifier.
     *
     * @param customerIdentifier the customer identifier to set
     */
    public void setCustomerIdentifier(String customerIdentifier) {
        this.customerIdentifier = customerIdentifier;
    }

    /**
     * Gets the vendor identifier.
     *
     * @return the vendor identifier
     */
    public String getVendorIdentifier() {
        return vendorIdentifier;
    }

    /**
     * Sets the vendor identifier.
     *
     * @param vendorIdentifier the vendor identifier to set
     */
    public void setVendorIdentifier(String vendorIdentifier) {
        this.vendorIdentifier = vendorIdentifier;
    }

    /**
     * Gets the location of the booking.
     *
     * @return the booking location
     */
    public AddressDTO getLocation() {
        return location;
    }

    /**
     * Sets the location of the booking.
     *
     * @param location the booking location to set
     */
    public void setLocation(AddressDTO location) {
        this.location = location;
    }

    /**
     * Gets the timestamp when the booking was created.
     *
     * @return the creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the timestamp when the booking was created.
     *
     * @param createdAt the creation timestamp to set
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the date of the booking.
     *
     * @return the booking date
     */
    public LocalDate getBookingDate() {
        return bookingDate;
    }

    /**
     * Sets the date of the booking.
     *
     * @param bookingDate the booking date to set
     */
    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    /**
     * Gets the time of the booking.
     *
     * @return the booking time
     */
    public LocalTime getBookingTime() {
        return bookingTime;
    }

    /**
     * Sets the time of the booking.
     *
     * @param bookingTime the booking time to set
     */
    public void setBookingTime(LocalTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    /**
     * Gets the price of the booking.
     *
     * @return the booking price
     */
    public Double getPrice() {
        return price;
    }

    /**
     * Sets the price of the booking.
     *
     * @param price the booking price to set
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * Gets the modifiers associated with the booking.
     *
     * @return the booking modifiers
     */
    public Set<String> getModifiers() {
        return modifiers;
    }

    /**
     * Sets the modifiers associated with the booking.
     *
     * @param modifiers the booking modifiers to set
     */
    public void setModifiers(Set<String> modifiers) {
        this.modifiers = modifiers;
    }

    /**
     * Gets the current status of the booking.
     *
     * @return the booking status
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * Sets the current status of the booking.
     *
     * @param status the booking status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Calculates the hash code for this booking, based on its unique identifier.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /**
     * Compares this booking to another object for equality based on their unique
     * identifiers.
     *
     * @param obj the object to compare with
     * @return {@code true} if the bookings are equal; {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BookingDTO other = (BookingDTO) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
