package jpja.webapp.model.dto;

import java.util.Set;

/**
 * Data Transfer Object (DTO) for outgoing user data.
 * This class represents user information sent from the server to the client.
 * Extends {@link UserSuperDTO} to include additional user-specific fields, such as
 * the user ID and associated roles.
 * 
 * <p>Primarily intended for read-only operations where user data needs to be serialized 
 * and sent as part of a response.</p>
 * 
 * @author James Prial
 */
public class UserOutgoingDTO extends UserSuperDTO implements UserDTOInterface {

    /**
     * The unique identifier of the user.
     */
    private Long id;

    /**
     * The roles associated with the user, represented as a set of {@link ModifierDTO}.
     */
    private Set<ModifierDTO> roles;

    /**
     * Default constructor.
     * Initializes all fields to {@code null}.
     */
    public UserOutgoingDTO() {
        super();
        this.id = null;
        this.roles = null;
    }

    /**
     * Gets the unique identifier of the user.
     *
     * @return the user ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the user.
     *
     * @param id the user ID to set
     */
    public void setId(Long id) {
        this.id = id;
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
