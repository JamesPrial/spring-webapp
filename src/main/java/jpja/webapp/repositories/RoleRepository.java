package jpja.webapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jpja.webapp.model.entities.Role;

/**
 * Repository interface for {@link Role} entities.
 * This interface provides CRUD operations and custom query methods for managing {@link Role} objects in the database.
 * 
 * <p>Extends {@link JpaRepository}, enabling standard data access methods such as save, findById, findAll, and delete.</p>
 * 
 * <p>Includes a custom query method for retrieving a {@link Role} entity by its name.</p>
 * 
 * @author James Prial
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Finds a {@link Role} entity by its name.
     *
     * @param name the name of the role
     * @return an {@link Optional} containing the {@link Role} if found, or empty if not found
     */
    Optional<Role> findByName(String name);
}
