package cristoffer85.com.projektatornserver.MAINAPP.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cristoffer85.com.projektatornserver.MAINAPP.dto.RegistrationDTO;
import cristoffer85.com.projektatornserver.MAINAPP.dto.UserUpdateDTO;
import cristoffer85.com.projektatornserver.MAINAPP.model.User;
import cristoffer85.com.projektatornserver.MAINAPP.service.AdminService;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // ################### Users ###################
    @GetMapping("/getAllUsers")
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }

    @GetMapping("/getOneUser/{username}")
    public User getOneUser(@PathVariable String username) {
        return adminService.getOneUser(username);
    }

    @PostMapping("/createUser")
    public User registerUser(@RequestBody RegistrationDTO body){
        return adminService.registerUser(body);
    }

    @PutMapping("/updateUser/{username}")
    public User updateUser(@PathVariable String username, @RequestBody UserUpdateDTO userUpdateDTO) {
        return adminService.updateUser(username, userUpdateDTO);}

    @DeleteMapping("/deleteOneUser/{username}")
    public void deleteOneUser(@PathVariable String username) {
        adminService.deleteOneUser(username);
    }
}