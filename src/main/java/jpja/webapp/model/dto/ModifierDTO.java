package jpja.webapp.model.dto;

/**
 * Data Transfer Object (DTO) for representing a booking modifier.
 * A modifier represents additional attributes or details that can be applied
 * to a booking, address, or user
 * 
 * 
 * <p>
 * Encapsulates basic modifier details, including its unique identifier,
 * name, and description.
 * </p>
 * 
 * @author James Prial
 */
public class ModifierDTO implements DTOInterface {

    /**
     * The unique identifier for the modifier.
     */
    private Long id;

    /**
     * The name of the modifier (e.g., "STATUS_CLAIMED").
     */
    private String name;

    /**
     * A description of the modifier.
     */
    private String description;

    /**
     * Default constructor.
     * Initializes all fields to {@code null}.
     */
    public ModifierDTO() {
        this.id = null;
        this.name = null;
        this.description = null;
    }

    /**
     * Gets the unique identifier of the modifier.
     *
     * @return the modifier ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the modifier.
     *
     * @param id the modifier ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name of the modifier.
     *
     * @return the modifier name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the modifier.
     *
     * @param name the modifier name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the modifier.
     *
     * @return the modifier description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the modifier.
     *
     * @param description the modifier description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
