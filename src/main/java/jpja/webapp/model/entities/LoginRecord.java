package jpja.webapp.model.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a login record.
 * This class maps to the "login_records" table in the database and stores details
 * about user logins, including the timestamp and IP address of the login.
 * 
 * <p>Each login record is associated with a {@link User} entity.</p>
 * 
 * @author James Prial
 */
@Entity
@Table(name = "login_records")
public class LoginRecord {

    /**
     * The unique identifier for the login record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The user associated with the login record.
     * Represented as a many-to-one relationship with lazy fetching to optimize loading.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * The timestamp of the login.
     * This field is required.
     */
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    /**
     * The IP address from which the login occurred.
     * This field is optional and can store up to 15 characters.
     */
    @Column(columnDefinition = "VARCHAR(15)", name = "ip_address", nullable = true)
    private String ipAddress;

    /**
     * Default constructor.
     * Initializes fields to {@code null}.
     */
    public LoginRecord() {
        this.user = null;
        this.timestamp = null;
        this.ipAddress = null;
    }

    /**
     * Constructs a login record with the specified details.
     *
     * @param user the user associated with the login
     * @param timestamp the timestamp of the login
     * @param ipAddress the IP address of the login
     */
    public LoginRecord(User user, LocalDateTime timestamp, String ipAddress) {
        this.user = user;
        this.timestamp = timestamp;
        this.ipAddress = ipAddress;
    }

    /**
     * Gets the unique identifier for the login record.
     *
     * @return the login record ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the user associated with the login record.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Gets the timestamp of the login.
     *
     * @return the login timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the IP address from which the login occurred.
     *
     * @return the IP address
     */
    public String getIpAddress() {
        return ipAddress;
    }
}
