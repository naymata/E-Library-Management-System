package com.Backend.user.controller;

import com.Backend.email.exceptions.EmailIsInvalidException;
import com.Backend.email.exceptions.EmailIsTakenException;
import com.Backend.user.dto.*;
import com.Backend.user.exceptions.*;
import com.Backend.user.service.UsersControllerService;
import com.Backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import static com.Backend.config.utility.ELibraryUtility.SOMETHING_WENT_WRONG;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class UsersControllerServiceImpl implements UsersControllerService {

    private final UserService service;
    private static final Logger logger = LoggerFactory.getLogger(UsersControllerServiceImpl.class);

    /**
     * Registers a new customer based on the information provided in the {@link RegisterRequest}.
     *
     * @param request The {@link RegisterRequest} containing details of the customer to be registered.
     * @return A {@link RegisterResponse} indicating the result of the registration operation.
     * @throws UsernameIsTakenException if the username for the customer is already taken.
     * @throws EmailIsTakenException if the email for the customer is already taken.
     * @throws EmailIsInvalidException if the provided email is invalid.
     * @throws PhoneNumberIsInvalidException if the provided phone number is invalid.
     */
    @Override
    public RegisterResponse register(RegisterRequest request) {
        try {
            return service.addCustomer(request);
        } catch (UsernameIsTakenException | EmailIsTakenException | EmailIsInvalidException |
                 PhoneNumberIsInvalidException e) {
            return new RegisterResponse(BAD_REQUEST.value(), e.getMessage());
        } catch (RuntimeException e) {
            logger.error(e.getMessage(),e.fillInStackTrace());
            return new RegisterResponse(INTERNAL_SERVER_ERROR.value(), SOMETHING_WENT_WRONG);
        }
    }
    /**
     * Updates the information of a customer based on the provided {@link UpdateUserRequest}.
     *
     * @param request The {@link UpdateUserRequest} containing updated customer information.
     * @return An {@link UpdateUserResponse} indicating the result of the update operation.
     * @throws UserNotFoundException if the customer with the specified ID is not found.
     */
    @Override
    public UpdateUserResponse update(UpdateUserRequest request) {
        try {
            return service.updateCustomer(request);
        } catch (UserNotFoundException e) {
            return new UpdateUserResponse(BAD_REQUEST.value(), e.getMessage());
        } catch (RuntimeException e) {
            logger.error(e.getMessage(),e.fillInStackTrace());
            return new UpdateUserResponse(INTERNAL_SERVER_ERROR.value(), SOMETHING_WENT_WRONG);
        }
    }
    /**
     * Deletes a customer account with the specified ID.
     *
     * @param id The ID of the customer account to be deleted.
     * @return A {@link DeleteUserResponse} indicating the result of the deletion operation.
     * @throws UserNotFoundException if the customer with the specified ID is not found.
     */
    @Override
    public DeleteUserResponse deleteUser(Long id) {
        try {
            return service.deleteUser(id);
        } catch (UserNotFoundException e) {
            return new DeleteUserResponse(BAD_REQUEST.value(), e.getMessage());
        } catch (RuntimeException e) {
            logger.error(e.getMessage(),e.fillInStackTrace());
            return new DeleteUserResponse(INTERNAL_SERVER_ERROR.value(), SOMETHING_WENT_WRONG);
        }
    }

}
