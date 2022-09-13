package net.thumbtack.school.database.model;

public enum TrainingErrorCode {
    TRAINEE_WRONG_FIRSTNAME("Trainee wrong firstname"),
    TRAINEE_WRONG_LASTNAME("Trainee wrong lastname"),
    TRAINEE_WRONG_RATING("Trainee wrong rating"),
    GROUP_WRONG_NAME("Group wrong name"),
    GROUP_WRONG_ROOM("Group wrong room"),
    TRAINEE_NOT_FOUND("Trainee not found"),
    SCHOOL_WRONG_NAME("School wrong name"),
    DUPLICATE_GROUP_NAME("Duplicate group name"),
    GROUP_NOT_FOUND("Group not found"),
    DUPLICATE_TRAINEE("Duplicate trainee"),
    EMPTY_TRAINEE_QUEUE("Empty trainee queue");
    private final String errorString;
    private TrainingErrorCode(String errorString){
        this.errorString = errorString;
    }

    public String getErrorString() {
        return errorString;
    }
}
