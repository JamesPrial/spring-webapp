package jpja.webapp.model.dto;

import java.util.Set;

/**
 * Interface representing a Data Transfer Object (DTO) for a user.
 * Extends {@link DTOInterface} to provide user-specific attributes such as
 * email, username, and roles.
 * 
 * <p>
 * Implementations of this interface are expected to encapsulate the basic
 * data needed to represent and manipulate user information in the application.
 * </p>
 * 
 * @author James Prial
 */
public interface UserDTOInterface extends DTOInterface {

    /**
     * Gets the email address of the user.
     *
     * @return the email address
     */
    String getEmail();

    /**
     * Gets the username of the user.
     *
     * @return the username
     */
    String getUsername();

    /**
     * Gets the roles associated with the user.
     * Each role is represented as a {@link ModifierDTO}.
     *
     * @return a set of roles
     */
    Set<ModifierDTO> getRoles();

    /**
     * Sets the email address of the user.
     *
     * @param email the email address to set
     */
    void setEmail(String email);

    /**
     * Sets the username of the user.
     *
     * @param username the username to set
     */
    void setUsername(String username);

    /**
     * Sets the roles associated with the user.
     *
     * @param roles a set of roles to associate with the user
     */
    void setRoles(Set<ModifierDTO> roles);
}
