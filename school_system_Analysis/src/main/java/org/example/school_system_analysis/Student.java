package org.example.school_system_analysis;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "students")
public class Student {
    @Id
    private String id;
    private String StudentID;
    private String Name;
    private int Attendance;
    private int Homework;
    private int Midterm;
    private int Final;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String studentID) {
        this.StudentID = studentID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public int getAttendance() {
        return Attendance;
    }

    public void setAttendance(int attendance) {
        this.Attendance = attendance;
    }

    public int getHomework() {
        return Homework;
    }

    public void setHomework(int homework) {
        this.Homework = homework;
    }

    public int getMidterm() {
        return Midterm;
    }

    public void setMidterm(int midterm) {
        this.Midterm = midterm;
    }

    public int getFinal() {
        return Final;
    }

    public void setFinal(int aFinal) {
        this.Final = aFinal;
    }

    public double calculateFinalScore(){
        return 0.3* Attendance + 0.2* Homework + 0.1* Midterm + 0.4* Final;
    }
    public boolean isTopStudent(){
        return calculateFinalScore() >= 85 && Attendance >=90 && Homework >=85;
    }


}
