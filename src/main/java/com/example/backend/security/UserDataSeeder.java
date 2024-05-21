package com.example.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDataSeeder {
    final UserRepository userRepository;
    final RoleRepository roleRepository;

    public UserDataSeeder(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public void Seed(){
        if (roleRepository.findByName("Admin") == null) {
            addRole("Admin");
        }
        if (roleRepository.findByName("Receptionist") == null) {
            addRole("Receptionist");
        }
        if(userRepository.getUserByUsername("admin") == null){
            addUser("admin@admin.se","Admin");
        }
        if(userRepository.getUserByUsername("recep") == null){
            addUser("recep@recep.se","Receptionist");
        }
    }

    private void addUser(String mail, String group) {
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName(group));

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("Hejsan123#");
        User user = User.builder().enabled(true).password(hash).username(mail).roles(roles).build();
        userRepository.save(user);
    }

    private void addRole(String name) {
        Role role = new Role();
        roleRepository.save(Role.builder().name(name).build());
    }

}
