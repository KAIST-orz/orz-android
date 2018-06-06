package kr.ac.kaist.orz.models;

import java.time.LocalDateTime;
import java.util.List;

public abstract class Schedule {
    private int id;
    private int studentID;
    private LocalDateTime start;
    private LocalDateTime end;
    private List<Integer> alarms;
}
