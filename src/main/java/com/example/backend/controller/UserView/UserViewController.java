package com.example.backend.controller.UserView;

import com.example.backend.security.ConcreteUserDetails;
import com.example.backend.security.PasswordLink;
import com.example.backend.security.PasswordLinkRepository;
import com.example.backend.security.Role;
import com.example.backend.security.RoleRepository;
import com.example.backend.security.User;
import com.example.backend.security.UserDetailsServiceImpl;
import com.example.backend.security.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/Users")
public class UserViewController {
    private UserRepository userRepo;
    private RoleRepository roleRepository;
    private UserDetailsServiceImpl userDetailsService;
    private PasswordLinkRepository passwordLinkRepository;
    public UserViewController(UserDetailsServiceImpl userDetailsService, UserRepository userRepo,
                              RoleRepository roleRepository, PasswordLinkRepository passwordLinkRepository) {
        this.userDetailsService = userDetailsService;
        this.userRepo = userRepo;
        this.roleRepository = roleRepository;
        this.passwordLinkRepository = passwordLinkRepository;
    }

    @GetMapping("/viewUsers")
    @PreAuthorize("hasAuthority('Admin')")
    public String getAllUsers(Model model){

        List<User> users = (List<User>) userRepo.findAll();
        List<ConcreteUserDetails> concreteUsers = new ArrayList<>();

        for (User user : users) {
            concreteUsers.add(new ConcreteUserDetails(user));
        }

        model.addAttribute("header", "Users");
        model.addAttribute("allUsers", concreteUsers);

        return "Users/manageUsers.html";

    }
    @GetMapping("/addUser")
    @PreAuthorize("hasAuthority('Admin')")
    public String addUser() {

        return "Users/addUser.html";
    }


    @PostMapping("/addUser")
    @PreAuthorize("hasAuthority('Admin')")
    public String addUser(@RequestParam String username, @RequestParam String password,
                          @RequestParam boolean enabled, @RequestParam boolean admin,
                          @RequestParam boolean recep,  Model model){
        model.addAttribute("username", username);
        model.addAttribute("password", password);
        model.addAttribute("enabled", enabled);

        try {
            UserDetails test = userDetailsService.loadUserByUsername(username);

            model.addAttribute("header", "User already exists");
        } catch (UsernameNotFoundException e) {
            ArrayList<Role> roles = new ArrayList<>();
            if(admin){
                roles.add(roleRepository.findByName("Admin"));
            }
            if(recep){
                roles.add(roleRepository.findByName("Receptionist"));
            }

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String hash = encoder.encode(password);

            User user = User.builder()
                    .username(username)
                    .password(hash)
                    .enabled(enabled)
                    .roles(roles)
                    .build();
            userRepo.save(user);
        }

        return getAllUsers(model);
    }

    @GetMapping("/editUser/{username}")
    @PreAuthorize("hasAuthority('Admin')")
    public String editUser(@PathVariable String username, Model model) {
        getUserAttributes(username, model);

        return "Users/editUser.html";
    }

    @PostMapping("/editUser")
    @PreAuthorize("hasAuthority('Admin')")
    public String editUser(@RequestParam String username, @RequestParam String originalUsername,
                           @RequestParam String password, @RequestParam String originalPassword,
                           @RequestParam(required = false) boolean enabled, @RequestParam(required = false) boolean adminRole,
                           @RequestParam(required = false) boolean recepRole, Model model) {

        User user = userRepo.getUserByUsername(originalUsername);
        ArrayList<Role> roles = new ArrayList<>();

        if(adminRole){
            roles.add(roleRepository.findByName("Admin"));
        }
        if(recepRole){
            roles.add(roleRepository.findByName("Receptionist"));
        }
        if (!originalPassword.equals(password)) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String hash = encoder.encode(password);
            user.setPassword(hash);
        }
        user.setUsername(username);
        user.setEnabled(enabled);
        user.setRoles(roles);

        userRepo.save(user);

        return getAllUsers(model);
    }

    @GetMapping("/deleteUser/{username}")
    @PreAuthorize("hasAuthority('Admin')")
    public String deleteUser(@PathVariable String username, Model model) {
        getUserAttributes(username, model);

        return "Users/deleteUser.html";
    }

    @PostMapping("/deleteUser")
    @PreAuthorize("hasAuthority('Admin')")
    public String deleteUserById(@RequestParam String username, Model model) {

        User user = userRepo.getUserByUsername(username);
        List<PasswordLink> passwordRequests = passwordLinkRepository.findAllByUserId(user.getId());
        passwordLinkRepository.deleteAll(passwordRequests);
        userRepo.deleteById(user.getId());

        return getAllUsers(model);

    }

    private void getUserAttributes(String username, Model model) {
        User user = userRepo.getUserByUsername(username);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("password", user.getPassword());
        model.addAttribute("enabled", user.isEnabled());
        List<Role> roles = user.getRoles();
        if(roles.contains(roleRepository.findByName("Admin"))){
            model.addAttribute("adminRole" , true);
        }
        if(roles.contains(roleRepository.findByName("Receptionist"))){
            model.addAttribute("recepRole" , true);
        }
    }

}