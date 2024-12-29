package jpja.webapp.factories;

import java.util.HashSet;
import java.util.Set;

import jpja.webapp.model.dto.AddressDTO;
import jpja.webapp.model.dto.BookingDTO;
import jpja.webapp.model.dto.ReviewDTO;
import jpja.webapp.model.entities.Booking;
import jpja.webapp.model.entities.BookingModifier;
import jpja.webapp.model.entities.Review;
import jpja.webapp.model.entities.User;

public class BookingDTOFactory {
    /** 
     * Creates a new, blank, BookingDTO 
     */
    public static BookingDTO createBookingDTO(){
        return new BookingDTO();
    }

    /** 
     * Copies data from a Booking entity to a BookingDTO.
     * 
     * @param booking The Booking entity to copy from.
     * @return A BookingDTO populated with data from the Booking entity.
     * @throws IllegalArgumentException if the provided booking is null.
     */
    public static BookingDTO copyToBookingDTO(Booking booking){
        if(booking == null){
            throw new IllegalArgumentException("No booking given");
        }
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());
        dto.setCustomerIdentifier(booking.getCustomer().getUsername());
        User vendor = booking.getVendor();
        if(vendor == null){
            dto.setVendorIdentifier(null);
        }else{
            dto.setVendorIdentifier(booking.getVendor().getUsername());
        }
        
        dto.setCreatedAt(booking.getCreatedAt());
        dto.setBookingDate(booking.getBookingDate());
        dto.setBookingTime(booking.getBookingTime());
        dto.setPrice(booking.getPrice());
        Set<String> modifiers = new HashSet<String>();
        for(BookingModifier modifier : booking.getModifiers()){
            modifiers.add(modifier.toString());
        }
        dto.setModifiers(modifiers);
        dto.setStatus(booking.getStatus().getName());
        dto.setLocation((AddressDTO)DTOMapper.toDTO(booking.getLocation()));
        return dto;
    }

    /** 
     * Copies a set of Booking entities to a set of BookingDTOs.
     * 
     * @param bookings The set of Booking entities to copy from.
     * @return A Set of BookingDTOs populated with data from the Booking entities.
     * @throws IllegalArgumentException if the provided set of bookings is null.
     */
    public static Set<BookingDTO> copyBookingSetToDTO(Set<Booking> bookings){
        if(bookings == null){
            throw new IllegalArgumentException("No bookings given");
        }
        Set<BookingDTO> dtos = new HashSet<BookingDTO>();
        for(Booking booking : bookings){
            dtos.add(copyToBookingDTO(booking));
        }
        return dtos;
    }


    /** 
     * Creates a new, blank, ReviewDTO. 
     */
    public static ReviewDTO createReviewDTO(){
        return new ReviewDTO();
    }

    /** 
     * Copies data from a Review entity to a ReviewDTO.
     * 
     * @param review The Review entity to copy from.
     * @return A ReviewDTO populated with data from the Review entity.
     * @throws IllegalArgumentException if the provided review is null.
     */
    public static ReviewDTO copyToReviewDTO(Review review){
        return new ReviewDTO(review.getBookingId(), review.getScore(), review.getReview());
    }
}
