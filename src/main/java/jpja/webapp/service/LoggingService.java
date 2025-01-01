package jpja.webapp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;

import jpja.webapp.model.entities.LoginRecord;
import jpja.webapp.model.entities.User;
import jpja.webapp.repositories.LoginRepository;

/**
 * Service for logging events, warnings, errors, and user login records.
 * Provides methods for saving login information and logging messages or binding
 * errors.
 * 
 * @author James Prial
 */
@Service
public class LoggingService {

    private final LoginRepository loginRepository;
    private static final Logger logger = LoggerFactory.getLogger(LoggingService.class);
    private static final Logger activityLogger = LoggerFactory.getLogger("jpja.logging.activity");

    /**
     * Constructs a new instance of LoggingService with the provided
     * {@link LoginRepository}.
     * 
     * @param loginRepository the repository for managing login records
     */
    public LoggingService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    /**
     * Logs a user's login event, saving the associated IP address and timestamp.
     * 
     * @param user the user who is logging in
     * @param ip   the IP address of the user
     * @return the saved {@link LoginRecord}
     */
    public LoginRecord login(User user, String ip) {
        LoginRecord loginRecord = new LoginRecord(user, LocalDateTime.now(), ip);
        loginRepository.save(loginRecord);
        return loginRecord;
    }

    /**
     * Retrieves the most recent login record for a given user.
     * 
     * @param user the user whose last login record is requested
     * @return an {@link Optional} containing the most recent {@link LoginRecord},
     *         or empty if none exists
     */
    public Optional<LoginRecord> getLastLogin(User user) {
        return loginRepository.findFirstByUserOrderByTimestampDesc(user);
    }

    /**
     * Logs a warning message with the specified source and message.
     * 
     * @param source  the source of the warning (e.g., method or class name)
     * @param message the warning message
     */
    public void warn(String source, String message) {
        logger.warn("{}: {}", source, message);
    }

    /**
     * Logs an error message with the specified source and message.
     * 
     * @param source  the source of the error (e.g., method or class name)
     * @param message the error message
     */
    public void error(String source, String message) {
        logger.error("{}: {}", source, message);
    }

    /**
     * Logs binding results from validation errors, with the option to log as a
     * warning or error.
     * 
     * @param source     the source of the binding results (e.g., method or class
     *                   name)
     * @param errors     a list of {@link ObjectError} representing the validation
     *                   errors
     * @param logAsError {@code true} to log as an error, {@code false} to log as a
     *                   warning
     */
    public void logBindingResults(String source, List<ObjectError> errors, boolean logAsError) {
        StringBuilder msg = new StringBuilder();
        for (ObjectError error : errors) {
            msg.append(error.getDefaultMessage()).append("\n");
        }
        if (logAsError) {
            logger.error("{}: Binding errors: \n{}", source, msg);
        } else {
            logger.warn("{}: Binding errors: \n{}", source, msg);
        }
    }

    /**
     * Logs binding results from validation errors as warnings.
     * 
     * @param source the source of the binding results (e.g., method or class name)
     * @param errors a list of {@link ObjectError} representing the validation
     *               errors
     */
    public void logBindingResults(String source, List<ObjectError> errors) {
        logBindingResults(source, errors, false);
    }

    public void logActivity(String ip, String method, String uri, String query) {
        activityLogger.info("IP: {} - Method: {} - URI: {} - Query: {}", ip, method, uri, query);
    }
}
