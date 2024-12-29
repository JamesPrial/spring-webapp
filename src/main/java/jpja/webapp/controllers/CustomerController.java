package jpja.webapp.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import jpja.webapp.factories.BookingDTOFactory;
import jpja.webapp.factories.DTOMapper;
import jpja.webapp.factories.UserDTOFactory;
import jpja.webapp.model.dto.BookingDTO;
import jpja.webapp.model.dto.CustomerIncomingDTO;
import jpja.webapp.model.dto.DTOInterface;
import jpja.webapp.model.dto.NewBookingDTO;
import jpja.webapp.model.dto.ReviewDTO;
import jpja.webapp.model.entities.Booking;
import jpja.webapp.model.entities.Customer;
import jpja.webapp.service.AuthenticationService;
import jpja.webapp.service.BookingService;
import jpja.webapp.service.CustomerService;
import jpja.webapp.service.LoggingService;
import jpja.webapp.service.ReviewService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller responsible for handling customer-related requests,
 * including dashboard, booking management, profile updates, and reviews.
 * 
 * @author James Prial
 */
@Controller
@RequestMapping("/customer")
public class CustomerController {
    private final BookingService bookingService;
    private final ReviewService reviewService;
    private final AuthenticationService authenticationService;
    private final CustomerService customerService;
    private final LoggingService loggingService;

    public CustomerController(BookingService bookingService, ReviewService reviewService,
            AuthenticationService authenticationService,
            CustomerService customerService, LoggingService loggingService) {
        this.bookingService = bookingService;
        this.reviewService = reviewService;
        this.authenticationService = authenticationService;
        this.customerService = customerService;
        this.loggingService = loggingService;
    }

    /**
     * Displays the customer dashboard with upcoming and previous bookings.
     *
     * @param model the model object to populate view attributes
     * @return the name of the dashboard view template
     */
    @GetMapping("/dashboard")
    public String showCustomerDashboard(Model model) {
        Set<BookingDTO> bookings = BookingDTOFactory
                .copyBookingSetToDTO(bookingService.getUpcomingBookings(authenticationService.getCurrentUser()));
        model.addAttribute("bookings", bookings);
        loggingService.warn("showCustomerDash", "Bookings' size: " + bookings.size());
        Set<BookingDTO> prevBookings = BookingDTOFactory.copyBookingSetToDTO(
                bookingService.getBookingsByCustomerAndModifier((Customer) authenticationService.getCurrentUser(),
                        bookingService.findModifier("STATUS_COMPLETED")
                                .orElseThrow(() -> new IllegalArgumentException("could not find COMPLETED modifier"))));
        model.addAttribute("prevBookings", prevBookings);
        return "/customer/customer-dashboard.html";
    }

    /**
     * Shows the review page for a specific booking identified by its ID.
     *
     * @param id    the ID of the booking
     * @param model the model object to populate with booking information
     * @return the name of the review view template
     */
    @GetMapping("/review/{id}")
    public String showReviewPage(@PathVariable Long id, Model model) {
        BookingDTO dto = BookingDTOFactory.copyToBookingDTO(bookingService.getBookingById(id));
        model.addAttribute("booking", dto);
        return "/customer/review.html";
    }

