package jpja.webapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jpja.webapp.model.entities.User;

/**
 * Repository interface for {@link User} entities.
 * This interface provides CRUD operations and custom query methods for managing {@link User} objects in the database.
 * 
 * <p>Extends {@link JpaRepository}, enabling standard data access methods such as save, findById, findAll, and delete.</p>
 * 
 * <p>Includes custom queries for retrieving users by email, username, or a combination of both.</p>
 * 
 * @author James Prial
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a {@link User} entity by their email address.
     *
     * @param email the email address of the user
     * @return an {@link Optional} containing the {@link User} if found, or empty if not found
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a {@link User} entity exists with the specified email address.
     *
     * @param email the email address to check
     * @return {@code true} if a user exists with the given email, {@code false} otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Finds a {@link User} entity by their username.
     *
     * @param username the username of the user
     * @return an {@link Optional} containing the {@link User} if found, or empty if not found
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks if a {@link User} entity exists with the specified username.
     *
     * @param username the username to check
     * @return {@code true} if a user exists with the given username, {@code false} otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Finds a {@link User} entity by either their username or email address.
     *
     * @param identifier the username or email address of the user
     * @return an {@link Optional} containing the {@link User} if found, or empty if not found
     */
    @Query("SELECT u FROM User u WHERE u.username = :identifier OR u.email = :identifier")
    Optional<User> findByUsernameOrEmail(@Param("identifier") String identifier);

    /**
     * Checks if a {@link User} entity exists with either the specified username or email address.
     *
     * @param identifier the username or email address to check
     * @return {@code true} if a user exists with the given username or email, {@code false} otherwise
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END " +
           "FROM User u WHERE u.username = :identifier OR u.email = :identifier")
    boolean existsByUsernameOrEmail(@Param("identifier") String identifier);
}
