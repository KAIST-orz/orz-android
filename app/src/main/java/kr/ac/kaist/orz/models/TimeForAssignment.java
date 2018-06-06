package kr.ac.kaist.orz.models;

import java.util.Calendar;
import java.util.List;

public class TimeForAssignment extends Schedule {
    int assignmentID;
    public String assignmentName;
    public String courseName;

    public TimeForAssignment(int id, int studentID, Calendar start, Calendar end, List<Integer> alarms,int assignmentID, String assignmentName, String courseName) {
        super(id, studentID, start, end, alarms);
        this.assignmentID = assignmentID;
        this.assignmentName = assignmentName;
        this.courseName = courseName;
    }
}
