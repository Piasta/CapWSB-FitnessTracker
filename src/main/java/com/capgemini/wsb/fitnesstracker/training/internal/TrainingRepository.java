package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

interface TrainingRepository extends JpaRepository<Training, Long> {
    List<Training> findByUserId(Long userId);

    List<Training> findByEndTimeAfter(Date date);

    @Query("SELECT t FROM Training t WHERE t.activityType = ?1")
    List<Training> findByActivityType(ActivityType activityType);
}
