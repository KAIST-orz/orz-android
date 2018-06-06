package kr.ac.kaist.orz.models;

public class Assignment {
    private String assignmentCourse;
    private String assignmentName;
    private int assignmentID;

    public Assignment(String input_course, String input_name, int input_ID){
        assignmentCourse = input_course;
        assignmentName = input_name;
        assignmentID = input_ID;
    }

    public String getCourse() {
        return assignmentCourse;
    }

    public String getName() {
        return assignmentName;
    }

    public int getID(){return assignmentID;}
}
