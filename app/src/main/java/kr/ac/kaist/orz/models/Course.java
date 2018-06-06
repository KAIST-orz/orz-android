package kr.ac.kaist.orz.models;

//Course 자바 클래스는 값을 저장해줄 공간의 틀을 짜는 것

import java.util.List;

public class Course {
    private int id;
    private int lecturerID;
    private List<Integer> studentIDs;
    private int schollID;
    private List<Integer> assignmentIDs;
    private String name;
    private String code;
    private String professor;

    public Course(String input_name, int input_id, String input_professor){
        name = input_name;
        id = input_id;
        professor = input_professor;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getProfessor() {
        return professor;
    }

    public int getID(){ return id; }
}
