package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Query searching users by email address. It matches by exact match.
     *
     * @param email email of the user to search
     * @return {@link Optional} containing found user or {@link Optional#empty()} if none matched
     */
    default Optional<User> findByEmail(String email) {
        return findAll().stream()
                        .filter(user -> Objects.equals(user.getEmail(), email))
                        .findFirst();
    }

    /**
     * Query searching users by first name and last name. It matches case-insensitively.
     *
     * @param firstName first name of the user to search
     * @param lastName last name of the user to search
     * @return {@link List} containing found users
     */
    default List<User> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName) {
        return findAll().stream()
                .filter(user -> user.getFirstName().equalsIgnoreCase(firstName)
                        && user.getLastName().equalsIgnoreCase(lastName))
                .toList();
    }

    /**
     * Query searching users by birthdate.
     *
     * @param birthdate birthdate of the user to search
     * @return {@link List} containing found users
     */
    default List<User> findByBirthdate(LocalDate birthdate) {
        return findAll().stream()
                .filter(user -> Objects.equals(user.getBirthdate(), birthdate))
                .toList();
    }

    /**
     * Query searching users by birthdate after the specified date.
     *
     * @param startDate The minimum birthdate of the users to be retrieved
     * @return A list of users whose birthdate is after the specified date
     */
    default List<User> findByBirthdateAfter(LocalDate startDate) {
        return findAll().stream()
                .filter(user -> user.getBirthdate().isBefore(startDate))
                .toList();
    }

}
