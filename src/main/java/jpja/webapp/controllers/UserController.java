package jpja.webapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import jpja.webapp.exceptions.database.ConflictingDataException;
import jpja.webapp.factories.UserDTOFactory;
import jpja.webapp.model.dto.UserIncomingDTO;
import jpja.webapp.model.dto.UserOutgoingDTO;
import jpja.webapp.service.AuthenticationService;
import jpja.webapp.service.CustomUserDetailsService;
import jpja.webapp.service.LoggingService;

/**
 * Controller for managing user-related actions such as viewing and updating the
 * user profile.
 * 
 * @author James Prial
 */
@Controller
@RequestMapping("/user")
public class UserController {
    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationService authenticationService;
    private final LoggingService loggingService;

    public UserController(CustomUserDetailsService userDetailsService, AuthenticationService authenticationService,
            LoggingService loggingService) {
        this.userDetailsService = userDetailsService;
        this.authenticationService = authenticationService;
        this.loggingService = loggingService;
    }

    /**
     * Displays the user profile.
     *
     * @param model The model to hold user attributes.
     * @return The view name for displaying the user profile.
     */
    @GetMapping("/profile")
    public String showUserProfile(Model model) {
        UserOutgoingDTO user = UserDTOFactory.copyToUserOutgoingDTO(authenticationService.getCurrentUser());
        model.addAttribute("user", user);
        return "user/profile"; // View to display user profile
    }

    /**
     * Updates the user profile based on the submitted form.
     *
     * @param updatedUser The updated user information.
     * @param model       The model to hold response attributes.
     * @return The view name for displaying the user profile.
     */
    @PostMapping("/profile")
    public String updateUserProfile(@ModelAttribute("user") @Valid UserIncomingDTO updatedUser, Model model) {
        updatedUser.setEmail(null);
        try {
            userDetailsService.modifyUser(authenticationService.getCurrentUsername(), updatedUser);
            model.addAttribute("message", "Profile updated successfully");
        } catch (ConflictingDataException e) {
            model.addAttribute("message", "Error");
            loggingService.error("UserController.updateUserProfile",
                    "ConflictingDataException SHOULDN'T GET THROWN HERE");
        }
        return "user/profile";
    }
}
