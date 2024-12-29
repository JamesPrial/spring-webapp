package jpja.webapp.model.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import jpja.webapp.exceptions.database.ConflictingModifiersException;

/**
 * Entity representing a booking.
 * This class maps to the "bookings" table in the database and includes fields
 * for customer, vendor, location, booking date and time, price, and modifiers.
 * 
 * <p>Each booking is associated with a customer and optionally a vendor. 
 * The modifiers allow for flexible tagging and categorization of bookings.</p>
 * 
 * @author James Prial
 */
@Entity
@Table(name = "bookings")
public class Booking {

    /**
     * The unique identifier for the booking.
     */
    @Id
    @Column(columnDefinition = "int(10) unsigned")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * The customer associated with the booking.
     * Represented as a many-to-one relationship.
     */
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    /**
     * The vendor associated with the booking (if assigned).
     * Represented as a many-to-one relationship.
     */
    @ManyToOne
    @JoinColumn(name = "vendor_id", nullable = true)
    private Vendor vendor;

    /**
     * The address where the booking takes place.
     * Represented as a many-to-one relationship.
     */
    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Address location;

    /**
     * The timestamp when the booking was created.
     * Automatically generated and not updatable.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * The date of the booking.
     */
    @Column(name = "booking_date", nullable = true)
    private LocalDate bookingDate;

    /**
     * The time of the booking.
     */
    @Column(name = "booking_time", nullable = true)
    private LocalTime bookingTime;

    /**
     * The price of the booking.
     */
    @Column(name = "price", nullable = true)
    private Double price;

    /**
     * The set of modifiers associated with the booking.
     * Represented as a many-to-many relationship.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "booking_modifier_join",
        joinColumns = @JoinColumn(name = "booking_id"),
        inverseJoinColumns = @JoinColumn(name = "modifier_id")
    )
    private Set<BookingModifier> modifiers;

    /**
     * Default constructor.
     * Initializes fields to default values and the modifiers set to an empty set.
     */
    public Booking() {
        this.customer = null;
        this.vendor = null;
        this.location = null;
        this.createdAt = null;
        this.bookingDate = null;
        this.bookingTime = null;
        this.price = null;
        this.modifiers = new HashSet<>();
    }

    /**
     * Constructs a booking with the specified details.
     *
     * @param customer the customer associated with the booking
     * @param vendor the vendor associated with the booking (optional)
     * @param location the location of the booking
     * @param createdAt the creation timestamp
     * @param bookingDate the date of the booking
     * @param bookingTime the time of the booking
     * @param price the price of the booking
     * @param modifiers the set of modifiers associated with the booking
     */
    public Booking(Customer customer, Vendor vendor, Address location, LocalDateTime createdAt,
                   LocalDate bookingDate, LocalTime bookingTime, double price, Set<BookingModifier> modifiers) {
        this.customer = customer;
        this.vendor = vendor;
        this.location = location;
        this.createdAt = createdAt;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.price = price;
        this.modifiers = modifiers;
    }

    // Getters and setters for all fields
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public User getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Address getLocation() {
        return location;
    }

    public void setLocation(Address location) {
        this.location = location;
    }

    public Set<BookingModifier> getModifiers() {
        return modifiers;
    }

    public void setModifiers(Set<BookingModifier> modifiers) {
        this.modifiers = modifiers;
    }

    // Additional utility methods
    /**
     * Gets modifiers of a specific type.
     *
     * @param type the type of modifier to filter by
     * @return a set of modifiers of the specified type
     */
    public Set<BookingModifier> getModifiersByType(String type) {
        Set<BookingModifier> result = new HashSet<>();
        for (BookingModifier modifier : modifiers) {
            if (type.equals(modifier.getType())) {
                result.add(modifier);
            }
        }
        return result;
    }

    /**
     * Adds a modifier to the booking.
     *
     * @param modifier the modifier to add
     * @return {@code true} if the modifier was added, {@code false} if it already exists
     */
    public boolean addModifier(BookingModifier modifier) {
        return modifiers.add(modifier);
    }

    /**
     * Removes a modifier from the booking.
     *
     * @param modifier the modifier to remove
     * @return {@code true} if the modifier was removed, {@code false} if it did not exist
     */
    public boolean removeModifier(BookingModifier modifier) {
        return modifiers.remove(modifier);
    }

    /**
     * Gets the status of the booking.
     * Ensures that there is only one status modifier; otherwise, throws an exception.
     *
     * @return the status modifier
     * @throws ConflictingModifiersException if there are multiple status modifiers
     */
    public BookingModifier getStatus() {
        Set<BookingModifier> statuses = getModifiersByType("STATUS");
        if (statuses.size() > 1) {
            throw new ConflictingModifiersException();
        }
        return statuses.iterator().next();
    }

    /**
     * Checks if the booking is marked as completed.
     *
     * @return {@code true} if the booking is completed, {@code false} otherwise
     */
    public boolean isCompleted() {
        BookingModifier status = getStatus();
        return "COMPLETED".equals(status.getName());
    }

    // hashCode, equals, and toString methods
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Booking other = (Booking) obj;
        return id == other.id;
    }
}
