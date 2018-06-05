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
abstract public class ScheduleView extends FrameLayout {
    private LinearLayout layout;
    private TextView titleText;
    private TextView descText;

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
        titleText = (TextView) findViewById(R.id.schedule_title);
        descText = (TextView) findViewById(R.id.schedule_description);
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

    @Override
    public void setMinimumWidth(int minWidth) {
        super.setMinimumWidth(minWidth);
        layout.setMinimumWidth(minWidth);
    }

    @Override
    public void setMinimumHeight(int minHeight) {
        super.setMinimumHeight(minHeight);
        layout.setMinimumHeight(minHeight);
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

