package cristoffer85.com.projektatornserver.MAINAPP.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import cristoffer85.com.projektatornserver.MAINAPP.model.EmailVerificationToken;
import cristoffer85.com.projektatornserver.MAINAPP.model.User;
import cristoffer85.com.projektatornserver.MAINAPP.repository.EmailVerificationTokenRepository;
import cristoffer85.com.projektatornserver.MAINAPP.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Scheduled(fixedRate = 3600000)
    public void cleanUpExpiredEmailVerificationTokens() {
        Instant now = Instant.now();
        emailVerificationTokenRepository.deleteByExpiryBefore(now);
    }

    // ########## Email verification on registration and update ##########
    public void sendVerificationEmail(String to, String token, String username) {
        String link = frontendUrl + "/verify-email?token=" + token + "&username=" + username;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Verify your email address");
        message.setText("Please verify your email by clicking the following link: " + link);
        mailSender.send(message);
    }

    public void requestEmailChange(String username, String newEmail) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPendingEmail(newEmail);
        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = new EmailVerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUsername(username);

        //Verification tokens are now valid for 1 hour
        verificationToken.setExpiry(Instant.now().plus(1, ChronoUnit.HOURS));
        emailVerificationTokenRepository.save(verificationToken);

        sendVerificationEmail(newEmail, token, username); // Send to new email
    }

    // ########## Password forgot/reset email ##########
    public void sendPasswordResetEmail(String to, String token) {
        String resetLink = frontendUrl + "/reset-password?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset Request");
        message.setText("Click the following link to reset your password: " + resetLink);
        mailSender.send(message);
    }

    // ########## New Project notification email ##########
    public void sendProjectNotificationEmailHtml(String to, String fromUser, String projectIdea, String projectId) throws MessagingException {
        String subject = "New Project Shared With You";
        String projectLink = frontendUrl + "/projects/" + projectId;
        String html = "<h2>Hi!</h2>"
                + "<p><b>" + fromUser + "</b> has shared a new project with you:</p>"
                + "<blockquote style='border-left:4px solid #ccc;padding-left:8px;'>" + projectIdea.replace("\n", "<br>") + "</blockquote>"
                + "<p><a href='" + projectLink + "'>View the project here</a></p>"
                + "<p>Log in to your account to view the details.</p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(html, true); // true = HTML

        mailSender.send(message);
    }
}