package com.Backend.user.implementation;

import com.Backend.email.exceptions.EmailIsInvalidException;
import com.Backend.email.exceptions.EmailIsTakenException;
import com.Backend.email.implementation.MailServiceImpl;
import com.Backend.user.dto.AddStaffRequest;
import com.Backend.user.dto.GetUsersRequest;
import com.Backend.user.dto.RegisterRequest;
import com.Backend.user.dto.UpdateUserRequest;
import com.Backend.user.exceptions.*;
import com.Backend.user.model.Role;
import com.Backend.user.model.User;
import com.Backend.user.repository.UserRepository;
import com.Backend.email.service.AccountVerificationService;
import com.Backend.user.service.UserService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.Backend.config.utility.ELibraryUtility.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceImplTest {
    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccountVerificationService accountVerificationService;

    @Mock
    private MailServiceImpl mailService;

    private UserService underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new UserServiceImpl(repository, passwordEncoder, accountVerificationService, mailService);
    }

    private final Long id = 1L;
    private static final String username = "nn",
            password = "password",
            email = "tt@gmail.com",
            phoneNumber = "+12399402",
            invalidEmail = "kwekm..@",
            invalidPhoneNumber = "999__22SS";


    @Test
    @Order(1)
    @DisplayName("Should add customer")
    public void shouldAddCustomer() {
        //given
        var request = registerRequest(username, password, email, phoneNumber);
        var expectedUser = user(username, password, email, phoneNumber);
        //when
        var response = underTest.addCustomer(request);
        //then
        verify(repository, times(1)).save(expectedUser);
        assertEquals(HttpStatus.OK.value(), response.status());
        assertEquals("Account " + CREATED_SUCCESSFULLY, response.message());
    }

    @Test
    @DisplayName("Should throw UsernameIsTakenException when given existing username")
    public void itShould_Throw_UsernameIsTakenException_When_Given_ExistingUsername() {
        when(repository.existsByUsername(username)).thenReturn(true);

        assertThatThrownBy(() -> underTest.addCustomer(registerRequest(username, password, email, phoneNumber)))
                .isInstanceOf(UsernameIsTakenException.class)
                .hasMessageContaining(stringFormatter("Username:", username, "is taken"));
    }

    @Test
    @DisplayName("Should throw EmailIsTakenException when given existing email")
    public void itShould_Throw_EmailIsTakenException_When_Given_ExistingEmail() {
        when(repository.existsByEmail(email)).thenReturn(true);

        assertThatThrownBy(() -> underTest.addCustomer(registerRequest(username, password, email, phoneNumber)))
                .isInstanceOf(EmailIsTakenException.class)
                .hasMessageContaining(stringFormatter("Email:", email, "is taken"));
    }

    @Test
    @DisplayName("Should throw EmailIsInvalidException when given wrong email")
    public void itShould_Throw_EmailIsInvalidException_When_Given_WrongEmail() {
        assertThatThrownBy(() -> underTest.addCustomer(registerRequest(username, password, invalidEmail, phoneNumber)))
                .isInstanceOf(EmailIsInvalidException.class)
                .hasMessageContaining(stringFormatter("Email:", invalidEmail, IS_INVALID));
    }

    @Test
    @DisplayName("Should throw PhoneNumberIsInvalidException when given wrong phone number")
    public void itShould_Throw_PhoneNumberIsInvalidException_When_Given_WrongPhoneNumber() {
        assertThatThrownBy(() -> underTest.addCustomer(registerRequest(username, password, email, invalidPhoneNumber)))
                .isInstanceOf(PhoneNumberIsInvalidException.class)
                .hasMessageContaining(stringFormatter("Phone number:", invalidPhoneNumber, IS_INVALID));
    }

    @Test
    @DisplayName("Should add staff")
    public void shouldAddStaff() {
        var request = new AddStaffRequest(username, password, Role.ADMIN);
        var response = underTest.addStaff(request);
        var admin = admin(username, password, Role.ADMIN);
        verify(repository, times(1)).save(admin);

        assertEquals(HttpStatus.OK.value(), response.status());
        assertEquals(SUCCESS, response.message());
    }

    @Test
    @DisplayName("Should throw UsernameIsTakenException when trying too add staff")
    public void itShould_Throw_UsernameIsTakenException_When_TryingToAddStaff() {
        var request = new AddStaffRequest(username, password, Role.ADMIN);
        when(repository.existsByUsername(username)).thenReturn(true);
        assertThatThrownBy(() -> underTest.addStaff(request))
                .isInstanceOf(UsernameIsTakenException.class)
                .hasMessageContaining(stringFormatter("Username:", username, "is taken"));
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when given wrong id when trying to update user")
    public void itShould_Throw_UserNotFoundException_When_Given_WrongId_When_TryingToUpdate() {
        assertThatThrownBy(() -> underTest.updateCustomer(new UpdateUserRequest(id, null, null, null, null)))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(stringFormatter("User with id:", id, NOT_FOUND));
    }

    @Test
    @DisplayName("Should update user")
    public void shouldUpdateUser() {
        var user = user(username, password, email, phoneNumber);
        when(repository.findById(id)).thenReturn(Optional.of(user));
        var request = new UpdateUserRequest(id, password, "", "", phoneNumber);
        var response = underTest.updateCustomer(request);

        verify(repository, times(1)).save(user);

        assertEquals(HttpStatus.OK.value(), response.status());
        assertEquals("Account " + UPDATED_SUCCESSFULLY, response.message());
    }

    @Test
    @DisplayName("Should throw PhoneNumberIsInvalidException when given wrong phone number when trying to update user")
    public void itShould_Throw_PhoneNumberIsInvalidException_When_Given_WrongPhoneNumber_When_TryingToUpdateUser() {
        var user = user(username, password, email, phoneNumber);
        when(repository.findById(id)).thenReturn(Optional.of(user));
        var request = new UpdateUserRequest(id, password, "", "", invalidPhoneNumber);
        assertThatThrownBy(() -> underTest.updateCustomer(request))
                .isInstanceOf(PhoneNumberIsInvalidException.class)
                .hasMessageContaining("Phone number", invalidPhoneNumber, "is " + INVALID_INFORMATION);
    }

    @Test
    @DisplayName("Should delete user")
    public void shouldDeleteUser() {
        when(repository.existsById(id)).thenReturn(true);
        var response = underTest.deleteUser(id);
        verify(repository, times(1)).deleteById(id);
        assertEquals(HttpStatus.OK.value(), response.status());
        assertEquals("Account " + DELETED_SUCCESSFULLY, response.message());
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when given wrong id trying to delete")
    public void itShould_Throw_UserNotFoundException_When_Given_WrongId() {
        assertThatThrownBy(() -> underTest.deleteUser(id))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(stringFormatter("User with id:", id, NOT_FOUND));
    }

    @Test
    @DisplayName("Should get users")
    public void shouldGetUsers() {
        var list = createUsersList(4);
        Page<User> userPage = new PageImpl<>(list, PageRequest.of(0, 4), list.size());
        var request = new GetUsersRequest(0, 4, Role.ADMIN);
        when(repository.getUserByRole(PageRequest.of(0, 4), Role.ADMIN)).thenReturn(userPage);
        var response = underTest.getUsers(request);

        assertEquals(HttpStatus.OK.value(), response.status());
        assertEquals(SUCCESS, response.message());

    }

    private List<User> createUsersList(Integer size) {
        var list = new ArrayList<User>();
        for (int i = 1; i <= size; i++) {
            list.add(new User((long) i, null, null, null, null, null, null, null, null, null, null, null));
        }
        return list;
    }

    private RegisterRequest registerRequest(String username, String password, String email, String phoneNumber) {
        return new RegisterRequest(username,
                password,
                "firstname",
                "lastname",
                email, phoneNumber);
    }

    private User user(String username, String password, String email, String phoneNumber) {
        return User
                .builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .firstName("firstname")
                .lastName("lastname")
                .email(email)
                .phoneNumber(phoneNumber)
                .isEnabled(false)
                .isAccountNonExpired(false)
                .isCredentialsNonExpired(false)
                .isAccountNonLocked(false)
                .role(Role.CUSTOMER)
                .build();
    }

    private User admin(String username, String password, Role role) {
        return User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .isEnabled(true)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .role(role)
                .build();
    }
}