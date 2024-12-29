package jpja.webapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jpja.webapp.model.entities.Booking;
import jpja.webapp.model.entities.Review;

/**
 * Repository interface for {@link Review} entities.
 * This interface provides CRUD operations and custom query methods for managing {@link Review} objects in the database.
 * 
 * <p>Extends {@link JpaRepository}, enabling standard data access methods such as save, findById, findAll, and delete.</p>
 * 
 * <p>Includes custom queries for retrieving reviews by their associated booking.</p>
 * 
 * @author James Prial
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * Finds a {@link Review} entity by the ID of its associated booking.
     *
     * @param bookingId the ID of the associated booking
     * @return an {@link Optional} containing the {@link Review} if found, or empty if not found
     */
    Optional<Review> findByBookingId(Long bookingId);

    /**
     * Finds a {@link Review} entity by its associated {@link Booking}.
     *
     * @param booking the {@link Booking} entity
     * @return an {@link Optional} containing the {@link Review} if found, or empty if not found
     */
    Optional<Review> findByBooking(Booking booking);
}
