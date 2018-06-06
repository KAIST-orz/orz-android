package kr.ac.kaist.orz.models;

import java.util.List;

public class StudentAssignment extends Assignment {
    private int timeEstimation;
    private List<TimeForAssignment> timeForAssignments;
    private float timeForAssignmentsSum;
    private int significance;
}
