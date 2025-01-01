package jpja.webapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import jpja.webapp.service.CustomAuthenticationFailureHandler;
import jpja.webapp.service.CustomAuthenticationSuccessHandler;

/**
 * Configuration class for Spring Security.
 * Defines security settings, including authentication, authorization, and custom handlers for login and logout.
 * 
 * @author James Prial
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler successHandler;
    private final CustomAuthenticationFailureHandler failureHandler;

    /**
     * Constructs a new instance of SecurityConfig with custom handlers for authentication success and failure.
     * 
     * @param successHandler the custom handler for successful authentication
     * @param failureHandler the custom handler for failed authentication
     */
    public SecurityConfig(CustomAuthenticationSuccessHandler successHandler, CustomAuthenticationFailureHandler failureHandler) {
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
    }

    /**
     * Defines the security filter chain for HTTP requests.
     * Configures authorization rules, login and logout behavior, and custom handlers for authentication events.
     * 
     * @param http the {@link HttpSecurity} object for configuring security settings
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception if an error occurs while building the security configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configure security settings
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN") // Admin-only pages
                .requestMatchers("/vendor/**").hasRole("VENDOR")
                .requestMatchers("/customer/**").hasRole("CUSTOMER")
                .requestMatchers("/login", "/register", "/", "/error", "/css/**", "/images/**", "/resend-verification", "/verify").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")      // Form POST endpoint
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }
}
