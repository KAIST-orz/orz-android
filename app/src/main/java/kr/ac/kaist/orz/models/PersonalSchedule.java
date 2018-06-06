package kr.ac.kaist.orz.models;

import java.util.Calendar;
import java.util.List;

public class PersonalSchedule extends Schedule {
    public String name;

    public PersonalSchedule(int id, int studentID, String start, String end, List<Integer> alarms, String name) {
        super(id, studentID, start, end, alarms);
        this.name = name;
    }
}
