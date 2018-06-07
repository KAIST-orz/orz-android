package kr.ac.kaist.orz.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Locale;

public class Assignment implements Serializable {
    private int id;
    private String name;
    private String description;
    private String due;
    private String courseName;
    private float averageTimeEstimate;

    public Assignment(int id, String name, String description, String courseName,
                      String due, float averageTimeEstimate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.due = due;
        this.courseName = courseName;
        this.averageTimeEstimate = averageTimeEstimate;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Calendar getDue() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.KOREA);
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(due));
            calendar.add(Calendar.HOUR, 9);
        }
        catch (ParseException e) {}
        return calendar;
    }

    public String getCourseName() {
        return courseName;
    }

    public float getAverageTimeEstimate() {
        return averageTimeEstimate;
    }

    public static final DueComparator DUE_COMPARATOR = new DueComparator();
    public static final CourseComparator COURSE_COMPARATOR = new CourseComparator();
    public static final EstimateComparator ESTIMATE_COMPARATOR = new EstimateComparator();

    private static class DueComparator implements Comparator<Assignment> {
        @Override
        public int compare(Assignment a1, Assignment a2) {
            return a1.getDue().compareTo(a2.getDue());
        }
    }

    private static class CourseComparator implements Comparator<Assignment> {
        @Override
        public int compare(Assignment a1, Assignment a2) {
            return a1.getCourseName().compareTo(a2.getCourseName());

        }
    }

    private static class EstimateComparator implements Comparator<Assignment> {
        @Override
        public int compare(Assignment a1, Assignment a2) {
            return (int) (a1.getAverageTimeEstimate() - a2.getAverageTimeEstimate());
        }
    }
}
