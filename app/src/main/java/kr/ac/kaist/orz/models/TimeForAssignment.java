package kr.ac.kaist.orz.models;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

public class TimeForAssignment extends Schedule implements Serializable {
    int assignmentID;
    int courseID;
    public String assignmentName;
    public String courseName;

    public TimeForAssignment(int id, int studentID, String start, String end, List<Integer> alarms,int assignmentID, String assignmentName, String courseName) {
        super(id, studentID, start, end, alarms);
        this.assignmentID = assignmentID;
        this.assignmentName = assignmentName;
        this.courseName = courseName;
    }

    public String getAssignmentName() { return assignmentName; }
    public String getCourseName() { return courseName; }
    public int getAssignmentID() { return assignmentID; }
    public int getCourseID() { return courseID; }
}
