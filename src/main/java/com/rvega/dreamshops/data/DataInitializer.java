package com.rvega.dreamshops.data;

import com.rvega.dreamshops.model.Role;
import com.rvega.dreamshops.model.User;
import com.rvega.dreamshops.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * This class initializes default data for the application, such as users and roles.
 * It implements the {@link ApplicationListener} interface to listen for the {@link ApplicationReadyEvent}.
 *
 * @author rvega
 */
@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * This method is called when the application is ready.
     * It creates default users and roles if they do not exist.
     *
     * @param event The {@link ApplicationReadyEvent} that triggered this method.
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRoles = Set.of("ROLE_ADMIN", "ROLE_USER");
        createDefaultUserIfNotExits();
        createDefaultRoleIfNotExits(defaultRoles);
        createDefaultAdminIfNotExits();
    }

    /**
     * This method creates default user accounts if they do not exist.
     * It uses a loop to create 5 default users with the role "ROLE_USER".
     */
    private void createDefaultUserIfNotExits() {
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        for (int i = 1; i <= 5; i++) {
            String defaultEmail = "user" + i + "@email.com";
            if (userRepository.existsByEmail(defaultEmail)) {
                continue;
            }

            User user = new User();
            user.setFirstName("The User");
            user.setLastName("User" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(userRole));
            userRepository.save(user);
            System.out.println("Default user " + i + " created successfully.");
        }
    }

    /**
     * This method creates default admin accounts if they do not exist.
     * It uses a loop to create 2 default admins with the role "ROLE_ADMIN".
     */
    private void createDefaultAdminIfNotExits() {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();
        for (int i = 1; i <= 2; i++) {
            String defaultEmail = "admin" + i + "@email.com";
            if (userRepository.existsByEmail(defaultEmail)) {
                continue;
            }

            User user = new User();
            user.setFirstName("Admin");
            user.setLastName("Admin" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(adminRole));
            userRepository.save(user);
            System.out.println("Default admin user " + i + " created successfully.");
        }
    }

    /**
     * This method creates default roles if they do not exist.
     * It uses a stream to filter and save new roles.
     *
     * @param roles A set of default role names.
     */
    private void createDefaultRoleIfNotExits(Set<String> roles) {
        roles.stream()
                .filter(role -> roleRepository.findByName(role).isEmpty())
                .map(Role::new)
                .forEach(roleRepository::save);
    }
}