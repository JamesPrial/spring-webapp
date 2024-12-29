package jpja.webapp.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import jpja.webapp.exceptions.IdNotFoundException;
import jpja.webapp.model.entities.User;
import jpja.webapp.model.entities.VerificationToken;
import jpja.webapp.repositories.TokenRepository;

/**
 * Service class for managing verification tokens.
 * Provides functionality to create, retrieve, and delete tokens used for email
 * verification.
 * 
 * @author James Prial
 */
@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    /**
     * Constructs a new instance of TokenService with the provided TokenRepository.
     * 
     * @param tokenRepository the repository for accessing verification tokens
     */
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    /**
     * Retrieves a verification token by its UUID.
     * 
     * @param UUID the unique identifier of the token
     * @return the {@link VerificationToken} object
     * @throws IdNotFoundException if the token with the specified UUID is not found
     */
    public VerificationToken getTokenByUUID(String UUID) {
        return tokenRepository.findById(UUID)
                .orElseThrow(() -> new IdNotFoundException("Verification token with UUID: " + UUID + " not found"));
    }

    /**
     * Creates and saves a new verification token for a given user.
     * The token expires after 24 hours.
     * 
     * @param user the {@link User} for whom the token is created
     * @return the created {@link VerificationToken}
     */
    public VerificationToken createAndSaveVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(24); // 24-hour expiry
        VerificationToken verificationToken = new VerificationToken(token, expiryDate, user);
        tokenRepository.save(verificationToken);
        return verificationToken;
    }

    /**
     * Deletes a verification token by its UUID.
     * 
     * @param UUID the unique identifier of the token to delete
     */
    public void deleteToken(String UUID) {
        deleteToken(this.getTokenByUUID(UUID));
    }

    /**
     * Deletes a specific verification token.
     * 
     * @param token the {@link VerificationToken} to delete
     */
    public void deleteToken(VerificationToken token) {
        tokenRepository.delete(token);
    }
}
