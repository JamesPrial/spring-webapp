package jpja.webapp.model.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a verification token for user account confirmation or password recovery.
 * This class maps to the "verification_ids" table in the database and is associated with a {@link User}.
 * 
 * <p>Each token contains a unique identifier (UUID), an expiration date, and a reference to the user it belongs to.</p>
 * 
 * @author James Prial
 */
@Entity
@Table(name = "verification_ids")
public class VerificationToken {

    /**
     * The unique identifier for the verification token.
     */
    @Id
    @Column(nullable = false, unique = true)
    private String UUID;

    /**
     * The expiration date and time of the verification token.
     * This field is required.
     */
    @Column(name = "exp_date", nullable = false)
    private LocalDateTime expiryDate;

    /**
     * The user associated with this verification token.
     * Represented as a many-to-one relationship with lazy fetching to optimize performance.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Default constructor.
     * Initializes fields to default values.
     */
    public VerificationToken() {
        this.UUID = null;
        this.expiryDate = null;
        this.user = null;
    }

    /**
     * Constructs a verification token with the specified UUID, expiry date, and associated user.
     *
     * @param UUID the unique identifier for the token
     * @param expiryDate the expiration date and time of the token
     * @param user the user associated with the token
     */
    public VerificationToken(String UUID, LocalDateTime expiryDate, User user) {
        this.UUID = UUID;
        this.expiryDate = expiryDate;
        this.user = user;
    }

    /**
     * Gets the unique identifier for the verification token.
     *
     * @return the UUID
     */
    public String getUUID() {
        return UUID;
    }

    /**
     * Sets the unique identifier for the verification token.
     *
     * @param UUID the UUID to set
     */
    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    /**
     * Gets the expiration date and time of the verification token.
     *
     * @return the expiration date and time
     */
    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    /**
     * Sets the expiration date and time of the verification token.
     *
     * @param expiryDate the expiration date and time to set
     */
    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     * Gets the user associated with this verification token.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user associated with this verification token.
     *
     * @param user the user to associate
     */
    public void setUser(User user) {
        this.user = user;
    }
}
