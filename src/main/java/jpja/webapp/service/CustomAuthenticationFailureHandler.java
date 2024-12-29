package jpja.webapp.service;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jpja.webapp.exceptions.EmailNotVerifiedException;

/**
 * Custom handler for authentication failures.
 * Extends {@link SimpleUrlAuthenticationFailureHandler} to provide custom behavior
 * for specific authentication exceptions, such as {@link EmailNotVerifiedException}.
 * 
 * @author James Prial
 */
@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    
    /**
     * Handles authentication failures. Redirects users to a specific page if their email is not verified.
     *
     * @param request The HTTP request that resulted in the authentication failure.
     * @param response The HTTP response to be sent.
     * @param exception The exception that triggered the authentication failure.
     * @throws IOException If an input or output error is detected when the handler processes the request.
     * @throws ServletException If the request could not be handled.
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        if (exception.getCause() instanceof EmailNotVerifiedException) {
            // Extract email from request and store it in the session for reference
            String email = request.getParameter("username");
            request.getSession().setAttribute("unverifiedEmail", email);
            
            // Redirect to the resend-verification page
            response.sendRedirect("/resend-verification");
        } else {
            // For other authentication exceptions, use the default behavior
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}
