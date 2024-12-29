package jpja.webapp.exceptions;

/**
 * Exception thrown when attempting to create account with a username that is already taken.
 * Inherits from RuntimeException.
 *
 * @author James Prial
 */
public class UsernameTakenException extends RuntimeException{
    public UsernameTakenException(){}
    public UsernameTakenException(String message){
        super(message);
    }
}
