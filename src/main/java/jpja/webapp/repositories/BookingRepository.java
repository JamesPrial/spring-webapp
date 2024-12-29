package jpja.webapp.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jpja.webapp.model.entities.Booking;
import jpja.webapp.model.entities.BookingModifier;
import jpja.webapp.model.entities.Customer;
import jpja.webapp.model.entities.User;
import jpja.webapp.model.entities.Vendor;

/**
 * Repository interface for {@link Booking} entities.
 * This interface provides CRUD operations and query methods for managing {@link Booking} objects in the database.
 * 
 * <p>Extends {@link JpaRepository}, which provides standard data access methods,
 * such as save, findById, findAll, and delete.</p>
 * 
 * <p>Includes custom query methods for retrieving bookings based on customer, vendor,
 * modifiers, and other attributes.</p>
 * 
 * @author James Prial
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Finds all bookings for a customer by their ID.
     *
     * @param customerId the ID of the customer
     * @return a set of bookings associated with the specified customer ID
     */
    Set<Booking> findByCustomerId(Long customerId);

    /**
     * Finds all bookings for a vendor by their ID.
     *
     * @param vendorId the ID of the vendor
     * @return a set of bookings associated with the specified vendor ID
     */
    Set<Booking> findByVendorId(Long vendorId);

    /**
     * Finds all bookings associated with a specific vendor.
     *
     * @param vendor the vendor entity
     * @return a set of bookings associated with the specified vendor
     */
    Set<Booking> findByVendor(Vendor vendor);

    /**
     * Finds all bookings associated with a specific customer.
     *
     * @param customer the customer entity
     * @return a set of bookings associated with the specified customer
     */
    Set<Booking> findByCustomer(User customer);

    /**
     * Finds all unclaimed bookings (i.e., bookings with no vendor assigned).
     *
     * @return a set of bookings where the vendor is null
     */
    Set<Booking> findByVendorIsNull();

    /**
     * Finds all bookings that include a specific booking modifier.
     *
     * @param modifier the booking modifier
     * @return a set of bookings that include the specified modifier
     */
    Set<Booking> findByModifiers(BookingModifier modifier);

    /**
     * Finds all bookings for a specific customer that include a specific booking modifier.
     *
     * @param customer the customer entity
     * @param modifier the booking modifier
     * @return a set of bookings for the specified customer with the specified modifier
     */
    Set<Booking> findByCustomerAndModifiers(Customer customer, BookingModifier modifier);

    /**
     * Finds all bookings for a specific vendor that include a specific booking modifier.
     *
     * @param vendor the vendor entity
     * @param modifier the booking modifier
     * @return a set of bookings for the specified vendor with the specified modifier
     */
    Set<Booking> findByVendorAndModifiers(Vendor vendor, BookingModifier modifier);
}
