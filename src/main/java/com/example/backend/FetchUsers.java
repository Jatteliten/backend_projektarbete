package com.example.backend;

import com.example.backend.security.Role;
import com.example.backend.security.RoleRepository;
import com.example.backend.security.User;
import com.example.backend.security.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;

@ComponentScan
public class FetchUsers implements CommandLineRunner {
    UserRepository userRepository;
    RoleRepository roleRepository;

    public FetchUsers(UserRepository userRepository, RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.findByName("Admin") == null) {
            addRole("Admin");
        }
        if (roleRepository.findByName("Receptionist") == null) {
            addRole("Receptionist");
        }
        if(userRepository.getUserByUsername("admin@admin.se") == null){
            addUser("admin@admin.se","Admin");
        }
        if(userRepository.getUserByUsername("recep@recep.se") == null){
            addUser("recep@recep.se","Receptionist");
        }
    }

    private void addUser(String mail, String group) {
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName(group));

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("password");
        User user = User.builder().enabled(true).password(hash).username(mail).roles(roles).build();
        userRepository.save(user);
    }

    private void addRole(String name) {
        roleRepository.save(Role.builder().name(name).build());
    }

}
