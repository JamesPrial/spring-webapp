package jpja.webapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jpja.webapp.model.entities.AddressType;

/**
 * Repository interface for {@link AddressType} entities.
 * This interface provides CRUD operations and query methods for managing {@link AddressType} objects in the database.
 * 
 * <p>Extends {@link JpaRepository}, which provides standard data access methods,
 * such as save, findById, findAll, and delete.</p>
 * 
 * <p>Includes a custom query method for finding an {@link AddressType} by its name.</p>
 * 
 * @author James Prial
 */
@Repository
public interface AddressTypeRepository extends JpaRepository<AddressType, Long> {

    /**
     * Finds an {@link AddressType} entity by its name.
     *
     * @param name the name of the address type to search for
     * @return an {@link Optional} containing the {@link AddressType} if found, or empty if not found
     */
    Optional<AddressType> findByName(String name);
}
