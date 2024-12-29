package jpja.webapp.controllers;

import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jpja.webapp.factories.BookingDTOFactory;
import jpja.webapp.model.dto.BookingDTO;
import jpja.webapp.model.entities.Vendor;
import jpja.webapp.service.AuthenticationService;
import jpja.webapp.service.BookingService;
import jpja.webapp.service.VendorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controller for managing vendor-related actions
 * such as displaying the vendor dashboard and claiming bookings.
 * 
 * @author James Prial
 */
@Controller
@RequestMapping("/vendor")
public class VendorController {
    private final VendorService vendorService;
    private final BookingService bookingService;
    private final AuthenticationService authenticationService;

    public VendorController(VendorService vendorService, BookingService bookingService,
            AuthenticationService authenticationService) {
        this.vendorService = vendorService;
        this.bookingService = bookingService;
        this.authenticationService = authenticationService;
    }

    /**
     * Displays the vendor dashboard with bookings and earnings information.
     *
     * @param model The model to hold attributes for the view.
     * @return The view name for displaying the vendor dashboard.
     */
    @GetMapping("/dashboard")
    public String showVendorDashboard(Model model) {
        Vendor vendor = vendorService.findVendorByUsername(authenticationService.getCurrentUsername());
        Set<BookingDTO> jobs = BookingDTOFactory.copyBookingSetToDTO(bookingService.getBookingsByVendor(vendor));
        model.addAttribute("jobs", jobs);
        Set<BookingDTO> unclaimedBookings = BookingDTOFactory
                .copyBookingSetToDTO(bookingService.getUnclaimedBookings());
        model.addAttribute("availableBookings", unclaimedBookings);
        model.addAttribute("totalEarnings", bookingService.calculateTotalEarnings(vendor));
        model.addAttribute("pendingPayouts", bookingService.calculatePendingEarnings(vendor));
        return "vendor-dashboard.html";
    }

    /**
     * Allows a vendor to claim a booking by its ID.
     *
     * @param bookingId The ID of the booking to be claimed.
     * @return A redirect URL indicating success or failure.
     */
    @PostMapping("/bookings/claim")
    public String claimBooking(@RequestParam("bookingId") Long bookingId) {
        boolean success = bookingService.claimBooking(bookingId,
                vendorService.findVendorByUsername(authenticationService.getCurrentUsername()));
        if (success) {
            return "redirect:/vendor/dashboard?claimed=true";
        } else {
            return "redirect:/vendor/dashboard?error=claim_failed";
        }
    }
}
