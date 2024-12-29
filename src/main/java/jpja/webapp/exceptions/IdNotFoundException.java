package jpja.webapp.exceptions;

/**
 * Exception thrown when a given ID is not found in the database.
 * Inherits from RuntimeException.
 *
 * @author James Prial
 */
public class IdNotFoundException extends RuntimeException {
    public IdNotFoundException(String message) {
        super(message);
    }
}