package jpja.webapp.exceptions.database;

/*
 * This exception is thrown when an error occurs while interacting with the database
 * and the error is related to runtime behavior of the database, NOT database access itself.
 * 
 * @author James Prial
 */
public class DatabaseException extends RuntimeException {
    public DatabaseException(){}

    public DatabaseException(String message){
        super(message);
    }
}
