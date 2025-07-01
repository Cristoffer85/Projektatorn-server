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
import cristoffer85.com.projektatornserver.MAINAPP.model.Role;
import cristoffer85.com.projektatornserver.MAINAPP.model.User;
import cristoffer85.com.projektatornserver.MAINAPP.repository.AdminRepository;
import cristoffer85.com.projektatornserver.MAINAPP.repository.RoleRepository;
import cristoffer85.com.projektatornserver.MAINAPP.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class AuthenticationService {                // Class that handles Registration of new user and employee, and login (Authenticates that they are valid) Uses LoginResponseDTO among others

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

        return userRepository.save(newUser);
    }

    public LoginResponseDTO loginUser(String username, String password) {
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            String token = tokenService.generateJwt(auth);

            Object userOrAdmin;
            Role role;
            if (userRepository.findByUsername(username).isPresent()) {
                userOrAdmin = userRepository.findByUsername(username).get();
                role = ((User) userOrAdmin).getAuthorities().iterator().next();
            } else if (adminRepository.findByUsername(username).isPresent()) {
                userOrAdmin = adminRepository.findByUsername(username).get();
                role = ((Admin) userOrAdmin).getAuthorities().iterator().next();
            } else {
                userOrAdmin = null;
                role = null;
            }

            return new LoginResponseDTO(userOrAdmin, token, role);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect Credentials");
        } catch (AuthenticationException e) {
            throw new InternalAuthenticationServiceException("Authentication failed", e);
        }
    }
}