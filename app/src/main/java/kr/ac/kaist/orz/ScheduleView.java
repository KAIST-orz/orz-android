package kr.ac.kaist.orz;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/*
 * This is a custom view that displays a schedule.
 */
abstract public class ScheduleView extends FrameLayout {
    // The ID of the schedule this view is showing.
    private int scheduleId;

    protected TextView titleText;
    protected TextView descText;

    public ScheduleView(Context context) {
        super(context);
        init(context);
    }

    public ScheduleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScheduleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    // Binds the two TextViews into the member fields.
    protected void bindTextViews(View view) {
        titleText = (TextView) view.findViewById(R.id.schedule_title);
        descText = (TextView) view.findViewById(R.id.schedule_description);
    }

    // Child classes should override this method. This method should properly
    // inflate the view for each view size.
    abstract protected void init(Context context);

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

    // Gets schedule id of the schedule represented by this view.
    public int getScheduleId() {
        return scheduleId;
    }

    // Set the schedule id of the schedule represented by this view.
    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }
}

class ScheduleViewLarge extends ScheduleView {

    ScheduleViewLarge(Context context) {
        super(context);
    }

    // Inflate the schedule_view_large.xml file.
    @Override
    protected void init(Context context) {
        View view = inflate(context, R.layout.schedule_view_large, this);
        bindTextViews(view);
    }
}

class ScheduleViewMedium extends ScheduleView {

    ScheduleViewMedium(Context context) {
        super(context);
    }

    // Inflate the schedule_view_medium.xml file.
    @Override
    protected void init(Context context) {
        View view = inflate(context, R.layout.schedule_view_medium, this);
        bindTextViews(view);
    }
}

class ScheduleViewSmall extends ScheduleView {

    ScheduleViewSmall(Context context) {
        super(context);
    }

    // Inflate the schedule_view_small.xml file.
    @Override
    protected void init(Context context) {
        View view = inflate(context, R.layout.schedule_view_small, this);
        bindTextViews(view);
    }
}

// This class does nothing. It is just a view with schedule id attached to it.
// Defined this class for the typing.
class ScheduleViewMinimal extends ScheduleView {

    ScheduleViewMinimal(Context context) {
        super(context);
    }

    // Do nothing. It is just a simple view.
    @Override
    protected void init(Context context) {

    }
}

