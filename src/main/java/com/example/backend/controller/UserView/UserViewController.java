package com.example.backend.controller.UserView;

import com.example.backend.security.ConcreteUserDetails;
import com.example.backend.security.User;
import com.example.backend.security.UserDetailsServiceImpl;
import com.example.backend.security.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/Users")
public class UserViewController {
    private UserRepository userRepo;
    private UserDetailsServiceImpl userDetailsService;
    public UserViewController(UserDetailsServiceImpl userDetailsService, UserRepository userRepo) {
        this.userDetailsService = userDetailsService;
        this.userRepo = userRepo;
    }

    @GetMapping("/viewUsers")
    @PreAuthorize("isAuthenticated()")
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
    @PreAuthorize("isAuthenticated()")
    public String addUser(Model model) {
        model.addAttribute("username", " ");
        model.addAttribute("password", "   ");

        return "Users/addUser.html";
    }


    @PostMapping("/addUser")
    @PreAuthorize("isAuthenticated()")
    public String addUser(@RequestParam String username, @RequestParam String password,
                          @RequestParam boolean enabled, Model model){
        model.addAttribute("username", username);
        model.addAttribute("password", password);
        model.addAttribute("enabled", enabled);

        try {
            UserDetails test = userDetailsService.loadUserByUsername(username);

            model.addAttribute("header", "Users already exists");
        } catch (UsernameNotFoundException e) {

            // lägg till kod för att lägga till user i database
        }

        return "Users/manageUsers.html";
    }

    @GetMapping("/editUser/{username}")
    @PreAuthorize("isAuthenticated()")
    public String editUser(@PathVariable String username, Model model) {
        UserDetails user = userDetailsService.loadUserByUsername(username);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("password", user.getPassword());
        model.addAttribute("enabled", user.isEnabled());

        return "Users/editUser.html";
    }

    @PostMapping("editUser")
    @PreAuthorize("isAuthenticated()")
    public String updateUser() {

        //lägg till kod för att uppdatera user

        return "Users/manageUsers.html";

    }

    //funkar ej, fälten blir tomma
    @GetMapping("/deleteUser/{username}")
    @PreAuthorize("isAuthenticated()")
    public String deleteUser(@PathVariable String username, Model model) {
        UserDetails user = userDetailsService.loadUserByUsername(username);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("password", user.getPassword());
        model.addAttribute("enabled", user.isEnabled());



        model.addAttribute("role", user.getAuthorities());

        return "Users/deleteUser.html";
    }

    @PostMapping("deleteUser")
    @PreAuthorize("isAuthenticated()")
    public String deleteUser() {

        //lägg till kod för att ta bort user

        return "Users/manageUsers.html";

    }

}