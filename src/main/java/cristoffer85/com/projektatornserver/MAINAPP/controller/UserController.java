package cristoffer85.com.projektatornserver.MAINAPP.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cristoffer85.com.projektatornserver.MAINAPP.dto.SendOnlyUserNameDTO;
import cristoffer85.com.projektatornserver.MAINAPP.dto.UserUpdateDTO;
import cristoffer85.com.projektatornserver.MAINAPP.model.User;
import cristoffer85.com.projektatornserver.MAINAPP.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getOneUser/{username}")
    public User getOneUser(@PathVariable String username) {
        return userService.getOneUser(username);
    }

    @GetMapping("/all-users")
    public List<SendOnlyUserNameDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/updateUser/{username}")
    public User updateUser(@PathVariable String username, @RequestBody UserUpdateDTO userUpdateDto) {
        return userService.updateUser(username, userUpdateDto);
    }
}