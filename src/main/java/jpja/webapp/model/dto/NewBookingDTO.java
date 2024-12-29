package jpja.webapp.model.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jpja.webapp.validation.annotation.ValidAddressId;
import jpja.webapp.validation.annotation.ValidBookingDate;
import jpja.webapp.validation.annotation.ValidBookingTime;
import jpja.webapp.validation.annotation.ValidModifierId;

/**
 * Data Transfer Object (DTO) for creating a new booking.
 * This class encapsulates the data required to schedule a booking, including
 * the date, time, address, and type of booking.
 * 
 * <p>
 * Validation annotations are applied to ensure the integrity and correctness
 * of the input data, such as scheduling constraints and valid identifiers.
 * </p>
 * 
 * @author James Prial
 */
public class NewBookingDTO implements DTOInterface {

    /**
     * The date of the booking.
     * Must be scheduled at least 7 days in advance.
     */
    @ValidBookingDate(days = 7, message = "Must schedule at least a week in advance.")
    private LocalDate date;

    /**
     * The time of the booking.
     * Must fall between the hours of 6:00 AM and 8:00 PM.
     */
    @ValidBookingTime(start = "06:00", end = "20:00")
    private LocalTime time;

    /**
     * The identifier for the address associated with the booking.
     * Must reference a valid address.
     */
    @ValidAddressId
    private Long addressId;

    /**
     * The identifier for the type of booking.
     * Must reference a valid modifier type.
     */
    @ValidModifierId
    private Long typeId;

    /**
     * Constructor for creating a new booking DTO with all required fields.
     * 
     * @param date      the date of the booking
     * @param time      the time of the booking
     * @param addressId the ID of the associated address
     * @param typeId    the ID of the booking type
     */
    public NewBookingDTO(
            @ValidBookingDate(days = 7, message = "Must schedule at least a week in advance.") LocalDate date,
            @ValidBookingTime(start = "06:00", end = "20:00") LocalTime time,
            @ValidAddressId Long addressId,
            @ValidModifierId Long typeId) {
        this.date = date;
        this.time = time;
        this.addressId = addressId;
        this.typeId = typeId;
    }

    /**
     * Gets the date of the booking.
     *
     * @return the booking date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date of the booking.
     *
     * @param date the booking date to set
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Gets the time of the booking.
     *
     * @return the booking time
     */
    public LocalTime getTime() {
        return time;
    }

    /**
     * Sets the time of the booking.
     *
     * @param time the booking time to set
     */
    public void setTime(LocalTime time) {
        this.time = time;
    }

    /**
     * Gets the identifier for the address associated with the booking.
     *
     * @return the address ID
     */
    public Long getAddressId() {
        return addressId;
    }

    /**
     * Sets the identifier for the address associated with the booking.
     *
     * @param addressId the address ID to set
     */
    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    /**
     * Gets the identifier for the type of booking.
     *
     * @return the type ID
     */
    public Long getTypeId() {
        return typeId;
    }

    /**
     * Sets the identifier for the type of booking.
     *
     * @param typeId the type ID to set
     */
    public void setType(Long typeId) {
        this.typeId = typeId;
    }
}
