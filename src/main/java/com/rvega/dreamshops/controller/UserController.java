package com.rvega.dreamshops.controller;

import com.rvega.dreamshops.dto.UserDto;
import com.rvega.dreamshops.exceptions.AlreadyExistsException;
import com.rvega.dreamshops.exceptions.ResourceNotFoundException;
import com.rvega.dreamshops.model.User;
import com.rvega.dreamshops.request.CreateUserRequest;
import com.rvega.dreamshops.request.UserUpdateRequest;
import com.rvega.dreamshops.response.ApiResponse;
import com.rvega.dreamshops.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {

    // Injected IUserService for user-related business logic.
    private final IUserService userService;

    /**
     * Retrieves a user by their ID.
     *
     * @param userId the ID of the user to retrieve
     * @return the user details in a response body, or a 404 error if not found
     */
    @GetMapping("/{userId}/user")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        try {
            // Retrieves the user by ID and converts it to DTO.
            User user = userService.getUserById(userId);
            UserDto userDto = userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("Success", userDto));
        } catch (ResourceNotFoundException e) {
            // If the user is not found, responds with a 404 error.
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    /**
     * Creates a new user based on the provided request.
     *
     * @param request the details of the user to create
     * @return a response indicating success or conflict (if the user already exists)
     */
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request) {
        try {
            // Creates a new user and converts it to DTO.
            User user = userService.createUser(request);
            UserDto userDto = userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("Create User Success!", userDto));
        } catch (AlreadyExistsException e) {
            // If the user already exists, responds with a 409 conflict error.
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    /**
     * Updates an existing user with the provided details.
     *
     * @param request the new details of the user
     * @param userId the ID of the user to update
     * @return the updated user details in a response body, or a 404 error if not found
     */
    @PutMapping("/{userId}/update")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UserUpdateRequest request, @PathVariable Long userId) {
        try {
            // Updates the user and converts it to DTO.
            User user = userService.updateUser(request, userId);
            UserDto userDto = userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("Update User Success!", userDto));
        } catch (ResourceNotFoundException e) {
            // If the user is not found, responds with a 404 error.
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    /**
     * Deletes a user by their ID.
     *
     * @param userId the ID of the user to delete
     * @return a response indicating success or a 404 error if the user is not found
     */
    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
        try {
            // Deletes the user by ID.
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("Delete User Success!", null));
        } catch (ResourceNotFoundException e) {
            // If the user is not found, responds with a 404 error.
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
