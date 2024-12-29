package jpja.webapp.factories;

import java.util.HashSet;
import java.util.Set;

import jpja.webapp.model.dto.AddressDTO;
import jpja.webapp.model.dto.DTOInterface;
import jpja.webapp.model.dto.ModifierDTO;
import jpja.webapp.model.dto.ReviewDTO;
import jpja.webapp.model.entities.Address;

import jpja.webapp.model.entities.Booking;
import jpja.webapp.model.entities.Customer;
import jpja.webapp.model.entities.ModifierInterface;
import jpja.webapp.model.entities.Review;
import jpja.webapp.model.entities.User;
import jpja.webapp.model.entities.Vendor;

public class DTOMapper {
    /**
     * Converts an object to its corresponding DTO.
     * 
     * @param obj The object to convert to a DTO.
     * @return The corresponding DTOInterface instance, or null if the input is null.
     * @throws IllegalArgumentException if the object type does not have a corresponding DTO.
     */
    public static DTOInterface toDTO(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof User) {
            return handleUser((User) obj);
        } else if (obj instanceof Booking) {
            return BookingDTOFactory.copyToBookingDTO((Booking) obj);
        } else if (obj instanceof Address) {
            return createAddressDTO((Address) obj);
        } else if (obj instanceof Review) {
            return createReviewDTO((Review) obj);
        } else if (obj instanceof ModifierInterface) {
            return createModifierDTO((ModifierInterface) obj);
        } else {
            throw new IllegalArgumentException("No corresponding DTO");
        }
    }

    // Helper method to handle User conversion.
    private static DTOInterface handleUser(User user) {
        if (user instanceof Customer) {
            return UserDTOFactory.copyToCustomerOutgoingDTO((Customer) user);
        } else if (user instanceof Vendor) {
            return UserDTOFactory.copyToVendorOutgoingDTO((Vendor) user);
        } else {
            return UserDTOFactory.copyToUserOutgoingDTO(user);
        }
    }

    // Helper method to create AddressDTO from Address entity.
    private static AddressDTO createAddressDTO(Address address) {
        AddressDTO dto = new AddressDTO();
        dto.setId(address.getId());
        dto.setAddress("" + address.getStreetNumber() + " " + address.getStreetName());
        dto.setNickname(address.getNickname());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setZip(address.getZip());
        dto.setUnit(address.getUnit());
        dto.setTypeId(address.getTypes().iterator().next().getId());
        return dto;
    }

    // Helper method to create ReviewDTO from Review entity.
    private static ReviewDTO createReviewDTO(Review review) {
        return new ReviewDTO(review.getBookingId(), review.getScore(), review.getReview());
    }

    // Helper method to create ModifierDTO from ModifierInterface.
    private static ModifierDTO createModifierDTO(ModifierInterface modifier) {
        ModifierDTO dto = new ModifierDTO();
        dto.setId(modifier.getId());
        dto.setName(modifier.getName());
        dto.setDescription(modifier.getDescription());
        return dto;
    }

    /**
     * Converts a set of objects to a set of DTOs.
     * 
     * @param set The set of objects to convert to DTOs.
     * @return The set of corresponding DTOInterface instances.
     */
    public static <T> Set<DTOInterface> toDTOSet(Set<T> set) {
        Set<DTOInterface> ret = new HashSet<DTOInterface>();
        for (T item : set) {
            ret.add(toDTO(item));
        }
        return ret;
    }
}
