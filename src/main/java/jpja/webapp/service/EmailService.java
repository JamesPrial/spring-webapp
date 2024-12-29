package jpja.webapp.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jpja.webapp.model.entities.User;
import jpja.webapp.model.entities.VerificationToken;

/**
 * Service responsible for sending emails, such as verification emails, to users.
 * Utilizes Spring's {@link JavaMailSender} to send emails.
 * 
 * @author James Prial
 */
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    /**
     * Constructs an instance of {@link EmailService} with a {@link JavaMailSender}.
     * 
     * @param mailSender The mail sender used for sending emails.
     */
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private static final Logger LOGGER = Logger.getLogger(EmailService.class.getName());

    /**
     * Sends a verification email to the user with the given verification token.
     * The email contains a link for the user to verify their email address.
     * 
     * @param user  The user to whom the email will be sent.
     * @param token The verification token associated with the user's email verification.
     */
    public void sendVerificationEmail(User user, VerificationToken token) {
        String subject = "Please Verify Your Email";
        // String verificationUrl = "http://EZCleanNJ.com/verify?token=" + token.getUUID();
        String verificationUrl = "localhost:8080/verify?token=" + token.getUUID();
        String message = "Click the link to verify your email: " + verificationUrl;

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(user.getEmail());
            helper.setFrom("info@EZCleanNJ.com");
            helper.setSubject(subject);
            helper.setText(message, true);
            mailSender.send(mimeMessage);
        } catch (MailException e) {
            // Handle exceptions related to Spring's mail-sending process
            LOGGER.log(Level.SEVERE, "Spring MailException occurred while sending email to: " + user.getEmail(), e);
        } catch (MessagingException e) {
            // Handle exceptions related to constructing or sending the email message
            LOGGER.log(Level.SEVERE, "MessagingException occurred while sending email to: " + user.getEmail(), e);
        }
    }
}
