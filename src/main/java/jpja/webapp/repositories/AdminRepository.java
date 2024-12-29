package jpja.webapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jpja.webapp.model.entities.Admin;

/**
 * Repository interface for {@link Admin} entities.
 * This interface provides CRUD operations and query methods for managing {@link Admin} objects in the database.
 * 
 * <p>Extends {@link JpaRepository}, which provides standard data access methods,
 * such as save, findById, findAll, and delete.</p>
 * 
 * <p>Includes a custom query method for finding an {@link Admin} by their email address.</p>
 * 
 * @author James Prial
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    /**
     * Finds an {@link Admin} entity by their email address.
     *
     * @param email the email address of the admin to search for
     * @return an {@link Optional} containing the {@link Admin} if found, or empty if not found
     */
    Optional<Admin> findByEmail(String email);

}
