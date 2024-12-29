package jpja.webapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jpja.webapp.model.entities.User;
import jpja.webapp.model.entities.VerificationToken;

import java.util.Optional;

/**
 * Repository interface for {@link VerificationToken} entities.
 * This interface provides CRUD operations and custom query methods for managing {@link VerificationToken} objects in the database.
 * 
 * <p>Extends {@link JpaRepository}, enabling standard data access methods such as save, findById, findAll, and delete.</p>
 * 
 * <p>Includes custom queries for retrieving the most recent token by user ID and finding a token by user.</p>
 * 
 * @author James Prial
 */
@Repository
public interface TokenRepository extends JpaRepository<VerificationToken, String> {

    /**
     * Finds the most recent {@link VerificationToken} for a user by their ID.
     *
     * @param userId the ID of the user
     * @return an {@link Optional} containing the most recent {@link VerificationToken}, or empty if not found
     */
    @Query(value = "SELECT * FROM verification_ids v WHERE v.user_id = :userId ORDER BY v.exp_date DESC LIMIT 1", nativeQuery = true)
    Optional<VerificationToken> findMostRecentByUserId(@Param("userId") Long userId);

    /**
     * Finds a {@link VerificationToken} entity associated with a specific {@link User}.
     *
     * @param user the {@link User} entity
     * @return an {@link Optional} containing the {@link VerificationToken}, or empty if not found
     */
    Optional<VerificationToken> findByUser(User user);
}
