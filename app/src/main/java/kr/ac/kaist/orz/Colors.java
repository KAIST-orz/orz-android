package kr.ac.kaist.orz;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;

import kr.ac.kaist.orz.models.Schedule;
import kr.ac.kaist.orz.models.TimeForAssignment;

public class Colors {
    public static int getCourseColor(Context context, int courseID) {
        Resources resources = context.getResources();

        int numberOfColors = resources.getInteger(R.integer.number_of_course_colors);
        String colorName = resources.getString(R.string.course_color_name_prefix);
        int colorHashValue = courseID % numberOfColors;

        colorName += colorHashValue;

        int colorIdentifier = resources.getIdentifier(colorName, "color", "kr.ac.kaist.orz");
        return ContextCompat.getColor(context, colorIdentifier);
    }

    public static int getScheduleColor(Context context, Schedule schedule) {
        if (schedule instanceof TimeForAssignment) {
            return getCourseColor(context, ((TimeForAssignment) schedule).getCourseID());
        } else {
            return getPersonalScheduleColor(context);
        }
    }

    public static int getPersonalScheduleColor(Context context) {
        Resources resources = context.getResources();
        String colorName = resources.getString(R.string.personal_schedule_color_name);
        int colorIdentifier = resources.getIdentifier(colorName, "color", "kr.ac.kaist.orz");
        return ContextCompat.getColor(context, colorIdentifier);
    }
}
