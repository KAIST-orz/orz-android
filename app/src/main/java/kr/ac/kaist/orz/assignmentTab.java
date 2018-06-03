package kr.ac.kaist.orz;

public class assignmentTab {
    private String assignmentCourse;
    private String assignmentName;

    public assignmentTab(String input_course, String input_name){
        assignmentCourse = input_course;
        assignmentName = input_name;
    }

    public String getCourse() {
        return assignmentCourse;
    }

    public String getName() {
        return assignmentName;
    }
}
