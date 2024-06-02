package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserNotFoundException;
import com.capgemini.wsb.fitnesstracker.user.api.UserProvider;
import com.capgemini.wsb.fitnesstracker.user.api.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
class UserServiceImpl implements UserService, UserProvider {

    private final UserRepository userRepository;

    @Override
    public User createUser(final User user) {
        log.info("Creating User {}", user);
        if (user.getId() != null) {
            throw new IllegalArgumentException("User has already DB ID, update is not permitted!");
        }
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUser(final Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> getUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findUsersByFirstNameAndLastName(String firstName, String lastName) {
        return userRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName);
    }

    @Override
    public List<User> findUsersByBirthdate(LocalDate birthdate) {
        return userRepository.findByBirthdate(birthdate);
    }

    @Override
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        log.info("Deleting User {}", user);
        userRepository.delete(user);
    }

    @Override
    public List<User> findUsersByBirthdateAfter(LocalDate startDate) {
        return userRepository.findByBirthdateAfter(startDate);
    }

    @Override
    public User updateUser(Long userId, com.capgemini.wsb.fitnesstracker.user.api.UserDto updatedUserDto) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (updatedUserDto.firstName() != null) {
            existingUser.setFirstName(updatedUserDto.firstName());
        }

        if (updatedUserDto.lastName() != null) {
            existingUser.setLastName(updatedUserDto.lastName());
        }

        if (updatedUserDto.email() != null) {
            existingUser.setEmail(updatedUserDto.email());
        }

        return userRepository.save(existingUser);
    }

}