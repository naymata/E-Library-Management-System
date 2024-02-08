package com.Backend.user.implementation;

import com.Backend.email.exceptions.EmailIsInvalidException;
import com.Backend.email.exceptions.EmailIsTakenException;
import com.Backend.email.implementation.MailServiceImpl;
import com.Backend.user.dto.RegisterRequest;
import com.Backend.user.dto.RegisterResponse;
import com.Backend.user.dto.*;
import com.Backend.user.exceptions.*;
import com.Backend.email.model.NotificationEmail;
import com.Backend.user.model.Role;
import com.Backend.user.model.User;
import com.Backend.user.repository.UserRepository;
import com.Backend.email.service.AccountVerificationService;
import com.Backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.Backend.config.utility.ELibraryUtility.*;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;
    private final AccountVerificationService accountVerificationService;
    private final MailServiceImpl mailService;

    /**
     * Registers a new customer based on the provided registration request.
     *
     * @param request The registration request containing customer information.
     * @return The response indicating the success of customer registration.
     * @throws UsernameIsTakenException   if the specified username is already taken.
     * @throws EmailIsTakenException      if the specified email is already taken.
     * @throws EmailIsInvalidException    if the specified email is invalid.
     * @throws PhoneNumberIsInvalidException if the specified phone number is invalid.
     */
    @Override
    public RegisterResponse addCustomer(RegisterRequest request) {
        if (repository.existsByUsername(request.username())) {
            throw new UsernameIsTakenException(stringFormatter("Username:", request.username(), "is taken"));
        }
        if (repository.existsByEmail(request.email())) {
            throw new EmailIsTakenException(stringFormatter("Email:", request.email(), "is taken"));
        }
        if (!isEmailValid(request.email())) {
            throw new EmailIsInvalidException(stringFormatter("Email:", request.email(), IS_INVALID));
        }
        if (!isPhoneNumberValid(request.phoneNumber())) {
            throw new PhoneNumberIsInvalidException(stringFormatter("Phone number:", request.phoneNumber(), IS_INVALID));
        }
        var customer = createCustomer(request);
        sendToken(customer);
        repository.save(customer);
        return new RegisterResponse(HttpStatus.OK.value(), "Account " + CREATED_SUCCESSFULLY);
    }

    /**
     * Adds a new staff member based on the provided request.
     *
     * @param request The request containing staff information.
     * @return The response indicating the success of adding the staff member.
     * @throws UsernameIsTakenException if the specified username is already taken.
     */
    @Override
    public AddStaffResponse addStaff(AddStaffRequest request) {
        if (repository.existsByUsername(request.username())) {
            throw new UsernameIsTakenException(stringFormatter("Username:", request.username(), "is taken"));
        }
        var staff = createStaff(request);
        repository.save(staff);
        return new AddStaffResponse(HttpStatus.OK.value(), SUCCESS);
    }
    /**
     * Updates customer information based on the provided request.
     *
     * @param request The request containing updated customer information.
     * @return The response indicating the success of updating customer information.
     * @throws UserNotFoundException      if the user with the specified ID is not found.
     * @throws PhoneNumberIsInvalidException if the specified phone number is invalid.
     */
    @Override
    public UpdateUserResponse updateCustomer(UpdateUserRequest request) {
        var user = repository.findById(request.id())
                .orElseThrow(() -> new UserNotFoundException(stringFormatter("User with id:", request.id(), NOT_FOUND)));
        if (!isPhoneNumberValid(request.phoneNumber())) {
            throw new PhoneNumberIsInvalidException(stringFormatter("Phone number:", request.phoneNumber(), IS_INVALID));
        }
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPhoneNumber(request.phoneNumber());
        repository.save(user);
        return new UpdateUserResponse(HttpStatus.OK.value(), "Account " + UPDATED_SUCCESSFULLY);
    }
    /**
     * Deletes a user account based on the provided user ID.
     *
     * @param id The ID of the user account to be deleted.
     * @return The response indicating the success of deleting the user account.
     * @throws UserNotFoundException if the user with the specified ID is not found.
     */
    @Override
    public DeleteUserResponse deleteUser(Long id) {
        if (!repository.existsById(id)) {
            throw new UserNotFoundException(stringFormatter("User with id:", id, NOT_FOUND));
        }
        repository.deleteById(id);
        return new DeleteUserResponse(HttpStatus.OK.value(), "Account " + DELETED_SUCCESSFULLY);
    }
    /**
     * Retrieves a paginated list of users based on the specified request criteria.
     *
     * @param request The request containing pagination details and optional role filtering.
     * @return The response containing the paginated list of users.
     * @throws UserPaginationSizeException if the specified page size is invalid.
     * @throws UserPaginationPageException if the specified page number is invalid.
     */
    @Override
    public GetUsersResponse getUsers(GetUsersRequest request) {
        if (!checkPageSize(request.size())) {
            throw new UserPaginationSizeException(stringFormatter("Size:", request.size(), IS_INVALID));
        }
        if (request.page() < 0) {
            throw new UserPaginationPageException(stringFormatter("Page:", request.page(), IS_INVALID));
        }
        var page = PageRequest.of(request.page(), request.size());
        var data = repository.getUserByRole(page, request.role());
        return new GetUsersResponse(HttpStatus.OK.value(), SUCCESS, data.getTotalPages(), data.getContent());
    }
    /**
     * Creates a new staff user based on the provided staff request.
     *
     * @param request The request containing staff information.
     * @return The created staff user.
     */
    private User createStaff(AddStaffRequest request) {
        return User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .isEnabled(true)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .role(request.role())
                .build();
    }
    /**
     * Creates a new customer user based on the provided registration request.
     *
     * @param request The registration request containing customer information.
     * @return The created customer user.
     */
    private User createCustomer(RegisterRequest request) {
        return User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .isEnabled(false)
                .isAccountNonExpired(false)
                .isCredentialsNonExpired(false)
                .isAccountNonLocked(false)
                .role(Role.CUSTOMER)
                .build();
    }
    /**
     * Checks if the specified email is in a valid format.
     *
     * @param email The email to be checked.
     * @return true if the email is in a valid format, false otherwise.
     */
    private Boolean isEmailValid(String email) {
        String EMAIL_REGEX =
                "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }
    /**
     * Checks if the specified phone number is in a valid format.
     *
     * @param phoneNumber The phone number to be checked.
     * @return true if the phone number is in a valid format, false otherwise.
     */
    private Boolean isPhoneNumberValid(String phoneNumber) {
        String PHONE_NUMBER_REGEX = "^\\+[0-9]+$";
        Pattern PHONE_NUMBER_PATTERN = Pattern.compile(PHONE_NUMBER_REGEX);
        Matcher matcher = PHONE_NUMBER_PATTERN.matcher(phoneNumber);
        return matcher.matches();
    }

    /**
     * Sends an email verification token to the user for account activation.
     *
     * @param email The email address of the user.
     * @param token The verification token.
     * @return The notification email object.
     */
    private NotificationEmail sendEmailVerificationToken(final String email, final String token) {
        return new NotificationEmail(
                "Please Activate your Account: "
                , email,
                "Thank you for signing up to E-Library "
                        + "please click the link: "
                        + "<a href="
                        + "http://localhost:8080/api/v1/verification/"
                        + token
                        + ">"
                        + "Activate"
                        + "</a>"
                        + "\t so you can activate your account"
        );
    }
    /**
     * Sends a verification token to the user for account activation.
     *
     * @param user The user to whom the verification token will be sent.
     */
    private void sendToken(User user) {
        var token = accountVerificationService.generateVerificationToken(user);
        mailService.sendEmail(sendEmailVerificationToken(user.getEmail(), token));
    }

}
