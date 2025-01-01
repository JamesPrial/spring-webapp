package jpja.webapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Configuration class for setting up encryption utilities.
 * Provides a {@link BCryptPasswordEncoder} bean for password encryption.
 * 
 * @author James Prial
 */
@Configuration
public class EncryptionConfig {

    /**
     * Creates a {@link BCryptPasswordEncoder} bean to be used for encrypting
     * passwords.
     * 
     * @return A configured instance of {@link BCryptPasswordEncoder}.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
