package jpja.webapp.exceptions;

/**
 * Exception thrown when attempting to create account with an email that is already taken.
 * Inherits from RuntimeException.
 *
 * @author James Prial
 */
public class EmailTakenException extends RuntimeException {
    public EmailTakenException(){}

    public EmailTakenException(String message){
        super(message);
    }
}
