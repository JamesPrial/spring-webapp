package jpja.webapp.service;

import org.springframework.stereotype.Service;

import jpja.webapp.model.dto.ReviewDTO;
import jpja.webapp.model.entities.Booking;
import jpja.webapp.model.entities.Review;
import jpja.webapp.repositories.ReviewRepository;

/**
 * Service for managing and processing reviews.
 * Provides methods for saving reviews, checking their existence, and converting
 * DTOs into entities.
 * 
 * @author James Prial
 */
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingService bookingService;

    /**
     * Constructs a new instance of ReviewService with the provided repositories and
     * services.
     * 
     * @param reviewRepository the repository for managing review data
     * @param bookingService   the service for managing bookings
     */
    public ReviewService(ReviewRepository reviewRepository, BookingService bookingService) {
        this.reviewRepository = reviewRepository;
        this.bookingService = bookingService;
    }

    /**
     * Saves the provided review to the repository.
     * 
     * @param review the {@link Review} to save
     */
    public void saveReview(Review review) {
        reviewRepository.save(review);
    }

    /**
     * Checks if a review exists for a given booking ID.
     * 
     * @param bookingId the ID of the booking
     * @return {@code true} if a review exists, {@code false} otherwise
     */
    public boolean doesReviewExist(Long bookingId) {
        return reviewRepository.existsById(bookingId);
    }

    /**
     * Checks if a review exists for a given booking.
     * 
     * @param booking the {@link Booking} to check
     * @return {@code true} if a review exists, {@code false} otherwise
     */
    public boolean doesReviewExist(Booking booking) {
        return doesReviewExist(booking.getId());
    }

    /**
     * Converts a {@link ReviewDTO} into a {@link Review} entity.
     * Validates that the DTO and associated booking data are provided and that no
     * existing review exists for the booking.
     * 
     * @param dto the {@link ReviewDTO} to convert
     * @return a {@link Review} entity created from the DTO
     * @throws IllegalArgumentException if the DTO is null, lacks a booking ID, or
     *                                  if a review already exists for the booking
     */
    public Review unwrapNewReviewDTO(ReviewDTO dto) {
        if (dto == null || dto.getBookingId() == null || doesReviewExist(dto.getBookingId())) {
            throw new IllegalArgumentException("No Review given, or review already exists");
        }
        Review review = new Review();
        review.setBookingId(dto.getBookingId());
        review.setBooking(bookingService.getBookingById(dto.getBookingId()));
        review.setScore(dto.getScore());
        review.setReview(dto.getReview());
        return review;
    }
}
