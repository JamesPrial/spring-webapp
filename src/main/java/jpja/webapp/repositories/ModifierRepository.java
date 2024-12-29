package jpja.webapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jpja.webapp.model.entities.BookingModifier;

import java.util.Optional;
import java.util.Set;

/**
 * Repository interface for {@link BookingModifier} entities.
 * This interface provides CRUD operations and custom query methods for managing {@link BookingModifier} objects in the database.
 * 
 * <p>Extends {@link JpaRepository}, which provides standard data access methods such as save, findById, findAll, and delete.</p>
 * 
 * <p>Includes custom queries for retrieving modifiers by type, name, or a combination of type and name.</p>
 * 
 * @author James Prial
 */
@Repository
public interface ModifierRepository extends JpaRepository<BookingModifier, Long> {

    /**
     * Finds all {@link BookingModifier} entities with the specified type.
     *
     * @param type the type of the modifier (e.g., "STATUS", "CATEGORY")
     * @return a set of {@link BookingModifier} entities matching the specified type
     */
    Set<BookingModifier> findByType(String type);

    /**
     * Finds all {@link BookingModifier} entities with the specified name.
     *
     * @param name the name of the modifier
     * @return a set of {@link BookingModifier} entities matching the specified name
     */
    Set<BookingModifier> findByName(String name);

    /**
     * Finds a {@link BookingModifier} entity with the specified type and name.
     *
     * @param type the type of the modifier
     * @param name the name of the modifier
     * @return an {@link Optional} containing the {@link BookingModifier} if found, or empty if not found
     */
    Optional<BookingModifier> findByTypeAndName(String type, String name);
}
