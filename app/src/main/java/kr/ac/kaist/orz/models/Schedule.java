package kr.ac.kaist.orz.models;

import java.util.List;

public abstract class Schedule {
    private int id;
    private int studentID;
    private String start;
    private String end;
    private List<Integer> alarms;
}
