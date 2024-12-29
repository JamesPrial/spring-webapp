package jpja.webapp.model.entities;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.*;

/**
 * Entity representing a user role.
 * Roles define the permissions and authorities assigned to users within the system.
 * This class maps to the "roles" table in the database and implements both
 * {@link GrantedAuthority} for Spring Security integration and {@link ModifierInterface} for consistent handling of modifiers.
 * 
 * @author James Prial
 */
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority, ModifierInterface {

    /**
     * The unique identifier for the role.
     */
    @Id
    @Column(columnDefinition = "INT UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the role (e.g., "ADMIN", "USER").
     * This field is required.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * A description of the role, providing additional context or details.
     */
    private String description;

    /**
     * Gets the unique identifier for the role.
     *
     * @return the role ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the role.
     *
     * @param id the role ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name of the role.
     *
     * @return the role name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the role.
     *
     * @param name the role name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the role.
     *
     * @return the role description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the role.
     *
     * @param description the role description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the role name as the granted authority for Spring Security.
     *
     * @return the role name as the authority
     */
    @Override
    public String getAuthority() {
        return name;
    }

    /**
     * Compares this role with another object for equality.
     * Two roles are considered equal if their IDs and names match.
     *
     * @param obj the object to compare to
     * @return {@code true} if the roles are equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Role) {
            Role other = (Role) obj;
            return this.id != null && this.id.equals(other.getId()) && this.name.equals(other.getName());
        }
        return false;
    }

    /**
     * Returns a string representation of the role, which is its name.
     *
     * @return the role name
     */
    @Override
    public String toString() {
        return name;
    }
}
