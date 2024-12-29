package jpja.webapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jpja.webapp.model.entities.Customer;
import java.util.Optional;

/**
 * Repository interface for {@link Customer} entities.
 * This interface provides CRUD operations and query methods for managing {@link Customer} objects in the database.
 * 
 * <p>Extends {@link JpaRepository}, which provides standard data access methods,
 * such as save, findById, findAll, and delete.</p>
 * 
 * <p>Includes custom query methods for retrieving customers by their username or email address.</p>
 * 
 * @author James Prial
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Finds a {@link Customer} entity by their username.
     *
     * @param username the username of the customer
     * @return an {@link Optional} containing the {@link Customer} if found, or empty if not found
     */
    Optional<Customer> findByUsername(String username);

    /**
     * Finds a {@link Customer} entity by their email address.
     *
     * @param email the email address of the customer
     * @return an {@link Optional} containing the {@link Customer} if found, or empty if not found
     */
    Optional<Customer> findByEmail(String email);
}
