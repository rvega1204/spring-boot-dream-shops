package com.rvega.dreamshops.service.user;

import com.rvega.dreamshops.dto.UserDto;
import com.rvega.dreamshops.exceptions.AlreadyExistsException;
import com.rvega.dreamshops.exceptions.ResourceNotFoundException;
import com.rvega.dreamshops.model.User;
import com.rvega.dreamshops.repository.UserRepository;
import com.rvega.dreamshops.request.CreateUserRequest;
import com.rvega.dreamshops.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    // Injected UserRepository for database operations on User entity.
    private final UserRepository userRepository;

    // Injected ModelMapper for converting User entities to UserDto objects.
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Retrieves a User by their ID.
     *
     * @param userId the ID of the user to retrieve
     * @return the User object corresponding to the given ID
     * @throws ResourceNotFoundException if no user is found with the provided ID
     */
    @Override
    public User getUserById(Long userId) {
        // Fetches the user from the repository or throws an exception if not found.
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }

    /**
     * Creates a new User from the given request.
     *
     * @param request the request object containing the user details
     * @return the newly created User object
     * @throws AlreadyExistsException if a user already exists with the same email
     */
    @Override
    public User createUser(CreateUserRequest request) {
        // Ensures that no user exists with the provided email before creating a new one.
        return Optional.of(request)
                .filter(user -> !userRepository.existsByEmail(request.getEmail())) // Check if email is already taken
                .map(req -> {
                    // Create a new user and save it to the repository
                    User user = new User();
                    user.setEmail(request.getEmail());
                    user.setPassword(passwordEncoder.encode(request.getPassword()));
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new AlreadyExistsException(request.getEmail() + " already exists!"));
    }

    /**
     * Updates an existing user's details.
     *
     * @param request the request object containing the updated user details
     * @param userId  the ID of the user to update
     * @return the updated User object
     * @throws ResourceNotFoundException if the user with the given ID does not exist
     */
    @Override
    public User updateUser(UserUpdateRequest request, Long userId) {
        // Retrieves the user by ID and updates their details
        return userRepository.findById(userId).map(existingUser -> {
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());
            return userRepository.save(existingUser); // Save the updated user
        }).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }

    /**
     * Deletes a user by their ID.
     *
     * @param userId the ID of the user to delete
     * @throws ResourceNotFoundException if the user does not exist
     */
    @Override
    public void deleteUser(Long userId) {
        // Attempts to delete the user, or throws an exception if the user does not exist
        userRepository.findById(userId).ifPresentOrElse(userRepository::delete, () -> {
            throw new ResourceNotFoundException("User not found!");
        });
    }

    /**
     * Converts a User entity to a UserDto.
     *
     * @param user the User entity to convert
     * @return the corresponding UserDto object
     */
    @Override
    public UserDto convertUserToDto(User user) {
        // Uses ModelMapper to convert the User entity to a UserDto
        return modelMapper.map(user, UserDto.class);
    }

    /**
     * Retrieves the currently authenticated user from the security context.
     *
     * @return the authenticated User object
     * @throws AuthenticationException if no user is currently authenticated
     */
    @Override
    public User getAuthenticatedUser() {
        // Obtain the current authentication object from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Extract the email from the authentication object
        String email = authentication.getName();

        // Retrieve the user from the database using their email
        return userRepository.findByEmail(email);
    }
}
