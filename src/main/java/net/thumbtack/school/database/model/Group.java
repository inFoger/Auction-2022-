package net.thumbtack.school.database.model;

import java.util.*;

public class Group {
    private int id;
    private String name;
    private String room;
    private List<Trainee> trainees;
    private List<Subject> subjects;

    public Group() {
    }

    public Group(int id, String name, String room, List<Trainee> trainees, List<Subject> subjects) {
        setId(id);
        setName(name);
        setRoom(room);
        setSubjects(subjects);
        setTrainees(trainees);
    }

    public Group(int id, String name, String room) {
        this(id, name, room, new ArrayList<>(), new ArrayList<>());
    }

    public Group(String name, String room) {
        this(0, name, room, new ArrayList<>(), new ArrayList<>());
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom(){
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public List<Trainee> getTrainees(){
        return trainees;
    }

    public void  addTrainee(Trainee trainee){
        trainees.add(trainee);
    }

    public void  removeTrainee(Trainee trainee) {
        trainees.remove(trainee);
    }

    public void  removeTrainee(int index) {
        trainees.remove(index);
    }

    public void addSubject(Subject subject) {
        subjects.add(subject);
    }

    public void removeSubject(Subject subject) {
        subjects.remove(subject);
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
//        int max = Collections.max(trainees, Comparator.comparing(Trainee::getRating)).getRating();
//        List<Trainee> result = new ArrayList<>();
//        for(Trainee elem: trainees){
//            if(elem.getRating() == max){
//                result.add(elem);
//            }
//        }
//        return result;
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
//        for (int i = 1; i < trainees.size(); i++) {
//            if(trainees.get(i).getRating() < trainees.get(0).getRating()){
//                break;
//            }
//            result.add(trainees.get(i));
//        }
        return result;
    }

    public boolean  hasDuplicates(){
        Set<Trainee> checkSet = new HashSet<>(trainees);
        return checkSet.size() < trainees.size();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTrainees(List<Trainee> trainees) {
        this.trainees = trainees;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Group)) return false;
        Group group = (Group) o;
        // использовать геттеры!!!
        group.getSubjects().sort(Comparator.comparing(Subject::getName));
        group.getTrainees().sort(Comparator.comparing(Trainee::getFullName));
        getSubjects().sort(Comparator.comparing(Subject::getName));
        getTrainees().sort(Comparator.comparing(Trainee::getFullName));
        return getId() == group.getId() && Objects.equals(getName(), group.getName()) && Objects.equals(getRoom(), group.getRoom()) && Objects.equals(getTrainees(), group.getTrainees()) && Objects.equals(getSubjects(), group.getSubjects());
    }


    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getRoom(), getTrainees(), getSubjects());
    }
}
