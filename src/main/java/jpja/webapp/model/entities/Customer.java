package jpja.webapp.model.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

/**
 * Entity representing a customer.
 * This class extends the {@link User} class to include customer-specific attributes,
 * such as first name, last name, phone number, addresses, and bookings.
 * 
 * <p>This entity maps to the "customer_info" table in the database and manages 
 * one-to-many relationships with {@link Address} and {@link Booking} entities.</p>
 * 
 * @author James Prial
 */
@Entity
@Table(name = "customer_info")
public class Customer extends User {

    /**
     * The first name of the customer.
     */
    @Column(name = "first_name", nullable = true, unique = false)
    private String firstName;

    /**
     * The last name of the customer.
     */
    @Column(name = "last_name", nullable = true, unique = false)
    private String lastName;

    /**
     * The phone number of the customer.
     * This field is unique and limited to 15 characters.
     */
    @Column(columnDefinition = "VARCHAR(15)", nullable = true, unique = true)
    private String phone;

    /**
     * The set of addresses associated with the customer.
     * Represented as a one-to-many relationship.
     */
    @OneToMany
    @JoinTable(
        name = "address_join",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "address_id")
    )
    private Set<Address> addresses;

    /**
     * The set of bookings associated with the customer.
     * Represented as a one-to-many relationship mapped by the "customer" field in the {@link Booking} entity.
     */
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<Booking> customerBookings;

    /**
     * Default constructor.
     * Initializes fields to default values and initializes sets for addresses and bookings.
     */
    public Customer() {
        super();
        this.firstName = null;
        this.lastName = null;
        this.phone = null;
        this.addresses = new HashSet<>();
        this.customerBookings = new HashSet<>();
    }

    /**
     * Constructs a customer with the specified email, username, password, and role.
     *
     * @param email the email of the customer
     * @param username the username of the customer
     * @param password the password of the customer
     * @param role the role of the customer
     */
    public Customer(String email, String username, String password, Role role) {
        super(email, username, password, role);
        this.firstName = null;
        this.lastName = null;
        this.phone = null;
        this.addresses = new HashSet<>();
        this.customerBookings = new HashSet<>();
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
     * Gets the full name of the customer as "FirstName LastName".
     *
     * @return the full name
     */
    public String getName() {
        return this.firstName + " " + this.lastName;
    }

    /**
     * Sets the full name of the customer by splitting a single string into first and last names.
     *
     * @param name the full name to set
     * @throws IllegalArgumentException if the name format is invalid
     */
    public void setName(String name) {
        String[] names = name.split(" ");
        if (names.length > 2) {
            throw new IllegalArgumentException("Incorrectly formatted name");
        }
        this.firstName = names[0];
        if (names.length == 2) {
            this.lastName = names[1];
        }
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
     * Gets the set of addresses associated with the customer.
     *
     * @return the set of addresses
     */
    public Set<Address> getAddresses() {
        return addresses;
    }

    /**
     * Sets the set of addresses associated with the customer.
     *
     * @param addresses the set of addresses to set
     */
    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    /**
     * Gets the set of bookings associated with the customer.
     *
     * @return the set of bookings
     */
    public Set<Booking> getCustomerBookings() {
        return customerBookings;
    }

    /**
     * Sets the set of bookings associated with the customer.
     *
     * @param customerBookings the set of bookings to set
     */
    public void setCustomerBookings(Set<Booking> customerBookings) {
        this.customerBookings = customerBookings;
    }

    /**
     * Compares this customer with another object for equality.
     * Two customers are considered equal if their parent {@link User} attributes match.
     *
     * @param obj the object to compare to
     * @return {@code true} if the customers are equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Customer) {
            return super.equals(obj);
        }
        return false;
    }
}
