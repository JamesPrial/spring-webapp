package jpja.webapp.model.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Abstract entity representing a user in the system.
 * This class serves as the base for specific user types, such as {@link Admin}, {@link Customer}, and {@link Vendor}.
 * 
 * <p>It maps to the "users" table in the database using a joined inheritance strategy,
 * which allows related tables for subclasses to share the same primary key.</p>
 * 
 * @author James Prial
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
public abstract class User {

    /**
     * The unique identifier for the user.
     */
    @Id
    @Column(columnDefinition = "int(10) unsigned")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * The unique username for the user.
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * The unique email for the user.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * The password for the user.
     */
    @Column(nullable = false)
    private String password;

    /**
     * The user's verification level, represented as a tiny integer.
     * Indicates the level of trust or verification the user has achieved.
     */
    @Column(columnDefinition = "TINYINT", name = "verification_level", nullable = false)
    private int verificationLevel;

    /**
     * The roles assigned to the user.
     * Represented as a many-to-many relationship with the {@link Role} entity.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    /**
     * Default constructor.
     * Initializes fields to default values.
     */
    public User() {
        this.email = null;
        this.username = null;
        this.password = null;
        this.verificationLevel = 0;
    }

    /**
     * Constructs a user with the specified email, username, password, and role.
     *
     * @param email the email of the user
     * @param username the username of the user
     * @param password the password of the user
     * @param role the role to assign to the user
     */
    public User(String email, String username, String password, Role role) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.verificationLevel = 0;
        this.roles.add(role);
    }

    /**
     * Gets the unique identifier for the user.
     *
     * @return the user ID
     */
    public long getId() {
        return id;
    }

    /**
     * Gets the email of the user.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user.
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the password of the user.
     *
     * @return the password
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
     * Gets the username of the user.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the verification level of the user.
     *
     * @return the verification level
     */
    public int getVerificationLevel() {
        return verificationLevel;
    }

    /**
     * Sets the verification level of the user.
     *
     * @param verificationLevel the verification level to set
     */
    public void setVerificationLevel(int verificationLevel) {
        this.verificationLevel = verificationLevel;
    }

    /**
     * Gets the roles assigned to the user.
     *
     * @return the set of roles
     */
    public Set<Role> getRoles() {
        return roles;
    }

    /**
     * Sets the roles assigned to the user.
     *
     * @param roles the set of roles to assign
     */
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    /**
     * Adds a role to the user's set of roles.
     *
     * @param role the role to add
     * @return {@code true} if the role was added, {@code false} if it was already present
     */
    public boolean addRole(Role role) {
        if (roles.contains(role)) {
            return false;
        }
        return roles.add(role);
    }

    /**
     * Gets the full name of the user.
     * This method is abstract and must be implemented by subclasses.
     *
     * @return the full name of the user
     */
    public abstract String getName();

    /**
     * Sets the full name of the user.
     * This method is abstract and must be implemented by subclasses.
     *
     * @param name the full name to set
     */
    public abstract void setName(String name);

    /**
     * Compares this user with another object for equality.
     * Two users are considered equal if their ID, email, password, roles, and verification level match.
     *
     * @param obj the object to compare to
     * @return {@code true} if the users are equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof User) {
            User userObj = (User) obj;
            return this.id == userObj.getId()
                && this.email.equals(userObj.getEmail())
                && this.password.equals(userObj.getPassword())
                && this.roles.equals(userObj.getRoles())
                && this.verificationLevel == userObj.getVerificationLevel();
        }
        return false;
    }
}
