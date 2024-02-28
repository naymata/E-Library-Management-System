package com.Backend.user.service;

import com.Backend.user.dto.AddStaffRequest;
import com.Backend.user.dto.AddStaffResponse;
import com.Backend.user.dto.GetUsersRequest;
import com.Backend.user.dto.GetUsersResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping(path = "/api/v1/staff")
public interface StaffControllerService {
    @PostMapping("/add-staff")
    AddStaffResponse addStaff(@Valid @RequestBody AddStaffRequest request);

    @GetMapping("/get-staff")
    GetUsersResponse getStaff(@Valid @RequestBody GetUsersRequest request);
}
