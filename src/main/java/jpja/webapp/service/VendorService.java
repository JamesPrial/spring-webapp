package jpja.webapp.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jpja.webapp.exceptions.database.ConflictingDataException;
import jpja.webapp.model.dto.VendorIncomingDTO;
import jpja.webapp.model.entities.Vendor;
import jpja.webapp.repositories.VendorRepository;

/**
 * Service class for managing vendor operations.
 * Provides functionality to find, modify, and save vendor data.
 * 
 * @author James Prial
 */
@Service
public class VendorService {

    private final VendorRepository vendorRepository;
    private final CustomUserDetailsService userDetailsService;

    /**
     * Constructs a new instance of VendorService with the provided dependencies.
     * 
     * @param vendorRepository   the repository for accessing vendor data
     * @param userDetailsService the service for managing user details
     */
    public VendorService(VendorRepository vendorRepository, CustomUserDetailsService userDetailsService) {
        this.vendorRepository = vendorRepository;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Finds a vendor by identifier, which can be username, email, or name.
     * 
     * @param identifier the vendor identifier
     * @return the {@link Vendor} object
     * @throws UsernameNotFoundException if no vendor is found
     */
    public Vendor findVendor(String identifier) {
        Optional<Vendor> optVendor = vendorRepository.findByUsername(identifier);
        if (optVendor.isEmpty()) {
            optVendor = vendorRepository.findByEmail(identifier);
            if (optVendor.isEmpty()) {
                optVendor = vendorRepository.findByName(identifier);
            }
        }
        if (optVendor.isEmpty()) {
            throw new UsernameNotFoundException("Vendor with identifier: " + identifier + " not found in database");
        }
        return optVendor.get();
    }

    /**
     * Finds a vendor by email.
     * 
     * @param email the vendor's email
     * @return the {@link Vendor} object
     * @throws UsernameNotFoundException if no vendor is found
     */
    public Vendor findVendorByEmail(String email) {
        return vendorRepository.findByEmail(email)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Vendor with email: " + email + " not found in database"));
    }

    /**
     * Finds a vendor by username.
     * 
     * @param username the vendor's username
     * @return the {@link Vendor} object
     * @throws UsernameNotFoundException if no vendor is found
     */
    public Vendor findVendorByUsername(String username) {
        return vendorRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Vendor with username: " + username + " not found in database"));
    }

    /**
     * Finds a vendor by ID.
     * 
     * @param id the vendor's ID
     * @return the {@link Vendor} object
     * @throws UsernameNotFoundException if no vendor is found
     */
    public Vendor findVendorById(Long id) {
        return vendorRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Vendor with id: " + id + " not found in database"));
    }

    /**
     * Finds a vendor by name.
     * 
     * @param name the vendor's name
     * @return the {@link Vendor} object
     * @throws UsernameNotFoundException if no vendor is found
     */
    public Vendor findVendorByName(String name) {
        return vendorRepository.findByName(name)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Vendor with name: " + name + " not found in database"));
    }

    /**
     * Saves a vendor entity to the database.
     * 
     * @param vendor the {@link Vendor} entity to save
     */
    public void saveVendor(Vendor vendor) {
        vendorRepository.save(vendor);
    }

    /**
     * Modifies a vendor's information based on a provided DTO.
     * 
     * @param identifier the identifier (username/email) of the vendor to modify
     * @param newInfo    the {@link VendorIncomingDTO} containing new vendor
     *                   information
     * @throws ConflictingDataException if there is conflicting data during
     *                                  modification
     */
    public void modifyVendor(String identifier, VendorIncomingDTO newInfo) throws ConflictingDataException {
        userDetailsService.modifyUser(identifier, newInfo);
        Vendor vendor = findVendor(identifier);
        if (newInfo.getName() != null) {
            vendor.setName(newInfo.getName());
        }
        if (newInfo.getPhone() != null) {
            vendor.setPhone(newInfo.getPhone());
        }
        if (newInfo.getSize() != null) {
            vendor.setSize(newInfo.getSize());
        }
        if (newInfo.getMax_sqft() != null) {
            vendor.setMax_sqft(vendor.getMax_sqft());
        }
        saveVendor(vendor);
    }
}
