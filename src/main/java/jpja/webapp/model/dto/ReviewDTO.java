package jpja.webapp.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * Data Transfer Object (DTO) for submitting a review for a booking.
 * This class encapsulates the data required to create or update a review,
 * including the booking ID, a score, and an optional review text.
 * 
 * <p>
 * Validation annotations are applied to enforce constraints on the review
 * score.
 * </p>
 * 
 * @author James Prial
 */
public class ReviewDTO implements DTOInterface {

    /**
     * The unique identifier for the booking being reviewed.
     */
    private Long bookingId;

    /**
     * The score of the review.
     * Must be between 1 and 5 (inclusive).
     */
    @Max(value = 5, message = "Score must not exceed 5.")
    @Min(value = 1, message = "Score must be at least 1.")
    private Double score;

    /**
     * The optional text of the review.
     */
    private String review;

    /**
     * Default constructor.
     * Initializes all fields to {@code null}.
     */
    public ReviewDTO() {
        this.bookingId = null;
        this.score = null;
        this.review = null;
    }

    /**
     * Constructor for creating a review with all required fields.
     *
     * @param bookingId the ID of the booking being reviewed
     * @param score     the score of the review (must be between 1 and 5)
     * @param review    the optional review text
     */
    public ReviewDTO(Long bookingId, @Max(5) @Min(1) Double score, String review) {
        this.bookingId = bookingId;
        this.score = score;
        this.review = review;
    }

    /**
     * Gets the booking ID for the review.
     *
     * @return the booking ID
     */
    public Long getBookingId() {
        return bookingId;
    }

    /**
     * Sets the booking ID for the review.
     *
     * @param bookingId the booking ID to set
     */
    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    /**
     * Gets the score of the review.
     *
     * @return the review score
     */
    public Double getScore() {
        return score;
    }

    /**
     * Sets the score of the review.
     * Must be between 1 and 5 (inclusive).
     *
     * @param score the review score to set
     */
    public void setScore(Double score) {
        this.score = score;
    }

    /**
     * Gets the text of the review.
     *
     * @return the review text
     */
    public String getReview() {
        return review;
    }

    /**
     * Sets the text of the review.
     *
     * @param review the review text to set
     */
    public void setReview(String review) {
        this.review = review;
    }
}
