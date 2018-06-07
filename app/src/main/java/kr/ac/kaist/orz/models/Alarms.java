package kr.ac.kaist.orz.models;

public class Alarms {
    private int assignmentDueAlarm;
    private int personalScheduleAlarm;
    private int timeForAssignmentAlarm;

    public int getAssignmentDueAlarm() {
        return assignmentDueAlarm;
    }
    public int getPersonalScheduleAlarm() {
        return personalScheduleAlarm;
    }
    public int getTimeForAssignmentAlarm() {
        return timeForAssignmentAlarm;
    }

    public void setAssignmentDueAlarm(int assignmentDueAlarm) {
        this.assignmentDueAlarm = assignmentDueAlarm;
    }
    public void setPersonalScheduleAlarm(int personalScheduleAlarm) {
        this.personalScheduleAlarm = personalScheduleAlarm;
    }
    public void setTimeForAssignmentAlarm(int timeForAssignmentAlarm) {
        this.timeForAssignmentAlarm = timeForAssignmentAlarm;
    }
}
