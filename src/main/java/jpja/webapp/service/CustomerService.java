package jpja.webapp.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jpja.webapp.model.dto.CustomerIncomingDTO;
import jpja.webapp.model.entities.Address;
import jpja.webapp.model.entities.Customer;
import jpja.webapp.model.entities.User;
import jpja.webapp.repositories.CustomerRepository;

/**
 * Service class to handle operations related to customers.
 * Provides methods for retrieving, modifying, and managing customer-related data.
 *
 * @author James Prial
 */
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final AuthenticationService authenticationService;
    private final AddressService addressService;

    /**
     * Constructs the service with necessary dependencies.
     *
     * @param customerRepository Repository for managing customer entities.
     * @param authenticationService Service for authentication-related operations.
     * @param addressService Service for managing addresses.
     */
    public CustomerService(CustomerRepository customerRepository, AuthenticationService authenticationService,
                           AddressService addressService) {
        this.customerRepository = customerRepository;
        this.authenticationService = authenticationService;
        this.addressService = addressService;
    }

    /**
     * Finds a customer using either their username or email.
     *
     * @param identifier Username or email of the customer.
     * @return The found customer.
     * @throws UsernameNotFoundException If no customer is found with the given identifier.
     */
    public Customer findCustomer(String identifier) {
        Optional<Customer> optCustomer = customerRepository.findByUsername(identifier);
        if (optCustomer.isEmpty()) {
            optCustomer = customerRepository.findByEmail(identifier);
        }
        if (optCustomer.isEmpty()) {
            throw new UsernameNotFoundException("Customer with identifier: " + identifier + " not found in database");
        }
        return optCustomer.get();
    }

    /**
     * Finds a customer using their email.
     *
     * @param email Email of the customer.
     * @return The found customer.
     * @throws UsernameNotFoundException If no customer is found with the given email.
     */
    public Customer findCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Customer with email: " + email + " not found in database"));
    }

    /**
     * Finds a customer using their username.
     *
     * @param username Username of the customer.
     * @return The found customer.
     * @throws UsernameNotFoundException If no customer is found with the given username.
     */
    public Customer findCustomerByUsername(String username) {
        return customerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Customer with username: " + username + " not found in database"));
    }

    /**
     * Finds a customer using their ID.
     *
     * @param id ID of the customer.
     * @return The found customer.
     * @throws UsernameNotFoundException If no customer is found with the given ID.
     */
    public Customer findCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Customer with id: " + id + " not found in database"));
    }

    /**
     * Saves a customer entity.
     *
     * @param customer The customer to save.
     */
    public void saveCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    /**
     * Adds an address to a customer's address set.
     *
     * @param customer The customer to whom the address will be added.
     * @param address The address to add.
     * @return True if the address was successfully added, false otherwise.
     */
    public boolean addAddress(Customer customer, Address address) {
        if (customer == null || address == null) {
            throw new IllegalArgumentException("customer/address not given");
        }
        Set<Address> addresses = customer.getAddresses();
        if (addresses == null) {
            addresses = new HashSet<>();
            customer.setAddresses(addresses);
        }
        boolean ret = addresses.add(address);
        if (ret) {
            customerRepository.save(customer);
        }
        return ret;
    }

    /**
     * Retrieves all addresses associated with a customer.
     *
     * @param customer The customer whose addresses are to be retrieved.
     * @return A set of addresses belonging to the customer.
     */
    public Set<Address> getAddresses(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("customer not given");
        }
        return customer.getAddresses();
    }

    /**
     * Checks if a specific address belongs to a given customer.
     *
     * @param customer The customer to check against.
     * @param address The address to verify.
     * @return True if the address belongs to the customer, false otherwise.
     */
    public boolean addressBelongsToUser(Customer customer, Address address) {
        return customer != null && address != null && customer.getAddresses().contains(address);
    }

    /**
     * Checks if an address, identified by its ID, belongs to a given user.
     *
     * @param user The user to check against.
     * @param addressId The ID of the address to verify.
     * @return True if the address belongs to the user, false otherwise.
     */
    public boolean addressBelongsToUser(User user, Long addressId) {
        return user != null && addressId != null && user instanceof Customer
                && addressBelongsToUser((Customer) user, addressService.getAddressById(addressId));
    }



        /**
     * Retrieves the currently authenticated customer.
     *
     * @return The authenticated customer.
     * @throws IllegalStateException If the current user is not a customer.
     */
    public Customer getCurrentCustomer() {
        User currentUser = authenticationService.getCurrentUser();
        if (currentUser instanceof Customer) {
            return (Customer) currentUser;
        }
        throw new IllegalStateException("Current user not customer");
    }

    /**
     * Modifies a customer's details based on the provided DTO.
     *
     * @param customer The customer to modify.
     * @param dto The DTO containing updated customer information.
     * @throws IllegalArgumentException If either the customer or DTO is null.
     */
    public void modifyCustomer(Customer customer, CustomerIncomingDTO dto) {
        if (customer == null || dto == null) {
            throw new IllegalArgumentException("Customer/dto null");
        }
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setPhone(dto.getPhone());
        customerRepository.save(customer);
    }
}