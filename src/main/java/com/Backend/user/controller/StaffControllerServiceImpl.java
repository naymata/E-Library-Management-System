package com.Backend.user.controller;

import com.Backend.user.dto.AddStaffRequest;
import com.Backend.user.dto.AddStaffResponse;
import com.Backend.user.dto.GetUsersRequest;
import com.Backend.user.dto.GetUsersResponse;
import com.Backend.user.exceptions.UsernameIsTakenException;
import com.Backend.user.service.StaffControllerService;
import com.Backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

import static com.Backend.config.utility.ELibraryUtility.*;

@RestController
@RequiredArgsConstructor
public class StaffControllerServiceImpl implements StaffControllerService {

    private final UserService service;
    private static final Logger logger = LoggerFactory.getLogger(StaffControllerServiceImpl.class);

    /**
     * Adds a new staff member based on the information provided in the {@link AddStaffRequest}.
     *
     * @param request The {@link AddStaffRequest} containing details of the staff member to be added.
     * @return An {@link AddStaffResponse} indicating the result of the addition operation.
     * @throws UsernameIsTakenException if the username for the staff member is already taken.
     */
    @Override
    public AddStaffResponse addStaff(AddStaffRequest request) {
        try {
            return service.addStaff(request);
        } catch (UsernameIsTakenException e) {
            return new AddStaffResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (RuntimeException e) {
            logger.error(e.getMessage(),e.fillInStackTrace());
            return new AddStaffResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), SOMETHING_WENT_WRONG);
        }
    }
    /**
     * Retrieves a list of staff members based on the provided {@link GetUsersRequest}.
     *
     * @param request The {@link GetUsersRequest} specifying parameters for retrieving staff members.
     * @return A {@link GetUsersResponse} containing the result of the retrieval operation.
     * @throws RuntimeException if an unexpected runtime exception occurs during the operation.
     */
    @Override
    public GetUsersResponse getStaff(GetUsersRequest request) {
        try {
            return service.getUsers(request);
        } catch (RuntimeException e) {
            logger.error(e.getMessage(),e.fillInStackTrace());
            return new GetUsersResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), SOMETHING_WENT_WRONG, null, new ArrayList<>());
        }
    }
}
