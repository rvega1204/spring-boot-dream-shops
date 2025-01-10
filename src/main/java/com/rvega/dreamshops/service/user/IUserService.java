package com.rvega.dreamshops.service.user;

import com.rvega.dreamshops.dto.UserDto;
import com.rvega.dreamshops.model.User;
import com.rvega.dreamshops.request.CreateUserRequest;
import com.rvega.dreamshops.request.UserUpdateRequest;

public interface IUserService {

    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request, Long userId);
    void deleteUser(Long userId);

    UserDto convertUserToDto(User user);

    User getAuthenticatedUser();
}
