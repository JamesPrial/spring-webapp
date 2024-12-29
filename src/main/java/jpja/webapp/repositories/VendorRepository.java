package jpja.webapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jpja.webapp.model.entities.Vendor;

/**
 * Repository interface for {@link Vendor} entities.
 * This interface provides CRUD operations and custom query methods for managing {@link Vendor} objects in the database.
 * 
 * <p>Extends {@link JpaRepository}, enabling standard data access methods such as save, findById, findAll, and delete.</p>
 * 
 * <p>Includes custom queries for retrieving vendors by email, username, or name.</p>
 * 
 * @author James Prial
 */
@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {

    /**
     * Finds a {@link Vendor} entity by their email address.
     *
     * @param email the email address of the vendor
     * @return an {@link Optional} containing the {@link Vendor} if found, or empty if not found
     */
    Optional<Vendor> findByEmail(String email);

    /**
     * Finds a {@link Vendor} entity by their username.
     *
     * @param username the username of the vendor
     * @return an {@link Optional} containing the {@link Vendor} if found, or empty if not found
     */
    Optional<Vendor> findByUsername(String username);

    /**
     * Finds a {@link Vendor} entity by their business name.
     *
     * @param name the name of the vendor's business
     * @return an {@link Optional} containing the {@link Vendor} if found, or empty if not found
     */
    Optional<Vendor> findByName(String name);
}
