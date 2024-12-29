package jpja.webapp.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * Exception thrown when signing in without verifying email
 * Inherits from AuthenticationException.
 *
 * @author James Prial
 */
public class EmailNotVerifiedException extends AuthenticationException {
    public EmailNotVerifiedException(String message) {
        super(message);
    }
}
