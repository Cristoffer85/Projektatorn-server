package cristoffer85.com.projektatornserver.MAINAPP.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cristoffer85.com.projektatornserver.MAINAPP.repository.AdminRepository;
import cristoffer85.com.projektatornserver.MAINAPP.repository.UserRepository;

// Override class for library UserDetailsService that lets you log in as Admin, Employee or User. Its needed for the security config, among others.

@Service
public class RoleService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getAuthorities()))
                .orElseGet(() -> adminRepository.findByUsername(username)
                        .map(admin -> new org.springframework.security.core.userdetails.User(admin.getUsername(), admin.getPassword(), admin.getAuthorities()))
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username)));
    }
}