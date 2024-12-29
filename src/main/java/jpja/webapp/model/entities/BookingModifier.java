package jpja.webapp.model.entities;

import jakarta.persistence.*;

/**
 * Entity representing a modifier for a booking.
 * A booking modifier allows for flexible tagging or categorization of bookings,
 * such as status ("COMPLETED") or type ("LARGE").
 * 
 * <p>This class maps to the "booking_modifiers" table in the database and
 * implements the {@link ModifierInterface} for consistency with other modifiers.</p>
 * 
 * @author James Prial
 */
@Entity
@Table(name = "booking_modifiers")
public class BookingModifier implements ModifierInterface {

    /**
     * The unique identifier for the booking modifier.
     */
    @Id
    @Column(columnDefinition = "INT UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The type of the modifier (e.g., "STATUS", "TYPE").
     */
    @Column
    private String type;

    /**
     * The name of the modifier (e.g., "COMPLETED", "LARGE").
     * This field is required.
     */
    @Column(nullable = false)
    private String name;

    /**
     * A description of the modifier.
     * This field is optional and provides additional context about the modifier.
     */
    @Column
    private String description;

    /**
     * Gets the unique identifier for the booking modifier.
     *
     * @return the modifier ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the booking modifier.
     *
     * @param id the modifier ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the type of the booking modifier.
     *
     * @return the modifier type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the booking modifier.
     *
     * @param type the modifier type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the name of the booking modifier.
     *
     * @return the modifier name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the booking modifier.
     *
     * @param name the modifier name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the booking modifier.
     *
     * @return the modifier description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the booking modifier.
     *
     * @param description the modifier description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns a string representation of the booking modifier in the format "type_name".
     *
     * @return the string representation of the booking modifier
     */
    @Override
    public String toString() {
        return this.type + "_" + this.name;
    }

    /**
     * Compares this booking modifier to another object for equality.
     * Two booking modifiers are considered equal if their IDs are the same.
     *
     * @param obj the object to compare to
     * @return {@code true} if the modifiers are equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof BookingModifier) {
            return this.id != null && this.id.equals(((BookingModifier) obj).getId());
        }
        return false;
    }
}
