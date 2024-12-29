package jpja.webapp.factories;

import java.util.HashSet;
import java.util.Set;

import jpja.webapp.model.dto.UserSuperDTO;
import jpja.webapp.model.dto.VendorDTOInterface;
import jpja.webapp.model.dto.CustomerDTOInterface;
import jpja.webapp.model.dto.CustomerIncomingDTO;
import jpja.webapp.model.dto.CustomerOutgoingDTO;
import jpja.webapp.model.dto.DTOInterface;
import jpja.webapp.model.dto.ModifierDTO;
import jpja.webapp.model.dto.UserDTOInterface;
import jpja.webapp.model.dto.UserIncomingDTO;
import jpja.webapp.model.dto.UserOutgoingDTO;
import jpja.webapp.model.dto.VendorIncomingDTO;
import jpja.webapp.model.dto.VendorOutgoingDTO;
import jpja.webapp.model.entities.Address;
import jpja.webapp.model.entities.Customer;
import jpja.webapp.model.entities.Role;
import jpja.webapp.model.entities.User;
import jpja.webapp.model.entities.Vendor;

public class UserDTOFactory {

    /**
     * Creates a new instance of UserSuperDTO.
     * 
     * @return A new UserSuperDTO object.
     */
    public static UserSuperDTO createUserSuperDTO() {
        return new UserSuperDTO();
    }

