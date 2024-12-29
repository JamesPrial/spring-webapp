package jpja.webapp.exceptions.database;

/**
 * Exception thrown when Modifiers were set wrong in the database, for example there should never be 2 STATUSes to one booking.
 * Inherits from ConflictingDataException.
 *
 * @author James Prial
 */
public class ConflictingModifiersException extends ConflictingDataException {
    public ConflictingModifiersException(){}

    public ConflictingModifiersException(String message){
        super(message);
    }
}
