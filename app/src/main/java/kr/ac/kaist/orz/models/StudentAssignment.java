package kr.ac.kaist.orz.models;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

public class StudentAssignment extends Assignment implements Serializable {
    private int timeEstimation;
    //private List<TimeForAssignment> timeForAssignments;
    private float timeForAssignmentsSum;
    private int significance;

    public StudentAssignment(int id, String assignmentName, String description, String courseName,
                             String due, float averageTimeEstimate, int significance) {
        super(id, assignmentName, description, courseName, due, averageTimeEstimate);
        this.significance = significance;
    }

    public static final SignificanceComparator SIGNIFICANCE_COMPARATOR = new SignificanceComparator();

    public int getSignificance() {
        return significance;
    }

    private static class SignificanceComparator implements Comparator<StudentAssignment> {
        @Override
        public int compare(StudentAssignment a1, StudentAssignment a2) {
            return a1.getSignificance() - a2.getSignificance();
        }
    }
}
