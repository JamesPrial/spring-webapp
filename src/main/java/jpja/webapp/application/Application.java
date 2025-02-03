package jpja.webapp.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main entry point for the JPJA Web Application.
 * 
 * <p>
 * This class configures and launches the Spring Boot application. It includes
 * configuration for component scanning, JPA repositories, and entity scanning.
 * </p>
 * 
 * <p>
 * Annotations Used:
 * </p>
 * <ul>
 * <li>{@link EnableAutoConfiguration} - Enables Spring Boot's
 * auto-configuration mechanism.</li>
 * <li>{@link Configuration} - Marks this class as a configuration class.</li>
 * <li>{@link ComponentScan} - Scans the specified package and its sub-packages
 * for Spring components.</li>
 * <li>{@link EnableJpaRepositories} - Enables scanning for Spring Data JPA
 * repositories.</li>
 * <li>{@link EntityScan} - Configures scanning for JPA entities in the
 * specified package.</li>
 * </ul>
 * 
 * <p>
 * Execution:
 * </p>
 * 
 * <pre>
 * {@code
 * public static void main(String[] args) {
 * 	SpringApplication.run(Application.class, args);
 * }
 * }
 * </pre>
 * 
 * @author James Prial
 */
@EnableAutoConfiguration
@Configuration
@ComponentScan("jpja.webapp")
@EnableJpaRepositories(basePackages = "jpja.webapp.repositories")
@EntityScan(basePackages = "jpja.webapp.model.entities")
@SpringBootApplication
public class Application {

	/**
	 * Main method that launches the Spring Boot application.
	 * 
	 * @param args command-line arguments passed to the application
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
