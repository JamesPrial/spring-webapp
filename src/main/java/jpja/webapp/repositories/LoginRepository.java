package jpja.webapp.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jpja.webapp.model.entities.LoginRecord;
import jpja.webapp.model.entities.User;

/**
 * Repository interface for {@link LoginRecord} entities.
 * This interface provides CRUD operations and custom query methods for managing {@link LoginRecord} objects in the database.
 * 
 * <p>Extends {@link JpaRepository}, enabling standard data access methods such as save, findById, findAll, and delete.</p>
 * 
 * <p>Includes custom queries for retrieving login records by user and finding the latest login record for a user.</p>
 * 
 * @author James Prial
 */
@Repository
public interface LoginRepository extends JpaRepository<LoginRecord, Long> {

    /**
     * Finds all {@link LoginRecord} entities associated with a user by their ID.
     *
     * @param userId the ID of the user
     * @return a list of {@link LoginRecord} entities for the specified user
     */
    List<LoginRecord> findByUserId(Long userId);

    /**
     * Finds all {@link LoginRecord} entities associated with a specific {@link User}.
     *
     * @param user the {@link User} entity
     * @return a list of {@link LoginRecord} entities for the specified user
     */
    List<LoginRecord> findByUser(User user);

    /**
     * Finds the most recent {@link LoginRecord} for a user by their ID.
     *
     * @param userId the ID of the user
     * @return an {@link Optional} containing the most recent {@link LoginRecord}, or empty if no records exist
     */
    Optional<LoginRecord> findFirstByUserIdOrderByTimestampDesc(Long userId);

    /**
     * Finds the most recent {@link LoginRecord} for a specific {@link User}.
     *
     * @param user the {@link User} entity
     * @return an {@link Optional} containing the most recent {@link LoginRecord}, or empty if no records exist
     */
    Optional<LoginRecord> findFirstByUserOrderByTimestampDesc(User user);
}
