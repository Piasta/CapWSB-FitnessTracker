package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingNotFoundException;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingProvider;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingService;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

// TODO: Provide Impl
@Service
public class TrainingServiceImpl implements TrainingProvider, TrainingService {

    private final TrainingRepository trainingRepository;

    @Autowired
    public TrainingServiceImpl(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @Override
    public Optional<User> getTraining(Long trainingId) {
        return trainingRepository.findById(trainingId).map(Training::getUser);
    }

    public List<Training> findAllTrainings() {
        return trainingRepository.findAll();
    }

    @Override
    public Training saveTraining(Training training) {
        return trainingRepository.save(training);
    }

    @Override
    public List<Training> findTrainingsByUser(Long userId) {
        return trainingRepository.findByUserId(userId);
    }

    @Override
    public List<Training> findTrainingsCompletedAfterDate(Date date) {
        return trainingRepository.findByEndTimeAfter(date);
    }

    @Override
    public List<Training> findTrainingsByActivityType(ActivityType activityType) {
        return trainingRepository.findByActivityType(activityType);
    }

    @Override
    public Training updateTraining(Long trainingId, Training updatedTraining) {
        Optional<Training> optionalTraining = trainingRepository.findById(trainingId);
        if (optionalTraining.isPresent()) {
            Training training = optionalTraining.get();

            if (updatedTraining.getDistance() != 0) {
                training.setDistance(updatedTraining.getDistance());
            }
            if (updatedTraining.getAverageSpeed() != 0) {
                training.setAverageSpeed(updatedTraining.getAverageSpeed());
            }
            if (updatedTraining.getActivityType() != null) {
                training.setActivityType(updatedTraining.getActivityType());
            }
            if (updatedTraining.getStartTime() != null) {
                training.setStartTime(updatedTraining.getStartTime());
            }
            if (updatedTraining.getEndTime() != null) {
                training.setEndTime(updatedTraining.getEndTime());
            }

            return trainingRepository.save(training);
        } else {
            throw new TrainingNotFoundException(trainingId);
        }
    }

}
