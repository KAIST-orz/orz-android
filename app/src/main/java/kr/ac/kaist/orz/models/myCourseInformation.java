package kr.ac.kaist.orz.models;

//myCourseInformation 자바 클래스는 값을 저장해줄 공간의 틀을 짜는 것

public class myCourseInformation {
    private String name;
    private String id;
    private String professor;

    public myCourseInformation(String input_name, String input_id, String input_professor){
        name = input_name;
        id = input_id;
        professor = input_professor;
    }

    public String getCourseName() {
        return name;
    }

    public String getCourseID() {
        return id;
    }

    public String getCourseLecturer() {
        return professor;
    }
}
