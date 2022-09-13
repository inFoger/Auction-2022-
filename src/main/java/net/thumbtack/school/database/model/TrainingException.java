package net.thumbtack.school.database.model;

public class TrainingException extends Exception{
    private final TrainingErrorCode trainingErrorCode;
    public TrainingException(TrainingErrorCode trainingErrorCode){
        super(trainingErrorCode.getErrorString());
        this.trainingErrorCode = trainingErrorCode;
    }

    public TrainingErrorCode getErrorCode(){
        return trainingErrorCode;
    }
}
