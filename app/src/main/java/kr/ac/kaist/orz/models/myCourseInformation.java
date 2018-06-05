package kr.ac.kaist.orz.models;

//myCourseInformation 자바 클래스는 값을 저장해줄 공간의 틀을 짜는 것

public class myCourseInformation {
    private String courseName;
    private String courseID;
    private String courseLecturer;

    public myCourseInformation(String input_name, String input_id, String input_lecturer){
        courseName = input_name;
        courseID = input_id;
        courseLecturer = input_lecturer;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseID() {
        return courseID;
    }

    public String getCourseLecturer() {
        return courseLecturer;
    }
}
