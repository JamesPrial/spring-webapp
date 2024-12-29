package jpja.webapp.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jpja.webapp.exceptions.EmailTakenException;
import jpja.webapp.exceptions.UsernameTakenException;
import jpja.webapp.factories.UserFactory;
import jpja.webapp.model.dto.ModifierDTO;
import jpja.webapp.model.dto.UserIncomingDTO;
import jpja.webapp.model.entities.User;
import jpja.webapp.model.entities.VerificationToken;

/**
 * Service class for handling user authentication, registration, and email
 * verification.
 * Provides methods for managing user accounts, verifying email addresses,
 * and accessing currently authenticated user details.
 * 
 * <p>
 * This service interacts with {@link CustomUserDetailsService},
 * {@link EmailService},
 * and {@link TokenService} to handle user-related operations.
 * </p>
 * 
 * @author James Prial
 */
@Service
public class AuthenticationService {
    private final CustomUserDetailsService userDetailsService;
    private final EmailService emailService;
    private final TokenService tokenService;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    /**
     * Constructs an {@link AuthenticationService} with the specified dependencies.
     * 
     * @param userDetailsService the user details service
     * @param emailService       the email service for sending emails
     * @param tokenService       the service for managing verification tokens
     */
    public AuthenticationService(CustomUserDetailsService userDetailsService, EmailService emailService,
            TokenService tokenService) {
        this.userDetailsService = userDetailsService;
        this.emailService = emailService;
        this.tokenService = tokenService;
    }

    /**
     * Registers a new user with the provided details.
     * 
     * @param userDTO the {@link UserIncomingDTO} containing user registration
     *                information
     * @throws IllegalArgumentException if {@code userDTO} is null or missing
     *                                  required fields
     * @throws EmailTakenException      if the email is already in use
     * @throws UsernameTakenException   if the username is already in use
     */
    public void registerUser(UserIncomingDTO userDTO) {
        if (userDTO == null) {
            throw new IllegalArgumentException();
        }
        if (userDetailsService.doesEmailExist(userDTO.getEmail())) {
            throw new EmailTakenException();
        }
        if (userDetailsService.doesUsernameExist(userDTO.getUsername())) {
            throw new UsernameTakenException();
        }
        if (userDTO.getRoles() == null) {
            throw new IllegalArgumentException();
        }
        for (ModifierDTO roleDTO : userDTO.getRoles()) {
            if (roleDTO.getName().equals("ROLE_ADMIN") || roleDTO.getName().equals("ROLE_CUSTOMER")
                    || roleDTO.getName().equals("ROLE_VENDOR")) {
                userDetailsService.registerUser(UserFactory.createUser(userDTO.getEmail(), userDTO.getUsername(),
                        userDTO.getPassword(), userDetailsService.unwrapRoleDTO(roleDTO)));
                return;
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * Verifies a user's email using a verification token string.
     * 
     * @param token the UUID of the verification token
     * @return {@code true} if the email was successfully verified; {@code false}
     *         otherwise
     */
    public boolean verifyUserEmail(String token) {
        return this.verifyUserEmail(tokenService.getTokenByUUID(token));
    }

    /**
     * Verifies a user's email using a {@link VerificationToken}.
     * 
     * @param token the {@link VerificationToken} to validate
     * @return {@code true} if the email was successfully verified; {@code false}
     *         otherwise
     */
    public boolean verifyUserEmail(VerificationToken token) {
        if (token == null || token.getExpiryDate().isBefore(LocalDateTime.now())) {
            return false;
        }

        User user = token.getUser();
        if (user.getVerificationLevel() != 0) {
            return false;
        }
        userDetailsService.verifyUser(user);
        tokenService.deleteToken(token);
        return true;
    }

    /**
     * Resends a verification email to the specified email address.
     * 
     * @param email the email address to resend the verification email to
     */
    public void resendVerificationEmail(String email) {
        try {
            this.resendVerificationEmail(userDetailsService.findUser(email));
        } catch (UsernameNotFoundException e) {
            logger.warn("Attempted to resendVerification to invalid email: " + email);
        }
    }

    /**
     * Resends a verification email to the specified user.
     * 
     * @param user the {@link User} to resend the verification email to
     */
    public void resendVerificationEmail(User user) {
        if (user == null || user.getVerificationLevel() != 0) {
            String email = user == null ? "" : user.getEmail();
            logger.warn("Attempted to resendVerification to an already verified user (or null) email: " + email);
        }
        emailService.sendVerificationEmail(user, tokenService.createAndSaveVerificationToken(user));
    }

    /**
     * Retrieves the username of the currently authenticated user.
     * 
     * @return the username of the current user
     * @throws IllegalStateException if the user is not authenticated
     */
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        throw new IllegalStateException("User is not authenticated");
    }

    /**
     * Retrieves the currently authenticated {@link User}.
     * 
     * @return the current user
     * @throws IllegalStateException if the user is not authenticated
     */
    public User getCurrentUser() {
        return userDetailsService.findUserByUsername(getCurrentUsername());
    }
}
