package cristoffer85.com.projektatornserver.MAINAPP.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cristoffer85.com.projektatornserver.MAINAPP.dto.RegistrationDTO;
// import cristoffer85.com.projektatornserver.MAINAPP.dto.UserUpdateDTO;
import cristoffer85.com.projektatornserver.MAINAPP.model.User;
import cristoffer85.com.projektatornserver.MAINAPP.repository.UserRepository;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationService authenticationService;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getOneUser(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public User registerUser(RegistrationDTO body){
        return authenticationService.registerUser(body.getUsername(), body.getPassword());
    }

    /*
    public User updateUser(String username, UserUpdateDTO userUpdateDTO) {
        User existingUser = userRepository.findByUsername(username).orElse(null);
        if (existingUser != null) {
            existingUser.setBirthdate(userUpdateDTO.getBirthdate());
            existingUser.setEmail(userUpdateDTO.getEmail());
            existingUser.setForestanimal(userUpdateDTO.getForestanimal());
            existingUser.setSoursnack(userUpdateDTO.getSoursnack());
            existingUser.setAvatar(userUpdateDTO.getAvatar());
            return userRepository.save(existingUser);
        }
        return null;
    }
    */

    public void deleteOneUser(String username) {
        userRepository.deleteByUsername(username);
    }
}