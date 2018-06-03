package kr.ac.kaist.orz;

public class assignmentTab {
    private String assignmentCourse;
    private String assignmentName;
    private int assignmentID;
    
    public assignmentTab(String input_course, String input_name, int input_ID){
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
