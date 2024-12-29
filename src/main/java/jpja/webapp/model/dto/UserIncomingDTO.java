package jpja.webapp.model.dto;

import java.util.Set;

/**
 * Data Transfer Object (DTO) for incoming user data.
 * This class is used to capture and validate user input during creation or
 * updates.
 * Extends {@link UserSuperDTO} to include additional user-specific fields, such
 * as
 * a password and associated roles.
 * 
 * <p>
 * Primarily intended for handling user-related requests where sensitive
 * information
 * like passwords is required.
 * </p>
 * 
 * @author James Prial
 */
public class UserIncomingDTO extends UserSuperDTO implements UserDTOInterface {

    /**
     * The password of the user.
     */
    private String password;

    /**
     * The roles associated with the user, represented as a set of
     * {@link ModifierDTO}.
     */
    private Set<ModifierDTO> roles;

    /**
     * Default constructor.
     * Initializes all fields to {@code null}.
     */
    public UserIncomingDTO() {
        super();
        this.password = null;
        this.roles = null;
    }

    /**
     * Gets the password of the user.
     *
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the roles associated with the user.
     * Each role is represented as a {@link ModifierDTO}.
     *
     * @return a set of roles
     */
    public Set<ModifierDTO> getRoles() {
        return roles;
    }

    /**
     * Sets the roles associated with the user.
     *
     * @param roles a set of roles to associate with the user
     */
    public void setRoles(Set<ModifierDTO> roles) {
        this.roles = roles;
    }
}
