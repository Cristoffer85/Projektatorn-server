package cristoffer85.com.projektatornserver.MAINAPP.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cristoffer85.com.projektatornserver.MAINAPP.dto.LoginResponseDTO;
import cristoffer85.com.projektatornserver.MAINAPP.dto.RegistrationDTO;
import cristoffer85.com.projektatornserver.MAINAPP.model.User;
import cristoffer85.com.projektatornserver.MAINAPP.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationDTO body) {
        try {
            User user = authenticationService.registerUser(body.getUsername(), body.getPassword(), body.getEmail());
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody RegistrationDTO body){
        try {
            LoginResponseDTO response = authenticationService.loginUser(body.getUsername(), body.getPassword());
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            // Return a 401 status code when the credentials are incorrect
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (AuthenticationException e) {
            // Return a 500 status code for other authentication errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
