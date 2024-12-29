package jpja.webapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import jpja.webapp.model.entities.Address;

/**
 * Repository interface for {@link Address} entities.
 * This interface provides CRUD operations and query methods for managing {@link Address} objects in the database.
 * 
 * <p>Extends {@link JpaRepository}, which provides standard data access methods,
 * such as save, findById, findAll, and delete.</p>
 * 
 * <p>Spring Data JPA will automatically generate the implementation for this interface.</p>
 * 
 * @author James Prial
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    
}
