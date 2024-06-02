package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
class UserController {

    private final UserServiceImpl userService;

    private final UserMapper userMapper;

    @GetMapping
    public List<UserSummaryDto> getAllUsers() {
        return userService.findAllUsers()
                          .stream()
                          .map(userMapper::toSummaryDto)
                          .toList();
    }

    @PostMapping
    public User addUser(@RequestBody UserDto userDto) throws InterruptedException {

        // Demonstracja how to use @RequestBody
        System.out.println("User with e-mail: " + userDto.email() + "passed to the request");

        // TODO: saveUser with Service and return User

        User user = userMapper.toEntity(userDto);

        User savedUser = userService.createUser(user);

        return savedUser;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody com.capgemini.wsb.fitnesstracker.user.api.UserDto updatedUserDto) {
        User updatedUser = userService.updateUser(id, updatedUserDto);
        return userMapper.toDto(updatedUser);
    }

}