    /**
     * Submits a review for a specific booking after validating the input.
     *
     * @param id                 the ID of the booking
     * @param review             the review data transfer object
     * @param result             the binding result containing validation errors, if
     *                           any
     * @param model              the model object to populate with attributes
     * @param redirectAttributes attributes for redirecting with messages
     * @return a redirect URL to the dashboard
     */
    @PostMapping("/review/{id}")
    public String submitReview(@PathVariable Long id, @ModelAttribute("review") @Valid ReviewDTO review,
            BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", result.getAllErrors());
        } else {
            reviewService.saveReview(reviewService.unwrapNewReviewDTO(review));
            redirectAttributes.addFlashAttribute("message", "Review submitted!");
        }
        return "redirect:/customer/dashboard";
    }

    /**
     * Displays all bookings associated with the current customer.
     *
     * @param model the model object to populate with bookings
     * @return the name of the bookings view template
     */
    @GetMapping("/bookings")
    public String showBookings(Model model) {
        Set<DTOInterface> bookings = DTOMapper
                .toDTOSet(bookingService.getBookingsByCustomer(customerService.getCurrentCustomer()));
        model.addAttribute("allBookings", bookings);
        return "/customer/bookings.html";
    }

    /**
     * Shows the booking edit page for a specific booking identified by its ID.
     * 
     * @param id    the ID of the booking
     * @param model the model object to populate with booking information
     * @return the name of the booking edit view template or a redirect in case of
     *         an error
     */
    @GetMapping("/bookings/edit/{id}")
    public String showBookingEditPage(@PathVariable Long id, Model model) {
        Booking bookingEntity = bookingService.getBookingById(id);
        if (bookingService.doesBookingBelongToCustomer(bookingEntity, customerService.getCurrentCustomer())) {
            model.addAttribute("booking", BookingDTOFactory.copyToBookingDTO(bookingEntity));
            return "edit-booking.html";
        } else {
            model.addAttribute("error", "Not authorized to access this page");
            return "redirect:/error";
        }
    }

    /**
     * Updates a specific booking with new date and time constraints.
     *
     * @param id                 the ID of the booking
     * @param newDate            the new date for the booking
     * @param newTime            the new time for the booking
     * @param model              the model object to add view attributes
     * @param redirectAttributes attributes for redirecting with messages
     * @return a redirect URL to the dashboard
     */
    @PostMapping("/bookings/edit/{id}")
    public String updateBooking(@PathVariable Long id, @RequestParam LocalDate newDate, @RequestParam LocalTime newTime,
            Model model, RedirectAttributes redirectAttributes) {
        Booking booking = bookingService.getBookingById(id);
        if (!bookingService.doesBookingBelongToCustomer(booking, customerService.getCurrentCustomer())) {
            model.addAttribute("error", "Not authorized to access this page");
            return "redirect:/error";
        }
        if (newDate == null || newTime == null) {
            redirectAttributes.addFlashAttribute("error", "Please enter a date and time");
        } else if (bookingService.modifyBooking(booking, newDate, newTime)) {
            redirectAttributes.addFlashAttribute("message",
                    "booking modified. It will need to be accepted by a vendor again");
        } else {
            redirectAttributes.addFlashAttribute("error",
                    "Please enter a date at least a day away and time between 8AM and 8PM");
        }
        return "redirect:/customer/dashboard";
    }

    /**
     * Cancels a specific booking identified by its ID.
     *
     * @param id                 the ID of the booking
     * @param model              the model object to add view attributes
     * @param redirectAttributes attributes for redirecting with messages
     * @return a redirect URL to the dashboard
     */
    @PostMapping("/customer/bookings/cancel/{id}")
    public String postMethodName(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Booking booking = bookingService.getBookingById(id);
        if (!bookingService.doesBookingBelongToCustomer(booking, customerService.getCurrentCustomer())) {
            model.addAttribute("error", "Not authorized to access this page");
            return "redirect:/error";
        }
        if (bookingService.cancelBooking(booking)) {
            redirectAttributes.addFlashAttribute("message", "Booking canceled");
        } else {
            redirectAttributes.addFlashAttribute("error", "Error canceling");
        }
        return "redirect:/customer/dashboard";
    }

    /**
     * Displays the schedule form for creating new bookings.
     *
     * @param model the model object to populate with modifiers and addresses
     * @return the name of the schedule view template
     */
    @GetMapping("/schedule")
    public String showScheduleForm(Model model) {
        model.addAttribute("types", bookingService.getModifiersByType("TYPE"));
        model.addAttribute("addresses",
                DTOMapper.toDTOSet(customerService.getAddresses(customerService.getCurrentCustomer())));
        return "/customer/schedule.html";
    }

    /**
     * Schedules a new booking based on user input and validates the data.
     *
     * @param newBooking         the new booking data transfer object
     * @param result             the binding result containing validation errors, if
     *                           any
     * @param model              the model object to populate with attributes
     * @param redirectAttributes attributes for redirecting with messages
     * @return a redirect URL to the dashboard or back to the schedule form upon
     *         validation failure
     */
    @PostMapping("/schedule")
    public String sheduleBooking(@ModelAttribute @Valid NewBookingDTO newBooking, BindingResult result, Model model,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", result.getAllErrors());

            return "redirect:/customer/schedule";
        }
        bookingService.createBooking(newBooking, customerService.getCurrentCustomer());
        redirectAttributes.addFlashAttribute("message", "Successfully posted booking!");
        return "redirect:/customer/dashboard";
    }

    /**
     * Displays the customer profile page.
     *
     * @param model the model object to populate with user details
     * @return the name of the profile view template
     */
    @GetMapping("/profile")
    public String showProfile(Model model) {
        model.addAttribute("user", UserDTOFactory.createCustomerIncomingDTO());
        return "/customer/profile.html";
    }

    /**
     * Updates the customer's profile with new data after validation.
     *
     * @param user               the updated customer information
     * @param result             the binding result containing validation errors, if
     *                           any
     * @param model              the model object to populate with attributes
     * @param redirectAttributes attributes for redirecting with messages
     * @return a redirect URL to the dashboard or back to the profile page upon
     *         validation failure
     */
    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute @Valid CustomerIncomingDTO user, BindingResult result, Model model,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", result.getAllErrors());
            return "redirect:/customer/profile";
        }
        customerService.modifyCustomer(customerService.getCurrentCustomer(), user);
        redirectAttributes.addFlashAttribute("message", "Successfully updated profile!");
        return "redirect:/customer/dashboard";
    }

}
