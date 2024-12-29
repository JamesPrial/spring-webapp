package jpja.webapp.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Superclass for Data Transfer Objects (DTOs) related to user information.
 * This class provides common fields and functionality shared across different 
 * user-related DTOs, such as email and username.
 * 
 * <p>Validation annotations are applied to ensure the integrity of these fields.</p>
 * 
 * @author James Prial
 */
public class UserSuperDTO implements DTOInterface {

    /**
     * The email address of the user.
     * Must be a valid email format.
     */
    @Email(message = "Please provide a valid email")
    private String email;

    /**
     * The username of the user.
     * Must not be blank.
     */
    @NotBlank
    private String username;

    /**
     * Default constructor.
     * Initializes all fields to {@code null}.
     */
    public UserSuperDTO() {
        this.email = null;
        this.username = null;
    }

    /**
     * Gets the email address of the user.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email the email address to set
     */
    public void setEmail(String email) {
        this.email = email;
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
}
