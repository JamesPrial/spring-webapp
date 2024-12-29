package jpja.webapp.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jpja.webapp.exceptions.EmailTakenException;
import jpja.webapp.exceptions.UsernameTakenException;
import jpja.webapp.model.dto.UserIncomingDTO;
import jpja.webapp.service.AuthenticationService;

/**
 * Controller responsible for handling user authentication actions,
 * including login, registration, and email verification.
 * 
 * @author James Prial
 */
@Controller
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;

    }

    /**
     * Handles user login.
     *
     * @param req The request object containing user credentials.
     * @param res The response object to send back data.
     */
    @GetMapping("/login")
    public String showLoginPage() {
        return "auth/login";
    }

    /**
     * Displays the registration page.
     *
     * @param model The model to hold attribute data for the view.
     * @return The name of the view to render the registration page.
     */
    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        return "auth/register";
    }

    /**
     * Handles user registration.
     *
     * @param userIncomingDTO    The incoming user data transfer object containing
     *                           user information.
     * @param result             BindingResult for validation results.
     * @param redirectAttributes Redirect attributes for sending flash attributes.
     * @return A redirect to the appropriate page based on registration outcome.
     */
    @PostMapping("/register")
    public String registerUser(@ModelAttribute @Valid UserIncomingDTO userIncomingDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", result.getAllErrors());
            return "redirect:/register";
        }
        try {
            authenticationService.registerUser(userIncomingDTO);
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please verify your email.");
            return "redirect:/login";
        } catch (EmailTakenException e) {
            redirectAttributes.addFlashAttribute("error", "Email already in use.");
            return "redirect:/register";
        } catch (UsernameTakenException e) {
            redirectAttributes.addFlashAttribute("error", "Username already in use.");
            return "redirect:/register";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error setting role.");
            return "redirect:/register";
        }

    }

    /**
     * Displays the dashboard based on user role.
     *
     * @param model The model to hold attribute data for the view.
     * @return A redirect to the appropriate dashboard based on the user's role.
     */
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().iterator().next().getAuthority(); // Get user's role

        if (role.equals("ROLE_VENDOR")) {
            return "redirect:/vendor/dashboard";
        } else if (role.equals("ROLE_CUSTOMER")) {
            return "redirect:/customer/dashboard";
        }
        return "redirect:/"; // Fallback to home
    }

    /**
     * Verifies user email using a token.
     *
     * @param token              The verification token sent to the user's email.
     * @param redirectAttributes Redirect attributes for sending flash attributes.
     * @return A redirect to the login page with a message indicating the result of
     *         the verification.
     */
    @GetMapping("/verify")
    public String verifyAccount(@RequestParam("token") String token, RedirectAttributes redirectAttributes) {
        if (authenticationService.verifyUserEmail(token)) {
            redirectAttributes.addFlashAttribute("message", "Email verified successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Token expired. Please request a new verification email.");
        }
        return "redirect:/login";
    }

    /**
     * Displays the resend verification email page.
     *
     * @param session            The HTTP session containing attributes.
     * @param model              The model to hold attribute data for the view.
     * @param redirectAttributes Redirect attributes for sending flash attributes.
     * @return The name of the view to render the resend verification page or a
     *         redirect if session is expired.
     */
    @GetMapping("/resend-verification")
    public String showResendVerification(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        String email = (String) session.getAttribute("unverifiedEmail");
        if (email == null) {
            redirectAttributes.addFlashAttribute("error", "Session expired. Please log in again.");
            return "redirect:/login";
        }
        model.addAttribute("email", email);
        return "auth/resend-verification";
    }

    /**
     * Resends the verification email to the user.
     *
     * @param email              The email address of the user.
     * @param session            The HTTP session to store the unverified email.
     * @param redirectAttributes Redirect attributes for sending flash attributes.
     * @return A redirect to the login page with a message indicating the outcome of
     *         the operation.
     */
    @PostMapping("/resend-verification")
    public String resendVerificationEmail(
            @RequestParam(value = "email", required = false) String email,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        if (email == null || email.isEmpty()) {
            email = (String) session.getAttribute("unverifiedEmail");
        } else {
            session.setAttribute("unverifiedEmail", email);
        }

        if (email == null || email.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "No email address provided.");
            return "redirect:/login";
        }

        authenticationService.resendVerificationEmail(email);
        redirectAttributes.addFlashAttribute("message",
                "If an account with that email exists, a verification email has been sent.");

        session.removeAttribute("unverifiedEmail");
        return "redirect:/login";
    }

}
