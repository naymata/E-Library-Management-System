package com.Backend.user.service;

import com.Backend.user.dto.RegisterRequest;
import com.Backend.user.dto.RegisterResponse;
import com.Backend.user.dto.*;


public interface UserService {

    RegisterResponse addCustomer(RegisterRequest request);

    UpdateUserResponse updateCustomer(UpdateUserRequest request);

    DeleteUserResponse deleteUser(Long id);

    AddStaffResponse addStaff(AddStaffRequest request);

    GetUsersResponse getUsers(GetUsersRequest request);
}
