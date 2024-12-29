package jpja.webapp.model.entities;

/**
 * Interface representing a general modifier.
 * A modifier is an entity that provides additional metadata or categorization
 * for other entities, such as {@link BookingModifier} or {@link AddressType}.
 * 
 * <p>This interface defines common methods for accessing and modifying
 * the identifier, name, and description of a modifier.</p>
 * 
 * @author James Prial
 */
public interface ModifierInterface {

    /**
     * Gets the unique identifier for the modifier.
     *
     * @return the modifier ID
     */
    Long getId();

    /**
     * Gets the name of the modifier.
     *
     * @return the name
     */
    String getName();

    /**
     * Gets the description of the modifier.
     *
     * @return the description
     */
    String getDescription();

    /**
     * Sets the unique identifier for the modifier.
     *
     * @param id the modifier ID to set
     */
    void setId(Long id);

    /**
     * Sets the name of the modifier.
     *
     * @param name the name to set
     */
    void setName(String name);

    /**
     * Sets the description of the modifier.
     *
     * @param description the description to set
     */
    void setDescription(String description);
}
