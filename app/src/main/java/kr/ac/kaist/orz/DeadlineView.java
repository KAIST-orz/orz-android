package kr.ac.kaist.orz;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/*
 * A class that displays the deadline of an assignment.
 */
public class DeadlineView extends FrameLayout {
    private ImageView arrowHeadImage;
    private TextView courseNameText;

    public DeadlineView(Context context) {
        super(context);
        init(context);
    }

    // Inflate the deadline_view.xml
    private void init(Context context) {
        inflate(context, R.layout.deadline_view, this);
        courseNameText = (TextView) findViewById(R.id.course_name);
        arrowHeadImage = (ImageView) findViewById(R.id.arrow_head);
    }

    public void setCourseName(String courseName) {
        courseNameText.setText(courseName);
    }

    public void setColor(int color) {
        arrowHeadImage.getDrawable().setTint(color);
        courseNameText.setBackgroundColor(color);
    }
}
