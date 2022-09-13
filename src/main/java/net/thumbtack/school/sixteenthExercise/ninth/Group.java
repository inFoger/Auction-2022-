package net.thumbtack.school.sixteenthExercise.ninth;

import net.thumbtack.school.database.model.Trainee;
import net.thumbtack.school.database.model.TrainingErrorCode;
import net.thumbtack.school.database.model.TrainingException;

import java.util.*;

//9. Изменить классы Group и School из Задания 10 Заочной Школы,
// обеспечив многопоточный доступ. В тех случаях, когда это возможно,
// использовать конкурентные коллекции.
public class Group {
    private String name;
    private String room;
    private List<Trainee> trainees;

    public Group(String name, String room) throws TrainingException {
        setName(name);
        setRoom(room);
        trainees = Collections.synchronizedList(new ArrayList<>());
    }

    public String getName(){
        return name;
    }

    public void setName(String name) throws TrainingException {
        if(name == null || name.equals("")){
            throw new TrainingException(TrainingErrorCode.GROUP_WRONG_NAME);
        }
        this.name = name;
    }

    public String getRoom(){
        return room;
    }

    public void setRoom(String room) throws TrainingException {
        if(room == null || room.equals("")){
            throw new TrainingException(TrainingErrorCode.GROUP_WRONG_ROOM);
        }
        this.room = room;
    }

    public List<Trainee> getTrainees(){
        return trainees;
    }

    public void  addTrainee(Trainee trainee){
        trainees.add(trainee);
    }

    public void  removeTrainee(Trainee trainee) throws TrainingException {
        if(!trainees.contains(trainee)){
            throw new TrainingException(TrainingErrorCode.TRAINEE_NOT_FOUND);
        }
        trainees.remove(trainee);
    }

    public void  removeTrainee(int index) throws TrainingException {
        if(index < 0 || index >= trainees.size()){
           throw new TrainingException(TrainingErrorCode.TRAINEE_NOT_FOUND);
        }
        trainees.remove(index);
    }

    public Trainee getTraineeByFirstName(String firstName) throws TrainingException {
        for(Trainee elem: trainees){
            if(elem.getFirstName().equals(firstName)){
                return elem;
            }
        }
        throw new TrainingException(TrainingErrorCode.TRAINEE_NOT_FOUND);
    }

    public Trainee  getTraineeByFullName(String fullName) throws TrainingException {
        for(Trainee elem: trainees){
            if(elem.getFullName().equals(fullName)){
                return elem;
            }
        }
        throw new TrainingException(TrainingErrorCode.TRAINEE_NOT_FOUND);
    }

    public void  sortTraineeListByFirstNameAscendant(){
        trainees.sort(Comparator.comparing(Trainee::getFirstName));
    }

    public void  sortTraineeListByRatingDescendant(){
        trainees.sort((t1, t2)->-(t1.getRating()-t2.getRating()));
    }

    public void  reverseTraineeList(){
        Collections.reverse(trainees);
    }

    public void  rotateTraineeList(int positions){
        Collections.rotate(trainees, positions);
    }

    public List<Trainee>  getTraineesWithMaxRating() throws TrainingException {
        if(trainees.isEmpty()){
            throw new TrainingException(TrainingErrorCode.TRAINEE_NOT_FOUND);
        }
        List<Trainee> result = new ArrayList<>(trainees.size());

        result.add(trainees.get(0));
        int maxRating = trainees.get(0).getRating();
        for(int i = 1; i < trainees.size(); i++){
            if(maxRating == trainees.get(i).getRating()){
                result.add(trainees.get(i));
            } else if(maxRating < trainees.get(i).getRating()){
                result.clear();
                result.add(trainees.get(i));
                maxRating = trainees.get(i).getRating();
            }
        }
        return result;
    }

    public boolean  hasDuplicates(){
        Set<Trainee> checkSet = new HashSet<>(trainees);
        return checkSet.size() < trainees.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return name.equals(group.name) && room.equals(group.room) && trainees.equals(group.trainees);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, room, trainees);
    }
}
