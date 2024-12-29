package jpja.webapp.exceptions.database;

/**
 * Exception thrown when expected data is not found in the database.
 * Inherits from DatabaseException.
 *
 * @author James Prial
 */
public class ExpectedDataNotFoundException extends DatabaseException {
    public ExpectedDataNotFoundException(){}

    public ExpectedDataNotFoundException(String message){
        super(message);
    }
}
