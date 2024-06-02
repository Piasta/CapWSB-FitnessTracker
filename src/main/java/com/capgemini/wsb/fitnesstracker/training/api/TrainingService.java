package com.capgemini.wsb.fitnesstracker.training.api;

import com.capgemini.wsb.fitnesstracker.training.internal.ActivityType;
import com.capgemini.wsb.fitnesstracker.user.api.User;

import java.util.Date;
import java.util.List;

public interface TrainingService {

    Training saveTraining(Training training);

    List<Training> findTrainingsByUser(Long userId);

    List<Training> findTrainingsCompletedAfterDate(Date date);

    List<Training> findTrainingsByActivityType(ActivityType activityType);

    Training updateTraining(Long trainingId, Training request);
}
