package jpja.webapp.model.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;

/**
 * Entity representing a review for a booking.
 * This class maps to the "reviews" table in the database and is associated
 * with a {@link Booking} entity. Each review contains a score, optional text,
 * and the timestamp when it was submitted.
 * 
 * <p>The `Review` entity uses the booking ID as its primary key, linking directly to the corresponding booking.</p>
 * 
 * @author James Prial
 */
@Entity
@Table(name = "reviews")
public class Review {

    /**
     * The booking ID associated with this review.
     * Acts as the primary key, shared with the {@link Booking} entity.
     */
    @Id
    private Long bookingId;

    /**
     * The booking entity associated with this review.
     * Mapped as a one-to-one relationship using the same ID.
     */
    @OneToOne
    @MapsId
    @JoinColumn(name = "booking_id", columnDefinition = "int unsigned")
    private Booking booking;

    /**
     * The score of the review, ranging from 1 to 5.
     * Stored as an unsigned tiny integer in the database.
     */
    @Column(name = "score", columnDefinition = "tinyint unsigned", nullable = false)
    private Double score;

    /**
     * The timestamp indicating when the review was submitted.
     * Automatically generated when the review is created.
     */
    @CreationTimestamp
    @Column(name = "datetime_reviewed", nullable = false)
    private LocalDateTime datetimeReviewed;

    /**
     * The textual review provided by the user.
     * This field is optional and allows for detailed feedback.
     */
    @Column(name = "review", columnDefinition = "text", nullable = true)
    private String review;

    /**
     * Default constructor.
     * Initializes all fields to their default values.
     */
    public Review() {
    }

    /**
     * Gets the booking ID associated with this review.
     *
     * @return the booking ID
     */
    public Long getBookingId() {
        return bookingId;
    }

    /**
     * Sets the booking ID associated with this review.
     *
     * @param bookingId the booking ID to set
     */
    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    /**
     * Gets the booking associated with this review.
     *
     * @return the booking entity
     */
    public Booking getBooking() {
        return booking;
    }

    /**
     * Sets the booking associated with this review.
     *
     * @param booking the booking entity to set
     */
    public void setBooking(Booking booking) {
        this.booking = booking;
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
     *
     * @param score the review score to set
     */
    public void setScore(Double score) {
        this.score = score;
    }

    /**
     * Gets the timestamp indicating when the review was submitted.
     *
     * @return the timestamp of the review
     */
    public LocalDateTime getDatetimeReviewed() {
        return datetimeReviewed;
    }

    /**
     * Sets the timestamp indicating when the review was submitted.
     *
     * @param datetimeReviewed the timestamp to set
     */
    public void setDatetimeReviewed(LocalDateTime datetimeReviewed) {
        this.datetimeReviewed = datetimeReviewed;
    }

    /**
     * Gets the textual review provided by the user.
     *
     * @return the textual review
     */
    public String getReview() {
        return review;
    }

    /**
     * Sets the textual review provided by the user.
     *
     * @param review the textual review to set
     */
    public void setReview(String review) {
        this.review = review;
    }
}
