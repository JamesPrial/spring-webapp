package jpja.webapp.model.entities;

import jakarta.persistence.*;

/**
 * Entity representing a type of address.
 * This class maps to the "address_types" table in the database and includes fields
 * for the type's name and description.
 * 
 * <p>An address type is used to categorize addresses (e.g., "Home", "Work").</p>
 * Implements the {@link ModifierInterface} to standardize behavior across modifier-like entities.</p>
 * 
 * @author James Prial
 */
@Entity
@Table(name = "address_types")
public class AddressType implements ModifierInterface {

    /**
     * The unique identifier for the address type.
     */
    @Id
    @Column(columnDefinition = "int unsigned")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the address type (e.g., "Home", "Work").
     * This field is required and has a maximum length of 10 characters.
     */
    @Column(name = "name", columnDefinition = "varchar(10)", nullable = false)
    private String name;

    /**
     * A description of the address type.
     * This field is optional and provides additional context about the type.
     */
    @Column(name = "description", nullable = true)
    private String description;

    /**
     * Default constructor.
     * Initializes all fields to {@code null}.
     */
    public AddressType() {
        this.id = null;
        this.name = null;
        this.description = null;
    }

    /**
     * Constructs an AddressType with the specified details.
     *
     * @param id the unique identifier for the address type
     * @param name the name of the address type
     * @param description the description of the address type
     */
    public AddressType(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * Gets the unique identifier for the address type.
     *
     * @return the address type ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the address type.
     *
     * @param id the address type ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name of the address type.
     *
     * @return the name of the address type
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the address type.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the address type.
     *
     * @return the description of the address type
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the address type.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the string representation of the address type, which is its name.
     *
     * @return the name of the address type
     */
    @Override
    public String toString() {
        return name;
    }
}
