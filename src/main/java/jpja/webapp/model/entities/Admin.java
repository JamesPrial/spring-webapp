package jpja.webapp.model.entities;

import jakarta.persistence.*;

/**
 * Entity representing an admin user.
 * Extends the {@link User} class to include additional attributes specific to administrators,
 * such as their name and the record of their last login.
 * 
 * <p>This class maps to the "admin_info" table in the database.</p>
 * 
 * @author James Prial
 */
@Entity
@Table(name = "admin_info")
public class Admin extends User {

    /**
     * The last login record of the admin.
     * Mapped as a one-to-one relationship with {@link LoginRecord}.
     * This field is optional.
     */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "last_login", nullable = true)
    private LoginRecord lastLogin;

    /**
     * The name of the admin.
     */
    @Column
    private String name;

    /**
     * Default constructor.
     * Initializes all fields to their default values.
     */
    public Admin() {
        super();
        this.name = null;
    }

    /**
     * Constructs an admin with the specified email, username, password, and role.
     *
     * @param email the email of the admin
     * @param username the username of the admin
     * @param password the password of the admin
     * @param role the role of the admin
     */
    public Admin(String email, String username, String password, Role role) {
        super(email, username, password, role);
        this.name = null;
    }

    /**
     * Gets the last login record of the admin.
     *
     * @return the last login record
     */
    public LoginRecord getLastLogin() {
        return lastLogin;
    }

    /**
     * Sets the last login record of the admin.
     *
     * @param lastLogin the last login record to set
     */
    public void setLastLogin(LoginRecord lastLogin) {
        this.lastLogin = lastLogin;
    }

    /**
     * Gets the name of the admin.
     *
     * @return the admin's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the admin.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
