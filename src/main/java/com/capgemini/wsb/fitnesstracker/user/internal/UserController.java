package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
class UserController {

    private final UserServiceImpl userService;

    private final UserMapper userMapper;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.findAllUsers()
                          .stream()
                          .map(userMapper::toDto)
                          .toList();
    }

    @GetMapping("/simple")
    public List<UserSummaryDto> getAllUsersBasicInfo() {
        return userService.findAllUsers()
                .stream()
                .map(userMapper::toSummaryDto)
                .toList();
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody UserDto userDto) throws InterruptedException {
        System.out.println("User with e-mail: " + userDto.email() + " passed to the request");

        User user = userMapper.toEntity(userDto);
        User savedUser = userService.createUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        User user = userService.getUser(id).orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toDto(user);
    }

    @GetMapping("/search")
    public List<UserDto> searchUsers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) LocalDate birthdate,
            @RequestParam(required = false) Integer minAge) {

        List<User> users;
        if (minAge != null) {
            LocalDate startDate = LocalDate.now().minusYears(minAge);
            users = userService.findUsersByBirthdateAfter(startDate);
        } else if (birthdate != null) {
            users = userService.findUsersByBirthdate(birthdate);
        } else if (firstName != null && lastName != null) {
            users = userService.findUsersByFirstNameAndLastName(firstName, lastName);
        } else {
            users = userService.findAllUsers();
        }

        return users.stream()
                .map(userMapper::toDto)
                .toList();
    }

    @GetMapping("/email")
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam String email) {
        Optional<User> user = userService.getUserByEmail(email);
        return user.map(value -> ResponseEntity.ok(userMapper.toDto(value)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/older/{date}")
    public ResponseEntity<List<User>> getUsersOlderThan(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<User> users = userService.findUsersByBirthdateAfter(date);
        return ResponseEntity.ok(users);
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody com.capgemini.wsb.fitnesstracker.user.api.UserDto updatedUserDto) {
        User updatedUser = userService.updateUser(id, updatedUserDto);
        return userMapper.toDto(updatedUser);
    }

}