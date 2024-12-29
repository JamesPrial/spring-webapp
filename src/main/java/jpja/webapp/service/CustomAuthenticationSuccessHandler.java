package jpja.webapp.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jpja.webapp.model.entities.User;

import java.io.IOException;
import java.util.Set;

/**
 * Custom handler for processing actions after a successful authentication.
 * Determines the user's role and redirects to the appropriate dashboard or profile page.
 * Also logs the user's login and updates additional user information as needed.
 * 
 * @author James Prial
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final LoggingService loggingService;
    private final CustomUserDetailsService userDetailsService;

    /**
     * Constructs the handler with required dependencies.
     *
     * @param loggingService Service to log user activity.
     * @param userDetailsService Service to manage user details.
     */
    public CustomAuthenticationSuccessHandler(LoggingService loggingService, CustomUserDetailsService userDetailsService) {
        this.loggingService = loggingService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Handles successful authentication by redirecting the user based on their role and verification level.
     *
     * @param request The HTTP request that triggered the authentication.
     * @param response The HTTP response to send to the client.
     * @param authentication The authentication object containing the user's credentials and roles.
     * @throws IOException If an input or output error is detected when handling the request.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // Extract username from authentication principal
        Object principal = authentication.getPrincipal();
        String username = (principal instanceof UserDetails) ? ((UserDetails) principal).getUsername() : principal.toString();

        // Retrieve user entity based on username
        User user = userDetailsService.findUser(username);

        // Determine the user's role
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        String role = roles.contains("ROLE_ADMIN") ? "admin" :
                      roles.contains("ROLE_VENDOR") ? "vendor" :
                      roles.contains("ROLE_CUSTOMER") ? "customer" : "";

        // If the user is an admin, update their admin info
        if ("admin".equals(role)) {
            userDetailsService.saveAdminInfo(user, loggingService.getLastLogin(user).orElse(null));
        }

        // Redirect based on verification level and role
        if (!"admin".equals(role) && user.getVerificationLevel() < 2) {
            response.sendRedirect("/" + role + "/profile?verify");
        } else {
            response.sendRedirect("/" + role + "/dashboard");
        }

        // Log the login activity
        loggingService.login(user, request.getRemoteAddr());
    }
}
