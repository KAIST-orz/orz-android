package kr.ac.kaist.orz.models;

import java.util.Calendar;
import java.util.List;

public abstract class Schedule {
    public int id;
    private int studentID;
    public Calendar start;
    public Calendar end;
    private List<Integer> alarms;

    public Schedule(int id, int studentID, Calendar start, Calendar end, List<Integer> alarms) {
        this.id = id;
        this.studentID = studentID;
        this.start = start;
        this.end = end;
        this.alarms = alarms;
    }
}
