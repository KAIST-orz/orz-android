package kr.ac.kaist.orz.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public abstract class Schedule implements Serializable {
    public int id;
    private int studentID;
    private String start;
    private String end;
    private List<Integer> alarms;

    public Schedule(int id, int studentID, String start, String end, List<Integer> alarms) {
        this.id = id;
        this.studentID = studentID;
        this.start = start;
        this.end = end;
        this.alarms = alarms;
    }

    public Calendar getStart() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.KOREA);
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(start));
        }
        catch (ParseException e) {}
        return calendar;

    }

    public Calendar getEnd() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.KOREA);
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(end));
        }
        catch (ParseException e) {}
        return calendar;

    }
}
