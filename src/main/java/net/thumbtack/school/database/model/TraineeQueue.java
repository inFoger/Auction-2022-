package net.thumbtack.school.database.model;

import java.util.ArrayDeque;
import java.util.Queue;

public class TraineeQueue {
    Queue<Trainee> queue;

    public TraineeQueue(){
        queue = new ArrayDeque<>();
    }

    public void addTrainee(Trainee trainee){
        queue.add(trainee);
    }

    public Trainee removeTrainee() throws TrainingException {
        if(isEmpty()){
            throw new TrainingException(TrainingErrorCode.EMPTY_TRAINEE_QUEUE);
        }
        return queue.remove();
    }

    public boolean isEmpty(){
        return queue.isEmpty();
    }
}
