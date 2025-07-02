package cristoffer85.com.projektatornserver.MAINAPP.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import cristoffer85.com.projektatornserver.MAINAPP.model.EmailVerificationToken;
import cristoffer85.com.projektatornserver.MAINAPP.model.User;
import cristoffer85.com.projektatornserver.MAINAPP.repository.EmailVerificationTokenRepository;
import cristoffer85.com.projektatornserver.MAINAPP.repository.UserRepository;

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
    public void sendVerificationEmail(String to, String token) {
        String link = frontendUrl + "/verify-email?token=" + token;
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

        sendVerificationEmail(newEmail, token); // Send to new email
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
    public void sendProjectNotificationEmail(String to, String fromUser, String projectIdea) {
        String subject = "New Project Shared With You";
        String text = "Hi!\n\n" +
                    fromUser + " has shared a new project with you: \"" + projectIdea + "\".\n" +
                    "Log in to your account to view the details.";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}