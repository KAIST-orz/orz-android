package kr.ac.kaist.orz.models;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

public class StudentAssignment extends Assignment implements Serializable {
    private int timeEstimation;
    //private List<TimeForAssignment> timeForAssignments;
    private float timeForAssignmentsSum;
    private int significance;
    private List<Integer> alarms;

    public StudentAssignment(int id, String assignmentName, String description, String courseName,
                             String due, float averageTimeEstimate, int significance) {
        super(id, assignmentName, description, courseName, due, averageTimeEstimate);
        this.significance = significance;
    }

    public static final SignificanceComparator SIGNIFICANCE_COMPARATOR = new SignificanceComparator();

    public int getSignificance() {
        return significance;
    }

    public void setSignificance(int sig) { significance = sig; }

    private static class SignificanceComparator implements Comparator<StudentAssignment> {
        @Override
        public int compare(StudentAssignment a1, StudentAssignment a2) {
            return a1.getSignificance() - a2.getSignificance();
        }
    }

    public int getTimeEstimation() { return timeEstimation; }

    public void setTimeEstimation(int t) { timeEstimation = t; }

    public List<Integer> getAlarms() { return alarms; }

    public void addAlarm(int alarm) { alarms.add(alarm); }

    public void removeAlarm(int index) { alarms.remove(index); }
}
