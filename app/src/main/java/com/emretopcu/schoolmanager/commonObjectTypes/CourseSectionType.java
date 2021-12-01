package com.emretopcu.schoolmanager.commonObjectTypes;

import java.util.ArrayList;

public class CourseSectionType {
    private String lecturerName;
    private ArrayList<PersonType> students = new ArrayList<>();
    private ArrayList<String> hourDays = new ArrayList<>();
    private ArrayList<String> startHours = new ArrayList<>();
    private ArrayList<String> endHours = new ArrayList<>();

    public String getLecturerName() {
        return lecturerName;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
    }

    public ArrayList<PersonType> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<PersonType> students) {
        this.students = students;
    }

    public ArrayList<String> getHourDays() {
        return hourDays;
    }

    public void setHourDays(ArrayList<String> hourDays) {
        this.hourDays = hourDays;
    }

    public ArrayList<String> getStartHours() {
        return startHours;
    }

    public void setStartHours(ArrayList<String> startHours) {
        this.startHours = startHours;
    }

    public ArrayList<String> getEndHours() {
        return endHours;
    }

    public void setEndHours(ArrayList<String> endHours) {
        this.endHours = endHours;
    }
}
