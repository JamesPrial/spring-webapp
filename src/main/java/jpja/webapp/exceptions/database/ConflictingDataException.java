package jpja.webapp.exceptions.database;

/**
 * Exception thrown when data is set wrong in the database
 * Inherits from DatabaseException.
 *
 * @author James Prial
 */
public class ConflictingDataException extends DatabaseException {
    public ConflictingDataException(){}

    public ConflictingDataException(String message){
        super(message);
    }
}
