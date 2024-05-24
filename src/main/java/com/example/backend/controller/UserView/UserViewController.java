package com.example.backend.controller.UserView;

import com.example.backend.model.Customer;
import com.example.backend.security.ConcreteUserDetails;
import com.example.backend.security.User;
import com.example.backend.security.UserDetailsServiceImpl;
import com.example.backend.security.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

        System.out.println(concreteUsers.get(0).getUsername());
        System.out.println(concreteUsers.get(1).getUsername());

        model.addAttribute("header", "Users");
        model.addAttribute("allUsers", concreteUsers);

        return "Users/manageUsers.html";

    }
    @GetMapping("/addUser")
    @PreAuthorize("isAuthenticated()")
    public String addUser() {
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
            // lägg till user i database
        }

        return addUser();
    }

    @GetMapping("/editUser")
    @PreAuthorize("isAuthenticated()")
    public String editUser() {
        return "Users/editUser.html";
    }


}
