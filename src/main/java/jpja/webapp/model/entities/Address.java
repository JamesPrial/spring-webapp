package jpja.webapp.model.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

/**
 * Entity representing an address.
 * This class maps to the "addresses" table in the database and includes fields
 * for street details, city, state, ZIP code, and associated types.
 * 
 * <p>Addresses can be associated with multiple types via a many-to-many relationship
 * with the {@link AddressType} entity.</p>
 * 
 * <p>Includes utility methods for adding and removing address types.</p>
 * 
 * @author James Prial
 */
@Entity
@Table(name = "addresses")
public class Address {

    /**
     * The unique identifier for the address.
     */
    @Id
    @Column(columnDefinition = "int unsigned")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The street number of the address.
     * This field is required.
     */
    @Column(name = "street_number", columnDefinition = "varchar(10)", nullable = false)
    private String streetNumber;

    /**
     * The street name of the address.
     * This field is required.
     */
    @Column(name = "street_name", nullable = false)
    private String streetName;

    /**
     * The unit or apartment number of the address.
     * This field is optional.
     */
    @Column(name = "unit", columnDefinition = "varchar(25)", nullable = true)
    private String unit;

    /**
     * The city of the address.
     * This field is required.
     */
    @Column(name = "city", nullable = false)
    private String city;

    /**
     * The ZIP code of the address.
     * This field is required.
     */
    @Column(name = "zip", columnDefinition = "varchar(15)", nullable = false)
    private String zip;

    /**
     * The state of the address.
     * This field is required and should be a two-character state abbreviation.
     */
    @Column(name = "state", columnDefinition = "varchar(2)", nullable = false)
    private String state;

    /**
     * The nickname for the address (e.g., "Home", "Office").
     */
    @Column
    private String nickname;

    /**
     * The set of address types associated with this address.
     * Represented as a many-to-many relationship.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "address_type_join",
        joinColumns = @JoinColumn(name = "address_id"),
        inverseJoinColumns = @JoinColumn(name = "type_id")
    )
    private Set<AddressType> types;

    /**
     * Default constructor.
     * Initializes all fields to {@code null} and sets the types to an empty set.
     */
    public Address() {
        this.id = null;
        this.streetNumber = null;
        this.streetName = null;
        this.unit = null;
        this.city = null;
        this.zip = null;
        this.state = null;
        this.types = new HashSet<>();
        this.nickname = null;
    }

    /**
     * Constructs an Address with the specified details and no unit.
     *
     * @param id the unique identifier of the address
     * @param streetNumber the street number
     * @param streetName the street name
     * @param city the city
     * @param zip the ZIP code
     * @param state the state
     * @param types the set of address types
     * @param nickname the nickname for the address
     */
    public Address(long id, String streetNumber, String streetName, String city, String zip, String state, Set<AddressType> types, String nickname) {
        this.id = id;
        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.unit = null;
        this.city = city;
        this.zip = zip;
        this.state = state;
        this.types = types;
        this.nickname = nickname;
    }

    /**
     * Constructs an Address with the specified details, including a unit.
     *
     * @param id the unique identifier of the address
     * @param streetNumber the street number
     * @param streetName the street name
     * @param unit the unit or apartment number
     * @param city the city
     * @param zip the ZIP code
     * @param state the state
     * @param types the set of address types
     * @param nickname the nickname for the address
     */
    public Address(long id, String streetNumber, String streetName, String unit, String city, String zip, String state, Set<AddressType> types, String nickname) {
        this.id = id;
        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.unit = unit;
        this.city = city;
        this.zip = zip;
        this.state = state;
        this.types = types;
        this.nickname = nickname;
    }

    // Getters and setters for all fields
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Set<AddressType> getTypes() {
        return types;
    }

    public void setTypes(Set<AddressType> types) {
        this.types = types;
    }

    /**
     * Adds an {@link AddressType} to the set of associated types.
     *
     * @param type the type to add
     * @return {@code true} if the type was added, {@code false} otherwise
     */
    public boolean addType(AddressType type) {
        return types.add(type);
    }

    /**
     * Removes an {@link AddressType} from the set of associated types.
     *
     * @param type the type to remove
     * @return {@code true} if the type was removed, {@code false} otherwise
     */
    public boolean removeType(AddressType type) {
        return types.remove(type);
    }

    // hashCode, equals, and toString methods
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Address other = (Address) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return streetNumber + " " + streetName + ", " + (unit != null ? unit + ", " : "") + city + ", " + zip + " " + state;
    }
}
