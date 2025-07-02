package cristoffer85.com.projektatornserver.MAINAPP.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;

import cristoffer85.com.projektatornserver.MAINAPP.dto.SendOnlyUserNameDTO;
import cristoffer85.com.projektatornserver.MAINAPP.dto.UserUpdateDTO;
import cristoffer85.com.projektatornserver.MAINAPP.model.EmailVerificationToken;
import cristoffer85.com.projektatornserver.MAINAPP.model.PasswordReset;
import cristoffer85.com.projektatornserver.MAINAPP.model.User;
import cristoffer85.com.projektatornserver.MAINAPP.repository.EmailVerificationTokenRepository;
import cristoffer85.com.projektatornserver.MAINAPP.repository.PasswordResetRepository;
import cristoffer85.com.projektatornserver.MAINAPP.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetRepository passwordResetRepository;

    @Autowired
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    // Token cleanup run to database (Runs every hour = 3600000 ms)
    @Scheduled(fixedRate = 3600000)
    public void cleanUpExpiredPasswordResetTokens() {
        Date now = new Date();
        passwordResetRepository.deleteByExpiryBefore(now);
    }

    public User getOneUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<SendOnlyUserNameDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new SendOnlyUserNameDTO(user.getUsername()))
                .collect(Collectors.toList());
    }

    public User updateUser(String username, UserUpdateDTO userUpdateDto) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Handle email change with verification
        if (userUpdateDto.getEmail() != null && !userUpdateDto.getEmail().equals(user.getEmail())) {
            user.setPendingEmail(userUpdateDto.getEmail());
            userRepository.save(user);

            // Generate and save verification token
            String token = UUID.randomUUID().toString();
            EmailVerificationToken verificationToken = new EmailVerificationToken();
            verificationToken.setToken(token);
            verificationToken.setUsername(username);
            verificationToken.setExpiry(Instant.now().plus(24, ChronoUnit.HOURS));
            emailVerificationTokenRepository.save(verificationToken);

            emailService.sendVerificationEmail(userUpdateDto.getEmail(), token, username);
        }

        // Update other fields immediately
        if (userUpdateDto.getBirthday() != null) {
            user.setBirthday(userUpdateDto.getBirthday());
        }
        if (userUpdateDto.getForestanimal() != null) {
            user.setForestanimal(userUpdateDto.getForestanimal());
        }
        if (userUpdateDto.getSoursnack() != null) {
            user.setSoursnack(userUpdateDto.getSoursnack());
        }
        if (userUpdateDto.getAvatar() != null) {
            user.setAvatar(userUpdateDto.getAvatar());
        }

        return userRepository.save(user);
    }

    public void requestPasswordReset(String identifier) {
        User user = userRepository.findByUsername(identifier)
            .orElseGet(() -> userRepository.findByEmail(identifier)
                .orElseThrow(() -> new RuntimeException("No user with that username or email")));
        String token = UUID.randomUUID().toString();
        PasswordReset resetToken = new PasswordReset();
        resetToken.setToken(token);
        resetToken.setUsername(user.getUsername());

        // Expiry == 1 hour from now
        resetToken.setExpiry(Instant.now().plus(1, ChronoUnit.HOURS));

        passwordResetRepository.save(resetToken);
        emailService.sendPasswordResetEmail(user.getEmail(), token);
    }

    // Reset password
    public void resetPassword(String token, String newPassword) {
        PasswordReset resetToken = passwordResetRepository.findByToken(token)
            .orElseThrow(() -> new RuntimeException("Invalid or expired token"));
        if (resetToken.getExpiry().isBefore(Instant.now())) {
            throw new RuntimeException("Token expired");
        }
        User user = userRepository.findByUsername(resetToken.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        passwordResetRepository.delete(resetToken);
    }
}