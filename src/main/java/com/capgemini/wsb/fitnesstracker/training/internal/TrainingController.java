package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingNotFoundException;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/v1/trainings")
public class TrainingController {

    private final TrainingServiceImpl trainingService;

    @Autowired
    public TrainingController(TrainingServiceImpl trainingService) {
        this.trainingService = trainingService;
    }

    @GetMapping
    public ResponseEntity<?> getAllTrainings() {
        return ResponseEntity.ok(trainingService.findAllTrainings());
    }

    @PostMapping
    public ResponseEntity<?> createTraining(@RequestBody Training trainingRequest) {
        User user = trainingRequest.getUser();
        // Dodaj logi, aby sprawdzić wartość obiektu użytkownika
        System.out.println("User from request: " + user);
        Date startTime = trainingRequest.getStartTime();
        Date endTime = trainingRequest.getEndTime();
        ActivityType activityType = trainingRequest.getActivityType();
        double distance = trainingRequest.getDistance();
        double averageSpeed = trainingRequest.getAverageSpeed();

        Training training = new Training(user, startTime, endTime, activityType, distance, averageSpeed);
        Training savedTraining = trainingService.saveTraining(training);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedTraining);
    }


    @GetMapping("/{userId}")
    public ResponseEntity<?> getTrainingsByUser(@PathVariable Long userId) {
        List<Training> trainings = trainingService.findTrainingsByUser(userId);
        return ResponseEntity.ok(trainings);
    }

    @GetMapping("/finished/{date}")
    public ResponseEntity<?> getTrainingsCompletedAfterDate(@PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
        List<Training> trainings = trainingService.findTrainingsCompletedAfterDate(date);
        return ResponseEntity.ok(trainings);
    }

    @GetMapping("/activityType/{activityType}")
    public ResponseEntity<?> getTrainingsByActivityType(@PathVariable("activityType") ActivityType activityType) {
        List<Training> trainings = trainingService.findTrainingsByActivityType(activityType);
        return ResponseEntity.ok(trainings);
    }

    @PutMapping("/{trainingId}")
    public ResponseEntity<?> updateTraining(@PathVariable("trainingId") Long trainingId, @RequestBody Training request) {
        try {
            Training updatedTraining = trainingService.updateTraining(trainingId, request);
            return ResponseEntity.ok(updatedTraining);
        } catch (TrainingNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

}
