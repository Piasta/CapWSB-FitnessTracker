package com.capgemini.wsb.fitnesstracker.user.api;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserProvider {

    /**
     * Retrieves a user based on their ID.
     * If the user with given ID is not found, then {@link Optional#empty()} will be returned.
     *
     * @param userId id of the user to be searched
     * @return An {@link Optional} containing the located user, or {@link Optional#empty()} if not found
     */
    Optional<User> getUser(Long userId);

    /**
     * Retrieves a user based on their email.
     * If the user with given email is not found, then {@link Optional#empty()} will be returned.
     *
     * @param email The email of the user to be searched
     * @return An {@link Optional} containing the located user, or {@link Optional#empty()} if not found
     */
    Optional<User> getUserByEmail(String email);

    /**
     * Retrieves all users.
     *
     * @return An {@link Optional} containing the all users,
     */
    List<User> findAllUsers();

    /**
     * Retrieves users based on their first name and last name.
     *
     * @param firstName The first name of the user to be searched
     * @param lastName The last name of the user to be searched
     * @return A list of users matching the first name and last name
     */
    List<User> findUsersByFirstNameAndLastName(String firstName, String lastName);

    /**
     * Retrieves users based on their birthdate.
     *
     * @param birthdate The birthdate of the user to be searched
     * @return A list of users matching the birthdate
     */
    List<User> findUsersByBirthdate(LocalDate birthdate);

    /**
     * Retrieves users based on their birthdate being after the specified date,
     * effectively returning users older than a given age.
     *
     * @param startDate The minimum birthdate of the users to be retrieved
     * @return A list of users whose birthdate is after the specified date
     */
    List<User> findUsersByBirthdateAfter(LocalDate startDate);

}
