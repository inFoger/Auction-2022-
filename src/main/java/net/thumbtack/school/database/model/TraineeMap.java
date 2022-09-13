package net.thumbtack.school.database.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TraineeMap {
    Map<Trainee, String> map;

    public TraineeMap(){
        map = new HashMap<>();
    }

    public void addTraineeInfo(Trainee trainee, String institute) throws TrainingException {
        if(map.putIfAbsent(trainee, institute) != null){
            throw new TrainingException(TrainingErrorCode.DUPLICATE_TRAINEE);
        }
    }

    public void replaceTraineeInfo(Trainee trainee, String institute) throws TrainingException {
        if(map.replace(trainee, institute) == null){
            throw new TrainingException(TrainingErrorCode.TRAINEE_NOT_FOUND);
        }
    }

    public void removeTraineeInfo(Trainee trainee) throws TrainingException {
        if(map.remove(trainee) == null){
            throw new TrainingException(TrainingErrorCode.TRAINEE_NOT_FOUND);
        }
    }

    public int getTraineesCount(){
        return map.size();
    }

    public String getInstituteByTrainee(Trainee trainee) throws TrainingException {
        String result = map.get(trainee);
        if(result == null){
            throw new TrainingException(TrainingErrorCode.TRAINEE_NOT_FOUND);
        }
        return result;
    }

    public Set<Trainee> getAllTrainees(){
        return map.keySet();
    }

    public Set<String> getAllInstitutes(){
        return new HashSet<>(map.values());
    }

    public boolean isAnyFromInstitute(String institute){
        for(Trainee elem: map.keySet()){
            if(map.get(elem).equals(institute)){
                return true;
            }
        }
        return false;
    }

}
