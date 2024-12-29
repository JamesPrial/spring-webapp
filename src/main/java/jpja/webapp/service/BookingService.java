package jpja.webapp.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import jpja.webapp.exceptions.database.ConflictingModifiersException;
import jpja.webapp.exceptions.database.DatabaseException;
import jpja.webapp.exceptions.database.ExpectedDataNotFoundException;
import jpja.webapp.model.dto.ModifierDTO;
import jpja.webapp.model.dto.NewBookingDTO;
import jpja.webapp.model.entities.Booking;
import jpja.webapp.model.entities.BookingModifier;
import jpja.webapp.model.entities.Customer;
import jpja.webapp.model.entities.User;
import jpja.webapp.model.entities.Vendor;
import jpja.webapp.repositories.BookingRepository;
import jpja.webapp.repositories.ModifierRepository;

/**
 * Service class for managing bookings and associated operations.
 * Handles logic for retrieving, modifying, and saving booking entities, 
 * along with associated modifiers and status changes.
 * 
 * @author James Prial
 */
@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ModifierRepository modifierRepository;
    private final AddressService addressService;

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    /**
     * Constructs a BookingService with the necessary dependencies.
     * 
     * @param bookingRepository Repository for managing booking entities.
     * @param modifierRepository Repository for managing booking modifier entities.
     * @param addressService Service for handling address-related logic.
     */
    public BookingService(BookingRepository bookingRepository, ModifierRepository modifierRepository, AddressService addressService) {
        this.bookingRepository = bookingRepository;
        this.modifierRepository = modifierRepository;
        this.addressService = addressService;
    }

    /**
     * Saves a booking entity to the database.
     * 
     * @param booking The booking entity to save.
     * @throws IllegalArgumentException if the booking is null.
     */
    public void saveBooking(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking not given");
        }
        bookingRepository.save(booking);
    }

    /**
     * Retrieves bookings associated with a specific modifier.
     * 
     * @param modifier The booking modifier to filter by.
     * @return A set of bookings associated with the given modifier.
     */
    public Set<Booking> getBookingsByModifier(BookingModifier modifier) {
        return bookingRepository.findByModifiers(modifier);
    }

    /**
     * Retrieves bookings associated with a specific modifier name.
     * 
     * @param modifier The name of the booking modifier.
     * @return A set of bookings associated with the given modifier name.
     * @throws IllegalArgumentException if the modifier is not found in the repository.
     */
    public Set<Booking> getBookingsByModifier(String modifier) {
        return bookingRepository.findByModifiers(this.findModifier(modifier).orElseThrow(
                () -> new IllegalArgumentException(modifier + " not found in modifierRepo")));
    }

    /**
     * Retrieves bookings associated with a specific type and modifier name.
     * 
     * @param type The type of the booking modifier.
     * @param modifier The name of the booking modifier.
     * @return A set of bookings associated with the given type and modifier.
     * @throws IllegalArgumentException if the type or modifier is not found in the repository.
     */
    public Set<Booking> getBookingsByModifier(String type, String modifier) {
        return bookingRepository.findByModifiers(this.findModifier(type + "_" + modifier).orElseThrow(
                () -> new IllegalArgumentException(type + ", " + modifier + " not found in modifierRepo")));
    }

    /**
     * Retrieves all modifiers of a specific type.
     * 
     * @param type The type of modifiers to retrieve.
     * @return A set of modifier DTOs of the specified type.
     * @throws IllegalArgumentException if the type is null.
     */
    public Set<ModifierDTO> getModifiersByType(String type) {
        if (type == null) {
            throw new IllegalArgumentException("Type not given");
        }
        Set<BookingModifier> mods = modifierRepository.findByType(type);
        Set<ModifierDTO> ret = new HashSet<>();
        for (BookingModifier mod : mods) {
            ModifierDTO dto = new ModifierDTO();
            dto.setId((long) mod.getId());
            dto.setName(mod.getName());
            dto.setDescription(mod.getDescription());
            ret.add(dto);
        }
        return ret;
    }

    /**
     * Allows a vendor to claim a booking that is currently unclaimed.
     * 
     * @param bookingId The ID of the booking to claim.
     * @param vendor The vendor claiming the booking.
     * @return true if the booking was successfully claimed, false otherwise.
     */
    public boolean claimBooking(long bookingId, Vendor vendor) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();
            try {
                if (booking.getVendor() == null && booking.getStatus().getName().equals("UNCLAIMED")) {
                    booking.setVendor(vendor);
                    return setBookingStatus(booking, "CLAIMED");
                }
            } catch (DatabaseException e) {
                if (e instanceof ConflictingModifiersException) {
                    Set<BookingModifier> statuses = booking.getModifiersByType("STATUS");
                    String conflictingStatuses = "|";
                    for (BookingModifier modifier : statuses) {
                        conflictingStatuses = conflictingStatuses + modifier.toString() + "|";
                    }
                    logger.error("ConflictingModifiersException thrown for booking id " + booking.getId()
                            + " when attempting to claim booking. STATUSes: " + conflictingStatuses);
                } else if (e instanceof ExpectedDataNotFoundException) {
                    logger.error("ExpectedDataNotFoundException thrown for booking id " + booking.getId()
                            + "when attempting to claim booking. No status previously set");
                } else {
                    logger.error("Unrecognized DatabaseException", e);
                }
            }
        }
        return false;
    }
    /**
     * Retrieves all bookings with the specified status.
     * 
     * @param status The status to filter bookings by.
     * @return A set of bookings with the given status.
     */
    public Set<Booking> getBookingsByStatus(String status) {
        return getBookingsByModifier("STATUS", status);
    }

    /**
     * Retrieves all bookings associated with a specific vendor.
     * 
     * @param vendor The vendor whose bookings are to be retrieved.
     * @return A set of bookings linked to the given vendor.
     */
    public Set<Booking> getBookingsByVendor(Vendor vendor) {
        return bookingRepository.findByVendor(vendor);
    }

    /**
     * Retrieves all bookings associated with a specific customer.
     * 
     * @param customer The customer whose bookings are to be retrieved.
     * @return A set of bookings linked to the given customer.
     */
    public Set<Booking> getBookingsByCustomer(Customer customer) {
        return bookingRepository.findByCustomer(customer);
    }

    /**
     * Retrieves bookings associated with a specific customer and a specific modifier.
     * 
     * @param customer The customer whose bookings are to be retrieved.
     * @param modifier The modifier to filter bookings by.
     * @return A set of bookings linked to the given customer and modifier.
     */
    public Set<Booking> getBookingsByCustomerAndModifier(Customer customer, BookingModifier modifier) {
        return bookingRepository.findByCustomerAndModifiers(customer, modifier);
    }

    /**
     * Retrieves bookings associated with a specific vendor and a specific modifier.
     * 
     * @param vendor The vendor whose bookings are to be retrieved.
     * @param modifier The modifier to filter bookings by.
     * @return A set of bookings linked to the given vendor and modifier.
     */
    public Set<Booking> getBookingsByVendorAndModifier(Vendor vendor, BookingModifier modifier) {
        return bookingRepository.findByVendorAndModifiers(vendor, modifier);
    }

    /**
     * Retrieves bookings associated with a user and a specific modifier.
     * 
     * @param user The user whose bookings are to be retrieved.
     * @param modifier The modifier to filter bookings by.
     * @return A set of bookings linked to the given user and modifier.
     * @throws IllegalArgumentException if the user type is unrecognized.
     */
    public Set<Booking> getBookingsByUserAndModifier(User user, BookingModifier modifier) {
        if (user instanceof Customer) {
            return getBookingsByCustomerAndModifier((Customer) user, modifier);
        } else if (user instanceof Vendor) {
            return getBookingsByVendorAndModifier((Vendor) user, modifier);
        } else {
            throw new IllegalArgumentException("Unrecognized User type");
        }
    }

    /**
     * Retrieves a booking by its ID.
     * 
     * @param id The ID of the booking to retrieve.
     * @return The booking with the given ID.
     * @throws IllegalArgumentException if the booking is not found.
     */
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking with id: " + id + " not found"));
    }

    /**
     * Finds a modifier by its name or type_name combination.
     * 
     * @param modifier The name or type_name of the modifier.
     * @return An optional containing the modifier if found.
     * @throws IllegalArgumentException if the modifier format is invalid or duplicates exist.
     */
    public Optional<BookingModifier> findModifier(String modifier) {
        String[] splitModifier = modifier.toUpperCase().split("_");
        Optional<BookingModifier> ret = Optional.empty();
        if (splitModifier.length == 2) {
            ret = modifierRepository.findByTypeAndName(splitModifier[0], splitModifier[1]);
        } else if (splitModifier.length == 1) {
            Set<BookingModifier> mods = modifierRepository.findByName(splitModifier[0]);
            if (mods.size() > 1) {
                throw new IllegalArgumentException("duplicate modifiers: specify type");
            } else if (mods.size() == 1) {
                ret = mods.stream().findFirst();
            } else {
                ret = Optional.empty();
            }
        } else {
            throw new IllegalArgumentException("improperly formatted modifier");
        }
        return ret;
    }

    /**
     * Sets the status of a booking using a BookingModifier.
     * 
     * @param booking The booking whose status is to be set.
     * @param status The BookingModifier representing the status.
     * @return true if the status was successfully set, false otherwise.
     * @throws IllegalArgumentException if the status is not of type STATUS.
     */
    public boolean setBookingStatus(Booking booking, BookingModifier status) {
        if (!status.getType().equals("STATUS")) {
            throw new IllegalArgumentException("modifier not of type STATUS");
        }
        try {
            booking.removeModifier(booking.getStatus());
        } catch (DatabaseException e) {
            if (e instanceof ConflictingModifiersException) {
                Set<BookingModifier> statuses = booking.getModifiersByType("STATUS");
                String conflictingStatuses = "|";
                for (BookingModifier modifier : statuses) {
                    conflictingStatuses = conflictingStatuses + modifier.toString() + "|";
                    booking.removeModifier(modifier);
                }
                logger.warn("ConflictingModifiersException thrown for booking id " + booking.getId()
                        + " when setting status. Previous STATUSes: " + conflictingStatuses);
            } else if (e instanceof ExpectedDataNotFoundException) {
                logger.warn("ExpectedDataNotFoundException thrown for booking id " + booking.getId()
                        + "when setting status. No status previously set");
            } else {
                throw new IllegalArgumentException("Unrecognized DatabaseException", e);
            }
        }
        boolean ret = booking.addModifier(status);
        saveBooking(booking);
        return ret;
    }

    /**
     * Retrieves all unclaimed bookings.
     * 
     * @return A set of bookings with the status "UNCLAIMED".
     * @throws IllegalArgumentException if the "UNCLAIMED" status modifier is not found.
     */
    public Set<Booking> getUnclaimedBookings() {
        return bookingRepository.findByModifiers(
                findModifier("STATUS_UNCLAIMED").orElseThrow(() -> new IllegalArgumentException()));
    }

    /**
     * Sets the status of a booking using a status string.
     * 
     * @param booking The booking whose status is to be set.
     * @param statusAsString The status as a string.
     * @return true if the status was successfully set, false otherwise.
     * @throws IllegalArgumentException if the status is invalid.
     */
    public boolean setBookingStatus(Booking booking, String statusAsString) {
        BookingModifier status = modifierRepository
                .findByTypeAndName("STATUS", statusAsString)
                .orElseThrow(() -> new IllegalArgumentException(statusAsString + " is not a valid STATUS."));
        return setBookingStatus(booking, status);
    }

    /**
     * Retrieves the total number of bookings.
     * 
     * @return The total number of bookings in the database.
     */
    public long totalBookings() {
        return bookingRepository.count();
    }

    /**
     * Calculates the total earnings for a given vendor.
     * 
     * @param vendor The vendor whose earnings are to be calculated.
     * @return The total earnings of the vendor from completed bookings.
     * @throws IllegalArgumentException if the "COMPLETED" status modifier is not found.
     */
    public double calculateTotalEarnings(Vendor vendor) {
        Set<Booking> bookings = bookingRepository.findByVendorAndModifiers(
                vendor, findModifier("COMPLETED").orElseThrow(() -> new IllegalArgumentException()));
        double total = 0.0;
        for (Booking booking : bookings) {
            total += booking.getPrice();
        }
        return total;
    }
    /**
     * Calculates the total pending earnings for a given vendor.
     * 
     * @param vendor The vendor whose pending earnings are to be calculated.
     * @return The total pending earnings for the vendor from bookings marked as "PENDING_PAYOUT".
     * @throws IllegalArgumentException if the "PENDING_PAYOUT" status modifier is not found.
     */
    public double calculatePendingEarnings(Vendor vendor) {
        Set<Booking> bookings = bookingRepository.findByVendorAndModifiers(
                vendor, findModifier("PENDING_PAYOUT").orElseThrow(() -> new IllegalArgumentException()));
        double total = 0.0;
        for (Booking booking : bookings) {
            total += booking.getPrice();
        }
        return total;
    }

    /**
     * Retrieves upcoming bookings for a given user.
     * 
     * @param user The user whose upcoming bookings are to be retrieved.
     * @return A set of upcoming bookings for the user.
     * @throws IllegalArgumentException if the user type is invalid.
     */
    public Set<Booking> getUpcomingBookings(User user) {
        if (user instanceof Vendor || user instanceof Customer) {
            Set<Booking> allBookings = user instanceof Vendor
                    ? getBookingsByVendor((Vendor) user)
                    : getBookingsByCustomer((Customer) user);
            Set<Booking> ret = new HashSet<>();
            for (Booking booking : allBookings) {
                if (booking.getBookingDate().isAfter(LocalDate.now()) || 
                   (booking.getBookingDate().isEqual(LocalDate.now()) && booking.getBookingTime().isAfter(LocalTime.now()))) {
                    ret.add(booking);
                }
            }
            return ret;
        }
        throw new IllegalArgumentException("Invalid User");
    }

    /**
     * Checks if a booking belongs to a specific customer.
     * 
     * @param booking The booking to check.
     * @param customer The customer to verify.
     * @return true if the booking belongs to the customer, false otherwise.
     * @throws IllegalArgumentException if booking or customer is null.
     */
    public boolean doesBookingBelongToCustomer(Booking booking, Customer customer) {
        if (booking == null || customer == null) {
            throw new IllegalArgumentException("Booking or Customer not given");
        }
        return booking.getCustomer().equals(customer);
    }

    /**
     * Checks if a booking belongs to a specific vendor.
     * 
     * @param booking The booking to check.
     * @param vendor The vendor to verify.
     * @return true if the booking belongs to the vendor, false otherwise.
     * @throws IllegalArgumentException if booking or vendor is null.
     */
    public boolean doesBookingBelongToVendor(Booking booking, Vendor vendor) {
        if (booking == null || vendor == null) {
            throw new IllegalArgumentException("Booking or Vendor not given");
        }
        return booking.getVendor().equals(vendor);
    }

    /**
     * Checks if a booking belongs to a specific user.
     * 
     * @param booking The booking to check.
     * @param user The user to verify.
     * @return true if the booking belongs to the user, false otherwise.
     * @throws IllegalArgumentException if booking or user is null, or if the user type is invalid.
     */
    public boolean doesBookingBelongToUser(Booking booking, User user) {
        if (booking == null || user == null) {
            throw new IllegalArgumentException("Booking or User not given");
        }
        if (user instanceof Customer) {
            return doesBookingBelongToCustomer(booking, (Customer) user);
        } else if (user instanceof Vendor) {
            return doesBookingBelongToVendor(booking, (Vendor) user);
        } else {
            throw new IllegalArgumentException("Illegal User type");
        }
    }

    /**
     * Modifies a booking's date and time.
     * 
     * @param booking The booking to modify.
     * @param newDate The new booking date.
     * @param newTime The new booking time.
     * @return true if the booking was successfully modified, false otherwise.
     * @throws IllegalArgumentException if booking, date, or time is null, or if the new date/time is invalid.
     */
    public boolean modifyBooking(Booking booking, LocalDate newDate, LocalTime newTime) {
        if (booking == null || newDate == null || newTime == null) {
            throw new IllegalArgumentException("Booking or date or time not given");
        }
        if (newDate.isAfter(LocalDate.now().plusDays(1)) &&
            newTime.isAfter(LocalTime.NOON.minusHours(4)) &&
            newTime.isBefore(LocalTime.NOON.plusHours(8))) {
            booking.setBookingDate(newDate);
            booking.setBookingTime(newTime);
            setBookingStatus(booking, "UNCLAIMED");
            booking.setVendor(null);
            saveBooking(booking);
            return true;
        }
        return false;
    }

    /**
     * Cancels a booking by setting its status to "CANCELED".
     * 
     * @param booking The booking to cancel.
     * @return true if the booking was successfully canceled, false otherwise.
     * @throws IllegalArgumentException if the booking is null.
     */
    public boolean cancelBooking(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking not given");
        }
        return setBookingStatus(booking, "CANCELED");
    }

    /**
     * Creates a new booking for a customer.
     * 
     * @param bookingInfo The details of the new booking.
     * @param customer The customer for whom the booking is created.
     * @throws IllegalArgumentException if booking info or customer is null.
     */
    public void createBooking(@Valid NewBookingDTO bookingInfo, Customer customer) {
        if (bookingInfo == null || customer == null) {
            throw new IllegalArgumentException("Booking/customer not given");
        }
        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setBookingDate(bookingInfo.getDate());
        booking.setBookingTime(bookingInfo.getTime());
        booking.addModifier(findModifier("UNCLAIMED").orElseThrow(() -> new IllegalArgumentException()));
        booking.addModifier(getModifierById(bookingInfo.getTypeId()));
        booking.setLocation(addressService.getAddressById(bookingInfo.getAddressId()));
        booking.setPrice(0.0); // Placeholder
        saveBooking(booking);
    }

    /**
     * Retrieves a BookingModifier by its ID.
     * 
     * @param id The ID of the modifier.
     * @return The BookingModifier with the specified ID.
     * @throws IllegalArgumentException if no modifier is found with the given ID.
     */
    public BookingModifier getModifierById(Long id) {
        return modifierRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("BookingModifier Id not found"));
    }
}

