package net.thumbtack.school.sixteenthExercise.ninth;

import net.thumbtack.school.database.model.TrainingErrorCode;
import net.thumbtack.school.database.model.TrainingException;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

//9. Изменить классы Group и School из Задания 10 Заочной Школы,
// обеспечив многопоточный доступ. В тех случаях, когда это возможно,
// использовать конкурентные коллекции.
public class School {
    private String name;
    private AtomicInteger year;
    private Set<Group> groups;

    public School(String name, int year) throws TrainingException {
        setName(name);
        setYear(year);
        groups = Collections.synchronizedSortedSet(new TreeSet<>(Comparator.comparing(Group::getName)));
    }

    public String getName(){
        return name;
    }

    public void setName(String name) throws TrainingException {
        if(name == null || name.equals("")){
            throw new TrainingException(TrainingErrorCode.SCHOOL_WRONG_NAME);
        }
        this.name = name;
    }

    public int getYear(){
        return year.get();
    }

    public void setYear(int year){
        this.year.set(year);
    }

    public Set<Group> getGroups(){
        return groups;
    }

    public void  addGroup(Group group) throws TrainingException {
        if(containsGroup(group)){
            throw new TrainingException(TrainingErrorCode.DUPLICATE_GROUP_NAME);
        }
        groups.add(group);
    }

    public void  removeGroup(Group group) throws TrainingException {
        if(!groups.remove(group)){
            throw new TrainingException(TrainingErrorCode.GROUP_NOT_FOUND);
        }
    }

    public void  removeGroup(String name) throws TrainingException {
        for(Group elem: groups){
            if(elem.getName().equals(name)){
                groups.remove(elem);
                return;
            }
        }
        throw new TrainingException(TrainingErrorCode.GROUP_NOT_FOUND);
    }

    public boolean  containsGroup(Group group){
        return groups.contains(group);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        School school = (School) o;
        return year == school.year && name.equals(school.name) && groups.equals(school.groups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, year, groups);
    }
}
