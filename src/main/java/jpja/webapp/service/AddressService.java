package jpja.webapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import jpja.webapp.model.dto.AddressDTO;
import jpja.webapp.model.dto.ModifierDTO;
import jpja.webapp.model.entities.Address;
import jpja.webapp.model.entities.AddressType;
import jpja.webapp.repositories.AddressRepository;
import jpja.webapp.repositories.AddressTypeRepository;

/**
 * Service class for managing {@link Address} entities.
 * Provides methods to create, retrieve, and manipulate address data, as well as
 * handle address types.
 * 
 * <p>
 * This service interacts with {@link AddressRepository} and
 * {@link AddressTypeRepository} to perform
 * database operations related to addresses and their associated types.
 * </p>
 * 
 * <p>
 * Key functionalities include:
 * <ul>
 * <li>Retrieving an address by its ID.</li>
 * <li>Saving a new or existing address.</li>
 * <li>Creating and saving new addresses from raw data or {@link AddressDTO}
 * objects.</li>
 * <li>Retrieving address types excluding default and removed types.</li>
 * </ul>
 * </p>
 * 
 * @author James Prial
 */
@Service
public class AddressService {
    private final AddressRepository addressRepository;
    private final AddressTypeRepository addressTypeRepository;

    /**
     * Constructs an {@link AddressService} with the specified repositories.
     *
     * @param addressRepository     the repository for {@link Address} entities
     * @param addressTypeRepository the repository for {@link AddressType} entities
     */
    public AddressService(AddressRepository addressRepository, AddressTypeRepository addressTypeRepository) {
        this.addressRepository = addressRepository;
        this.addressTypeRepository = addressTypeRepository;
    }

    /**
     * Retrieves an {@link Address} by its unique identifier.
     *
     * @param id the unique ID of the address to retrieve
     * @return the {@link Address} entity corresponding to the provided ID
     * @throws IllegalArgumentException if no address is found with the specified ID
     */
    public Address getAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + " not found"));
    }

    /**
     * Saves an {@link Address} entity to the database.
     * If the address is {@code null}, the method does nothing.
     *
     * @param address the {@link Address} entity to save
     */
    public void saveAddress(Address address) {
        if (address != null) {
            addressRepository.save(address);
        }
    }

    /**
     * Creates and saves a new {@link Address} entity with the provided details.
     *
     * @param street   the full street address (e.g., "123 Main St")
     * @param unit     the unit or apartment number (optional)
     * @param city     the city of the address
     * @param zip      the ZIP code of the address
     * @param state    the state of the address
     * @param nickname a nickname for the address (e.g., "Home", "Office")
     * @param typeId   the ID of the {@link AddressType} to associate with the
     *                 address
     * @return the newly created and saved {@link Address} entity
     * @throws IllegalArgumentException if any required address information is
     *                                  missing
     * @throws RuntimeException         if the specified address type is not found
     *                                  and the default type ("DFT") is also
     *                                  unavailable
     */
    public Address createAndSaveNewAddress(String street, String unit, String city, String zip, String state,
            String nickname, Long typeId) {
        if (street == null || city == null || zip == null || state == null || nickname == null || typeId == null) {
            throw new IllegalArgumentException("Missing address info");
        }
        AddressType type = addressTypeRepository.findById(typeId)
                .orElse(addressTypeRepository.findByName("DFT")
                        .orElseThrow(() -> new RuntimeException("Something went very wrong!")));
        Address address = new Address();
        String streetNum = street.split(" ")[0];
        String streetName = street.substring(streetNum.length() + 1);
        address.setStreetNumber(streetNum);
        address.setStreetName(streetName);
        if (unit != null) {
            address.setUnit(unit);
        }
        address.setCity(city);
        address.setZip(zip);
        address.setState(state);
        address.setNickname(nickname);

        saveAddress(address);
        address.addType(type);

        saveAddress(address);
        return address;
    }

    /**
     * Creates and saves a new {@link Address} entity based on the provided
     * {@link AddressDTO}.
     *
     * @param dto the {@link AddressDTO} containing address details
     * @return the newly created and saved {@link Address} entity
     * @throws IllegalArgumentException if the provided {@link AddressDTO} is
     *                                  {@code null}
     */
    public Address createAndSaveNewAddress(AddressDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Missing address info");
        }
        return createAndSaveNewAddress(dto.getAddress(), dto.getUnit(), dto.getCity(), dto.getZip(), dto.getState(),
                dto.getNickname(), dto.getTypeId());
    }

    /**
     * Retrieves an empty {@link AddressDTO}.
     *
     * @return a new instance of {@link AddressDTO}
     */
    public AddressDTO getAddressDTO() {
        return new AddressDTO();
    }

    /**
     * Retrieves a list of {@link ModifierDTO} representing address types, excluding
     * the default ("DFT") and removed ("X") types.
     *
     * @return a list of {@link ModifierDTO} for eligible address types
     */
    public List<ModifierDTO> getTypesExcludingDefaultAndRemoved() {
        List<AddressType> types = addressTypeRepository.findAll();
        List<ModifierDTO> ret = new ArrayList<>();
        for (AddressType type : types) {
            if (!type.getName().equals("DFT") && !type.getName().equals("X")) {
                ModifierDTO toAdd = new ModifierDTO();
                toAdd.setId(type.getId());
                toAdd.setDescription(type.getDescription());
                toAdd.setName(type.getName());
                ret.add(toAdd);
            }
        }
        return ret;
    }
}