    private static void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("No user given");
        }
    }

    private static void validateDTO(DTOInterface dto) {
        if (dto == null) {
            throw new IllegalArgumentException("No DTO given");
        }
    }

    /**
     * Copies the user information to a UserSuperDTO object.
     * 
     * @param user The user to copy information from.
     * @return A new UserSuperDTO object with the user information.
     * @throws IllegalArgumentException if the user is null.
     */
    public static UserSuperDTO copyToUserSuperDTO(User user) {
        validateUser(user);
        UserSuperDTO dto = new UserSuperDTO();
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        return dto;
    }

    /**
     * Copies common user information to a UserDTOInterface.
     * 
     * @param dto  The UserDTOInterface to populate.
     * @param user The User entity.
     * @return The populated UserDTOInterface.
     * @throws IllegalArgumentException if the dto or user is null.
     */
    private static UserDTOInterface copyUserInfo(UserDTOInterface dto, User user) {
        validateDTO(dto);
        validateUser(user);
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setRoles(copyRolesToDTO(user.getRoles()));
        return dto;
    }

    /**
     * Creates a new instance of UserIncomingDTO.
     * 
     * @return A new UserIncomingDTO object.
     */
    public static UserIncomingDTO createUserIncomingDTO() {
        return new UserIncomingDTO();
    }

    /**
     * Copies a User entity to a UserIncomingDTO.
     * 
     * @param user The User entity to copy from.
     * @return A populated UserIncomingDTO object.
     * @throws IllegalArgumentException if the user is null.
     */
    public static UserIncomingDTO copyToUserIncomingDTO(User user) {
        validateUser(user);
        UserIncomingDTO dto = (UserIncomingDTO) copyUserInfo(createUserIncomingDTO(), user);
        dto.setPassword(user.getPassword());
        return dto;
    }

    /**
     * Creates a new instance of UserOutgoingDTO.
     * 
     * @return A new UserOutgoingDTO object.
     */
    public static UserOutgoingDTO createUserOutgoingDTO() {
        return new UserOutgoingDTO();
    }

    /**
     * Copies a User entity to a UserOutgoingDTO.
     * 
     * @param user The User entity to copy from.
     * @return A populated UserOutgoingDTO object.
     * @throws IllegalArgumentException if the user is null.
     */
    public static UserOutgoingDTO copyToUserOutgoingDTO(User user) {
        validateUser(user);
        UserOutgoingDTO dto = (UserOutgoingDTO) copyUserInfo(createUserOutgoingDTO(), user);
        dto.setId(user.getId());
        return dto;
    }

    /**
     * Copies vendor information to a VendorDTOInterface.
     * 
     * @param dto    The VendorDTOInterface to populate.
     * @param vendor The Vendor entity.
     * @return The populated VendorDTOInterface.
     * @throws IllegalArgumentException if the dto or vendor is null.
     */
    private static VendorDTOInterface copyVendorInfo(VendorDTOInterface dto, Vendor vendor) {
        validateUser(vendor);
        validateDTO(dto);
        dto = (VendorDTOInterface) copyUserInfo(dto, vendor);
        dto.setName(vendor.getName());
        dto.setPhone(vendor.getPhone());
        dto.setSize(vendor.getSize());
        dto.setMax_sqft(vendor.getMax_sqft());
        return dto;
    }

    /**
     * Copies customer information to a CustomerDTOInterface.
     * 
     * @param dto      The CustomerDTOInterface to populate.
     * @param customer The Customer entity.
     * @return The populated CustomerDTOInterface.
     * @throws IllegalArgumentException if the dto or customer is null.
     */
    private static CustomerDTOInterface copyCustomerInfo(CustomerDTOInterface dto, Customer customer) {
        validateUser(customer);
        validateDTO(dto);
        dto = (CustomerDTOInterface) copyUserInfo(dto, customer);
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setPhone(customer.getPhone());
        Set<String> addressesString = new HashSet<String>();
        for (Address address : customer.getAddresses()) {
            addressesString.add(address.toString());
        }
        dto.setAddresses(addressesString);
        return dto;
    }

    /**
     * Creates a new instance of VendorIncomingDTO.
     * 
     * @return A new VendorIncomingDTO object.
     */
    public static VendorIncomingDTO createVendorIncomingDTO() {
        return new VendorIncomingDTO();
    }

    /**
     * Copies a Vendor entity to a VendorIncomingDTO.
     * 
     * @param vendor The Vendor entity to copy from.
     * @return A populated VendorIncomingDTO object.
     * @throws IllegalArgumentException if the vendor is null.
     */
    public static VendorIncomingDTO copyToVendorIncomingDTO(Vendor vendor) {
        validateUser(vendor);
        VendorIncomingDTO dto = (VendorIncomingDTO) copyVendorInfo(createVendorIncomingDTO(), vendor);
        dto.setPassword(vendor.getPassword());
        return dto;
    }

    /**
     * Creates a new instance of VendorOutgoingDTO.
     * 
     * @return A new VendorOutgoingDTO object.
     */
    public static VendorOutgoingDTO createVendorOutgoingDTO() {
        return new VendorOutgoingDTO();
    }

    /**
     * Copies a Vendor entity to a VendorOutgoingDTO.
     * 
     * @param vendor The Vendor entity to copy from.
     * @return A populated VendorOutgoingDTO object.
     * @throws IllegalArgumentException if the vendor is null.
     */
    public static VendorOutgoingDTO copyToVendorOutgoingDTO(Vendor vendor) {
        validateUser(vendor);
        VendorOutgoingDTO dto = (VendorOutgoingDTO) copyVendorInfo(createVendorOutgoingDTO(), vendor);
        dto.setId(vendor.getId());
        return dto;
    }

    /**
     * Creates a new instance of CustomerIncomingDTO.
     * 
     * @return A new CustomerIncomingDTO object.
     */
    public static CustomerIncomingDTO createCustomerIncomingDTO() {
        return new CustomerIncomingDTO();
    }

    /**
     * Copies a Customer entity to a CustomerIncomingDTO.
     * 
     * @param customer The Customer entity to copy from.
     * @return A populated CustomerIncomingDTO object.
     * @throws IllegalArgumentException if the customer is null.
     */
    public static CustomerIncomingDTO copyToCustomerIncomingDTO(Customer customer) {
        validateUser(customer);
        CustomerIncomingDTO dto = (CustomerIncomingDTO) copyCustomerInfo(createCustomerIncomingDTO(), customer);
        dto.setPassword(customer.getPassword());
        return dto;
    }

    /**
     * Creates a new instance of CustomerOutgoingDTO.
     * 
     * @return A new CustomerOutgoingDTO object.
     */
    public static CustomerOutgoingDTO createCustomerOutgoingDTO() {
        return new CustomerOutgoingDTO();
    }

    /**
     * Copies a Customer entity to a CustomerOutgoingDTO.
     * 
     * @param customer The Customer entity to copy from.
     * @return A populated CustomerOutgoingDTO object.
     * @throws IllegalArgumentException if the customer is null.
     */
    public static CustomerOutgoingDTO copyToCustomerOutgoingDTO(Customer customer) {
        validateUser(customer);
        CustomerOutgoingDTO dto = (CustomerOutgoingDTO) copyCustomerInfo(createCustomerOutgoingDTO(), customer);
        dto.setId(customer.getId());
        return dto;
    }

    /**
     * Creates a new instance of ModifierDTO.
     * 
     * @return A new ModifierDTO object
     */
    public static ModifierDTO createModifierDTO() {
        return new ModifierDTO();
    }

    /**
     * Copies common information to a ModifierDTO from a User entity.
     *
     * @param dto  The ModifierDTO to populate.
     * @param user The User entity.
     * @return The populated ModifierDTO.
     * @throws IllegalArgumentException if the dto or user is null.
     */
    public static ModifierDTO copyToModifierDTO(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("No role given");
        }
        ModifierDTO dto = new ModifierDTO();
        dto.setName(role.getName());
        dto.setDescription(role.getDescription());
        return dto;
    }

    /**
     * Copies common information to a ModifierDTO from a User entity.
     *
     * @param dto  The ModifierDTO to populate.
     * @param user The User entity.
     * @return The populated ModifierDTO.
     * @throws IllegalArgumentException if the dto or user is null.
     */
    public static ModifierDTO copyToModifierDTOWithId(Role role) {
        ModifierDTO dto = copyToModifierDTO(role);
        dto.setId(role.getId());
        return dto;
    }

    /**
     * method to copy roles to a set of strings.
     * 
     * @param roles The set of Role entities.
     * @return A set of role names as strings.
     */
    public static Set<ModifierDTO> copyRolesToDTO(Set<Role> roles) {
        if (roles == null) {
            throw new IllegalArgumentException("No roles given");
        }
        Set<ModifierDTO> dto = new HashSet<ModifierDTO>();
        for (Role role : roles) {
            dto.add(copyToModifierDTO(role));
        }
        return dto;
    }

    /**
     * method to copy roles to a set of strings.
     * 
     * @param roles The set of Role entities.
     * @return A set of role names as strings.
     */
    public static Set<ModifierDTO> copyRolesToDTOWithId(Set<Role> roles) {
        if (roles == null) {
            throw new IllegalArgumentException("No roles given");
        }
        Set<ModifierDTO> dto = new HashSet<ModifierDTO>();
        for (Role role : roles) {
            dto.add(copyToModifierDTOWithId(role));
        }
        return dto;
    }
}
