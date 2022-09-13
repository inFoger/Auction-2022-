package net.thumbtack.school.database.model;

import java.util.*;

public class School {
    private int id;
    private String name;
    private int year;
    private List<Group> groups;

    public School(){
    }

    public School(int id, String name, int year, List<Group> groups) {
        setId(id);
        setName(name);
        setYear(year);
        setGroups(groups);
    }

    public School(int id, String name, int year) {
        this(id, name, year, new ArrayList<>());
    }

    public School(String name, int year) {
        this(0, name, year, new ArrayList<>());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear(){
        return year;
    }

    public void setYear(int year){
        this.year = year;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public void  addGroup(Group group) {
        groups.add(group);
    }

    public void  removeGroup(Group group) {
        groups.remove(group);
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
        if (!(o instanceof School)) return false;
        School school = (School) o;
        school.getGroups().sort(Comparator.comparing(Group::getName));
        getGroups().sort(Comparator.comparing(Group::getName));
        return getId() == school.getId() && getYear() == school.getYear() && Objects.equals(getName(), school.getName()) && Objects.equals(getGroups(), school.getGroups());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getYear(), getGroups());
    }
}
