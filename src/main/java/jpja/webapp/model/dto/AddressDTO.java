package jpja.webapp.model.dto;

/**
 * Data Transfer Object (DTO) for representing address information.
 * This class encapsulates the details of an address, including its components
 * such as street, unit, city, state, and ZIP code, as well as additional
 * metadata like
 * a type ID and nickname.
 * @author James Prial
 */
public class AddressDTO implements DTOInterface {

    /**
     * The unique identifier for the address.
     */
    private Long id;

    /**
     * The street address (e.g., "123 Main St").
     */
    private String address;

    /**
     * The unit or apartment number, if applicable.
     */
    private String unit;

    /**
     * The city of the address.
     */
    private String city;

    /**
     * The ZIP or postal code of the address.
     */
    private String zip;

    /**
     * The state or region of the address.
     */
    private String state;

    /**
     * The type identifier associated with the address (e.g., home, work).
     */
    private Long typeId;

    /**
     * A nickname for the address (e.g., "Home", "Office").
     */
    private String nickname;

    /**
     * Default constructor.
     * Initializes all fields to {@code null}.
     */
    public AddressDTO() {
        this.id = null;
        this.address = null;
        this.typeId = null;
        this.nickname = null;
        this.unit = null;
        this.city = null;
        this.zip = null;
        this.state = null;
    }

    /**
     * Gets the street address.
     *
     * @return the street address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the street address.
     *
     * @param address the street address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the type identifier of the address.
     *
     * @return the type identifier
     */
    public Long getTypeId() {
        return typeId;
    }

    /**
     * Sets the type identifier of the address.
     *
     * @param typeId the type identifier to set
     */
    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    /**
     * Gets the unique identifier of the address.
     *
     * @return the unique identifier
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the address.
     *
     * @param id the unique identifier to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the nickname of the address.
     *
     * @return the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets the nickname of the address.
     *
     * @param nickname the nickname to set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Gets the unit or apartment number.
     *
     * @return the unit or apartment number
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Sets the unit or apartment number.
     *
     * @param unit the unit or apartment number to set
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Gets the city of the address.
     *
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city of the address.
     *
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Gets the ZIP or postal code of the address.
     *
     * @return the ZIP or postal code
     */
    public String getZip() {
        return zip;
    }

    /**
     * Sets the ZIP or postal code of the address.
     *
     * @param zip the ZIP or postal code to set
     */
    public void setZip(String zip) {
        this.zip = zip;
    }

    /**
     * Gets the state or region of the address.
     *
     * @return the state or region
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the state or region of the address.
     *
     * @param state the state or region to set
     */
    public void setState(String state) {
        this.state = state;
    }
}
