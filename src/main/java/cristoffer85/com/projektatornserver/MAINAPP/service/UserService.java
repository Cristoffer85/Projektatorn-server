package cristoffer85.com.projektatornserver.MAINAPP.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cristoffer85.com.projektatornserver.MAINAPP.dto.SendOnlyUserNameDTO;
import cristoffer85.com.projektatornserver.MAINAPP.dto.UserUpdateDTO;
import cristoffer85.com.projektatornserver.MAINAPP.model.User;
import cristoffer85.com.projektatornserver.MAINAPP.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

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
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        user.setEmail(userUpdateDto.getEmail());
        user.setBirthday(userUpdateDto.getBirthday());
        user.setAddress(userUpdateDto.getAddress());
        user.setTelephone(userUpdateDto.getTelephone());
        return userRepository.save(user);
    }
}