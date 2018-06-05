package kr.ac.kaist.orz;

//myCourseInformation 자바 클래스는 값을 저장해줄 공간의 틀을 짜는 것

public class myCourseInformation {
    private String courseName;
    private String courseCode;
    private String courseLecturer;
    private int courseID;

    public myCourseInformation(String input_name, String input_code, String input_lecturer, int input_id){
        courseName = input_name;
        courseCode = input_code;
        courseLecturer = input_lecturer;
        courseID = input_id;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseLecturer() {
        return courseLecturer;
    }

    public int getCourseID() {
        return courseID;
    }
}

