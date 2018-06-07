package kr.ac.kaist.orz;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
 * This is a custom view that displays a schedule.
 */
public class ScheduleView extends FrameLayout {
    private LinearLayout layout;
    private TextView titleText;
    private TextView descText;

    public ScheduleView(Context context) {
        super(context);
        init(context);
    }

    // Child classes should override this method. This method should properly
    // inflate the view for each view size.
    protected void init(Context context) {
        layout = (LinearLayout) findViewById(R.id.schedule_linear_layout);
        titleText = (TextView) findViewById(R.id.schedule_title);
        descText = (TextView) findViewById(R.id.schedule_description);
    }

    // Set the title of the schedule view.
    public void setTitle(String title) {
        if (titleText != null) {
            titleText.setText(title);
        }
    }

    // Set the description (assignment name) of the schedule view.
    public void setDescription(String description) {
        if (descText != null) {
            descText.setText(description);
        }
    }

    public void setWidth(int width) {
        setMinimumWidth(width);
        if (layout != null) {
            layout.setMinimumWidth(width);
        }
    }

    public void setHeight(int height) {
        if (height < 32)
            height = 32;
        setMinimumHeight(height);
        if (layout != null) {
            layout.setMinimumHeight(height);
        }
    }

    public void setColor(int color) {
        setBackgroundColor(color);
    }
}

class ScheduleViewLarge extends ScheduleView {

    ScheduleViewLarge(Context context) {
        super(context);
    }

    // Inflate the schedule_view_large.xml file.
    @Override
    protected void init(Context context) {
        inflate(context, R.layout.schedule_view_large, this);
        super.init(context);
    }
}

class ScheduleViewMedium extends ScheduleView {

    ScheduleViewMedium(Context context) {
        super(context);
    }

    // Inflate the schedule_view_medium.xml file.
    @Override
    protected void init(Context context) {
        inflate(context, R.layout.schedule_view_medium, this);
        super.init(context);
    }
}

class ScheduleViewSmall extends ScheduleView {

    ScheduleViewSmall(Context context) {
        super(context);
    }

    // Inflate the schedule_view_small.xml file.
    @Override
    protected void init(Context context) {
        inflate(context, R.layout.schedule_view_small, this);
        super.init(context);
    }
}
