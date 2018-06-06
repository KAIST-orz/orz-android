package kr.ac.kaist.orz.models;

public class Assignment {
    private int id;
    private int courseID;
    private String courseName;
    private String name;
    private String due;
    private float averageTimeEstimation;
    private String Description;

    public Assignment() {
    }

    public Assignment(String input_course, String input_name, int input_ID){
        courseName = input_course;
        name = input_name;
        id = input_ID;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getName() {
        return name;
    }

    public int getID(){return id;}
}
