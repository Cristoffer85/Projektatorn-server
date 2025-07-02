package cristoffer85.com.projektatornserver.MAINAPP.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cristoffer85.com.projektatornserver.MAINAPP.dto.LoginResponseDTO;
import cristoffer85.com.projektatornserver.MAINAPP.model.Admin;
import cristoffer85.com.projektatornserver.MAINAPP.model.EmailVerificationToken;
import cristoffer85.com.projektatornserver.MAINAPP.model.Role;
import cristoffer85.com.projektatornserver.MAINAPP.model.User;
import cristoffer85.com.projektatornserver.MAINAPP.repository.AdminRepository;
import cristoffer85.com.projektatornserver.MAINAPP.repository.EmailVerificationTokenRepository;
import cristoffer85.com.projektatornserver.MAINAPP.repository.RoleRepository;
import cristoffer85.com.projektatornserver.MAINAPP.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

/*
 Class that handles registration and login of user and admins.
*/

@Service
@Transactional
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Autowired
    private EmailService emailService;

    public User registerUser(String username, String password, String email) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username '" + username + "' already exists. Please choose a different username.");
        }

        String encodedPassword = passwordEncoder.encode(password);
        Role userRole = roleRepository.findByAuthority("USER")
                .orElseThrow(() -> new RuntimeException("USER role not found"));

        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(encodedPassword);
        newUser.setEmail(email);
        newUser.setAuthorities(authorities);
        newUser.setVerified(false); // Require email verification

        User savedUser = userRepository.save(newUser);

        // Generate and save verification token
        String token = java.util.UUID.randomUUID().toString();
        var verificationToken = new cristoffer85.com.projektatornserver.MAINAPP.model.EmailVerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUsername(savedUser.getUsername());
        verificationToken.setExpiry(java.time.Instant.now().plus(24, java.time.temporal.ChronoUnit.HOURS));
        emailVerificationTokenRepository.save(verificationToken);

        // Send verification email
        emailService.sendVerificationEmail(savedUser.getEmail(), token, savedUser.getUsername());

        return savedUser;
    }

    public LoginResponseDTO loginUser(String username, String password) {
        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );

            Object userOrAdmin;
            Role role;

            // Check if user exists and is verified
            if (userRepository.findByUsername(username).isPresent()) {
                User user = userRepository.findByUsername(username).get();
                if (!user.isVerified()) {
                    throw new RuntimeException("Email not verified. Please check your inbox.");
                }
                userOrAdmin = user;
                role = user.getAuthorities().iterator().next();
            } else if (adminRepository.findByUsername(username).isPresent()) {
                Admin admin = adminRepository.findByUsername(username).get();
                userOrAdmin = admin;
                role = admin.getAuthorities().iterator().next();
            } else {
                userOrAdmin = null;
                role = null;
            }

            String token = tokenService.generateJwt(auth);
            return new LoginResponseDTO(userOrAdmin, token, role);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect Credentials");
        } catch (AuthenticationException e) {
            throw new InternalAuthenticationServiceException("Authentication failed", e);
        }
    }

    public void verifyEmail(String token) {
        EmailVerificationToken verificationToken = emailVerificationTokenRepository.findByToken(token)
            .orElseThrow(() -> new RuntimeException("Invalid or expired token"));

        User user = userRepository.findByUsername(verificationToken.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isVerified()) {
            emailVerificationTokenRepository.deleteByToken(token);
            throw new RuntimeException("User already verified");
        }

        user.setVerified(true);
        userRepository.save(user);
        emailVerificationTokenRepository.deleteByToken(token);
    }

    public void verifyEmailChange(String token) {
        EmailVerificationToken verificationToken = emailVerificationTokenRepository.findByToken(token)
            .orElseThrow(() -> new RuntimeException("Invalid or expired token"));

        User user = userRepository.findByUsername(verificationToken.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getPendingEmail() != null) {
            user.setEmail(user.getPendingEmail());
            user.setPendingEmail(null);
            userRepository.save(user);
        }
        emailVerificationTokenRepository.deleteByToken(token);
    }
}