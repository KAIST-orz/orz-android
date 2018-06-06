package kr.ac.kaist.orz;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.text.DateFormat;

import kr.ac.kaist.orz.models.PersonalSchedule;
import kr.ac.kaist.orz.models.Schedule;
import kr.ac.kaist.orz.models.TimeForAssignment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalendarTabFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CalendarTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarTabFragment extends Fragment
                                implements DatePickerDialog.OnDateSetListener {
    // Views for this fragment
    private ImageButton toPreviousDay;
    private ImageButton toNextDay;
    private Button pickDate;

    // Keep the date information which the user has chosen
    private Calendar current;

    // The layout onto which the schedule views are placed.
    private ConstraintLayout scheduleLayout;

    // TODO: Tracks the number of schedules being displayed currently.
    // TODO: Might keep the list of schedules later on, thus may override this.
    private int numOfViews;

//    private OnFragmentInteractionListener mListener;

    public CalendarTabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CalendarTabFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarTabFragment newInstance() {
        CalendarTabFragment fragment = new CalendarTabFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // When this fragment is first created, instantiate a new Calendar object.
        current = Calendar.getInstance();
        numOfViews = -1;     // Initialize to -1 to indicate the initiation.
    }

    // Formats the date held by current to "MMM DD, YYYY" format.
    public String formatDateOfCalendar() {
        DateFormat formatter = DateFormat.getDateInstance(DateFormat.MEDIUM);
        return formatter.format(current.getTime());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar_tab, container, false);

        // Initialize pickDate, toPreviousDay, toNextDay buttons.
        pickDate = (Button) view.findViewById(R.id.pick_date);
        toPreviousDay = (ImageButton) view.findViewById(R.id.to_previous_day);
        toNextDay = (ImageButton) view.findViewById(R.id.to_next_day);

        // The layout to draw schedules onto.
        scheduleLayout = (ConstraintLayout) view.findViewById(R.id.schedule_layout);

        // Set onClick listeners as the methods in this class.
        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        toPreviousDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current.add(Calendar.DATE, -1);
                pickDate.setText(formatDateOfCalendar());

                // TODO: Communicate with the server and draw schedules on the layout.
                displayCurrentDate(getPersonalSchedules(), getTimeForAssignments(), getAssignments());
            }
        });

        toNextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current.add(Calendar.DATE, 1);
                pickDate.setText(formatDateOfCalendar());

                // TODO: Communicate with the server and draw schedules on the layout.
                displayCurrentDate(getPersonalSchedules(), getTimeForAssignments(), getAssignments());
            }
        });

        // Let the button to show picked date.
        pickDate.setText(formatDateOfCalendar());

        // Display the schedules.
        displayCurrentDate(getPersonalSchedules(), getTimeForAssignments(), getAssignments());

        // Inflate the layout for this fragment
        return view;
    }

    /*
     * When pick_date button is pressed.
     */
    public void showDatePickerDialog() {
        FragmentActivity parentActivity = getActivity();
        if (parentActivity == null) {
            return;
        }

        DatePickerFragment dialog = new DatePickerFragment();
        dialog.setOnDateSetListener(this);  // Setting this fragment as OnDateSetListener

        // Putting currently picked (being shown) date as arguments.
        Bundle args = new Bundle();
        args.putInt("year", current.get(Calendar.YEAR));
        args.putInt("month", current.get(Calendar.MONTH));
        args.putInt("day", current.get(Calendar.DAY_OF_MONTH));
        dialog.setArguments(args);

        // Show to the user. fragment manager is OrzMainActivity's manager.
        dialog.show(parentActivity.getFragmentManager(), "datePicker");
    }

    // Listener for the date picker's DateSet event.
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Change where the current is pointing.
        current.set(year, month, day);
        pickDate.setText(formatDateOfCalendar());

        // Get schedules (possibly from the server) and display onto screen.
        displayCurrentDate(getPersonalSchedules(), getTimeForAssignments(), getAssignments());
    }

    // TODO: Communicates with the server to acquire the schedules for this day.
    public PersonalSchedule[] getPersonalSchedules() {
        return createDummyPersonalSchedules(6);
    }

    public TimeForAssignment[] getTimeForAssignments() {
        return createDummyTimeForAssignments(6);
    }

    public Assignment[] getAssignments() {
        return createDummyAssignments(6);
    }

    // Displays everything (schedules and assignment dues) of the current day.
    private void displayCurrentDate(PersonalSchedule[] personalSchedules, TimeForAssignment[] timeForAssignments, Assignment[] assignments) {
        if (numOfViews > 0) {
            clearLayout();
        } else {
            numOfViews = 0;
        }

        displayPersonalSchedules(personalSchedules);
        displayTimeForAssignments(timeForAssignments);
        displayDeadlines(assignments);
    }

    // Remove anything (schedule, assignment) displayed on the scheduleLayout.
    private void clearLayout() {
        if (numOfViews > 0) {
            scheduleLayout.removeViews(scheduleLayout.getChildCount() - numOfViews,
                    numOfViews);
        }
        numOfViews = 0;
    }

    // Displays the schedules of the date the user has picked.
    private void displayPersonalSchedules(PersonalSchedule[] schedules) {
        for (PersonalSchedule schedule : schedules) {
            // TODO: these schedules should be first filtered before being passed to this method.
            if ((schedule.end.get(Calendar.YEAR) < current.get(Calendar.YEAR)
                    || schedule.end.get(Calendar.DAY_OF_YEAR) < current.get(Calendar.DAY_OF_YEAR)
                    || schedule.start.get(Calendar.YEAR) > current.get(Calendar.YEAR)
                    || schedule.start.get(Calendar.DAY_OF_YEAR) > current.get(Calendar.DAY_OF_YEAR))
                    || (schedule.end.get(Calendar.DAY_OF_YEAR) == current.get(Calendar.DAY_OF_YEAR)
                        && schedule.end.get(Calendar.HOUR_OF_DAY) == 0
                        && schedule.end.get(Calendar.MINUTE) == 0)) {
                continue;
            }
            // Display only when the view is for today.
            createScheduleView(schedule);
            numOfViews++;    // Count only the displayed ones.
        }
    }

    // Displays the schedules of the date the user has picked.
    private void displayTimeForAssignments(TimeForAssignment[] schedules) {
        for (TimeForAssignment schedule : schedules) {
            // TODO: these schedules should be first filtered before being passed to this method.
            if ((schedule.end.get(Calendar.YEAR) < current.get(Calendar.YEAR)
                    || schedule.end.get(Calendar.DAY_OF_YEAR) < current.get(Calendar.DAY_OF_YEAR)
                    || schedule.start.get(Calendar.YEAR) > current.get(Calendar.YEAR)
                    || schedule.start.get(Calendar.DAY_OF_YEAR) > current.get(Calendar.DAY_OF_YEAR))
                    || (schedule.end.get(Calendar.DAY_OF_YEAR) == current.get(Calendar.DAY_OF_YEAR)
                    && schedule.end.get(Calendar.HOUR_OF_DAY) == 0
                    && schedule.end.get(Calendar.MINUTE) == 0)) {
                continue;
            }
            // Display only when the view is for today.
            createScheduleView(schedule);
            numOfViews++;    // Count only the displayed ones.
        }
    }

    /*
     * Creates a view for a schedule and adds it to the layout on the screen.
     * Assumptions:
     * 1. Schedules are given in units of 5 minutes.
     * 2. Layout does not change: I used some hard-coded values for the height of the view.
     */
    private void createScheduleView(final Schedule schedule) {
        // Calculate the height of the view and determine which layout resource to use.
        ScheduleView scheduleView;
        int height = calculateScheduleViewHeight(schedule.start, schedule.end);
        String title = "title";
        String description = "description";
        if (PersonalSchedule.class.isInstance(schedule)) {
            title = ((PersonalSchedule)schedule).name;
            description = null;
        }
        else if (TimeForAssignment.class.isInstance(schedule)) {
            title = ((TimeForAssignment)schedule).courseName;
            description = ((TimeForAssignment)schedule).assignmentName;
        }

        // There are four resources we can make use of according to the height.
        if (description != null
                && height >= getResources().getInteger(R.integer.schedule_view_large_height)) {
            // With description (assignment schedule), and it is large enough.
            scheduleView = new ScheduleViewLarge(getContext());
        } else if (height >= getResources().getInteger(R.integer.schedule_view_medium_height)) {
            scheduleView = new ScheduleViewMedium(getContext());
        } else if (height >= getResources().getInteger(R.integer.schedule_view_small_height)) {
            scheduleView = new ScheduleViewSmall(getContext());
        } else {
            scheduleView = new ScheduleView(getContext());
        }

        // Fill in the title and description of the schedule.
        scheduleView.setTitle(title);
        scheduleView.setDescription(description);

        // TODO: Fill appropriate background and font color.
        scheduleView.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));

        // Assign a unique ID to setup constraints of the view.
        int viewId = View.generateViewId();
        scheduleView.setId(viewId);

        // Add view to the layout (required to setup constraints).
        scheduleLayout.addView(scheduleView);

        // Get the width of the screen and set it as minimum width (the view then expands as desired).
        WindowManager wm = getActivity().getWindowManager();
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int windowWidth = size.x;

        // Set width and height of the scheduleView.
        scheduleView.setHeight((int) dpToPx(height));
        scheduleView.setWidth(windowWidth);

        // Constrain the top and the left side of the view.
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(scheduleLayout);

        // Constrain the left to the delimiter in the layout.
        int leftMarginInPx = (int) getResources().getDimension(R.dimen.schedule_view_left_margin);
        constraintSet.connect(scheduleView.getId(), ConstraintSet.START,
                            R.id.delimiter, ConstraintSet.END, leftMarginInPx);

        // Constrain the top of the view to appropriate place.
        if (schedule.start.get(Calendar.YEAR) < current.get(Calendar.YEAR)
                || schedule.start.get(Calendar.DAY_OF_YEAR) < current.get(Calendar.DAY_OF_YEAR)) {
            // If the schedule started before the day, it should seem like continuing from the day before.
            constraintSet.connect(scheduleView.getId(), ConstraintSet.TOP,
                                scheduleLayout.getId(), ConstraintSet.TOP, 0);
        } else {
            // Else connect to the appropriate hour line.
            String line_num = "line_" + schedule.start.get(Calendar.HOUR_OF_DAY);   // The identifier of the line.
            float marginFromTimeLineInPx    // The top margin from the hour line.
                    = dpToPx(4 * (schedule.start.get(Calendar.MINUTE) / 5) + 1);

            constraintSet.connect(scheduleView.getId(), ConstraintSet.TOP,
                                getResources().getIdentifier(line_num, "id", "kr.ac.kaist.orz"),
                                ConstraintSet.BOTTOM, (int) marginFromTimeLineInPx);
        }

        // Apply the constraints.
        constraintSet.applyTo(scheduleLayout);

        // Add onClickListener to the view to change to the appropriate activity.
        scheduleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ScheduleDetailsActivity.class);
                intent.putExtra("schedule_id", schedule.id);    // Pass schedule ID.
                startActivity(intent);
            }
        });

    }

    // Calculates the height of a view for a schedule in dp units.
    private int calculateScheduleViewHeight(Calendar start, Calendar end) {
        int height = 0; // The height of the view.

        int startHour;      // The hour the schedule starts in the current day.
        int startMinute;    // The minute in an hour the schedule starts in the current day.
        int endHour;        // The hour the schedule starts in the current day.
        int endMinute;      // The minute in an hour the schedule starts in the current day.

        // TODO: Handle the case when year is different.
        // Adding top margin in case the schedule starts before the current day.
        if (start.get(Calendar.DAY_OF_YEAR) < current.get(Calendar.DAY_OF_YEAR)) {
            height += getResources().getInteger(R.integer.timeline_margin_top) + 2;
            startHour = 0;
            startMinute = 0;
        } else {
            startHour = start.get(Calendar.HOUR_OF_DAY);
            startMinute = start.get(Calendar.MINUTE);
        }

        // Adding bottom margin in case the schedule
        if (end.get(Calendar.DAY_OF_YEAR) > current.get(Calendar.DAY_OF_YEAR)) {
            if (end.get(Calendar.HOUR_OF_DAY) != 0 || end.get(Calendar.MINUTE) != 0) {
                height += getResources().getInteger(R.integer.timeline_margin_bottom) + 2;
            }
            endHour = 24;
            endMinute = 0;
        } else {
            endHour = end.get(Calendar.HOUR_OF_DAY);
            endMinute = end.get(Calendar.MINUTE);
        }

        // Schedules are in 5 minute units, thus one slot corresponds to 5 minutes.
        // 5 minutes in the current tab corresponds to @integer/timeline_5min dp.
        int slotSize = getResources().getInteger(R.integer.timeline_5min);
        int numOfSlots = ((endHour * 60 + endMinute) - (startHour * 60 + startMinute)) / 5;

        height += slotSize * numOfSlots;

        // Add the gaps by delimiters.
        // Delimiters placed every 5 minutes. Each delimiter is 1 dp.
        height += numOfSlots - 1;

        // Delimiters placed every hour. It has 2 more dps per one.
        // If a schedule ends at O'clock, it does not contain the delimiter marking the last hour.
        if (endMinute == 0) {
            endHour--;
        }
        height += 2 * (endHour - startHour);

        return height;
    }



    // Displays the deadlines of assignments whose deadlines are in the middle of today.
    public void displayDeadlines(Assignment[] assignments) {
        // TODO: assignments should be sorted by deadline and significance. If deadlines are the same,
        // TODO: Should display higher deadline assignment on top.
        // TODO: When displaying assignments of same deadlines, should deal with them exclusively.
        for (Assignment assignment : assignments) {
            // TODO: these deadlines should be filtered before being passed to this method.
            if ((assignment.deadline.get(Calendar.YEAR) < current.get(Calendar.YEAR))
                    || assignment.deadline.get(Calendar.DAY_OF_YEAR) < current.get(Calendar.DAY_OF_YEAR)
                    || assignment.deadline.get(Calendar.YEAR) > current.get(Calendar.YEAR)
                    || assignment.deadline.get(Calendar.DAY_OF_YEAR) > current.get(Calendar.DAY_OF_YEAR)) {
                if (!(assignment.deadline.get(Calendar.DAY_OF_YEAR) == current.get(Calendar.DAY_OF_YEAR) + 1
                        && assignment.deadline.get(Calendar.HOUR_OF_DAY) == 0
                        && assignment.deadline.get(Calendar.MINUTE) == 0)) {
                    continue;
                }
            } else if (assignment.deadline.get(Calendar.YEAR) == current.get(Calendar.YEAR)
                        && assignment.deadline.get(Calendar.DAY_OF_YEAR) == current.get(Calendar.DAY_OF_YEAR)
                        && assignment.deadline.get(Calendar.HOUR_OF_DAY) == 0
                        && assignment.deadline.get(Calendar.MINUTE) == 0) {
                continue;
            }
            // Display only when the deadline is for today.
            createDeadlineView(assignment);
            numOfViews++;
        }
    }

    public void createDeadlineView(final Assignment assignment) {
        DeadlineView deadlineView = new DeadlineView(getContext());
        int topMargin = calculateDeadlineViewTopMargin(assignment.deadline);

        deadlineView.setCourseName(assignment.courseName);

        // TODO: Fill appropriate color.
        deadlineView.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));

        // Assign a unique ID to setup constraints.
        int viewId = View.generateViewId();
        deadlineView.setId(viewId);

        // Add view to the scheduleLayout.
        scheduleLayout.addView(deadlineView);

        // Constrain the top and right side of the view.
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(scheduleLayout);

        // Constrain the top of the view to the hour lines.
        int resourceId;     // The resource ID to which the view is constrained.
        int toWhere;        // The ConstraintSet.*** value of the resource to connect the view to.
        int topMarginInPx = (int) dpToPx((float) calculateDeadlineViewTopMargin(assignment.deadline));

        // Determine which hour line is directly above the view.
        if (assignment.deadline.get(Calendar.MINUTE) < 20) {
            if (assignment.deadline.get(Calendar.HOUR_OF_DAY) == 0
                    && assignment.deadline.get(Calendar.DAY_OF_YEAR) == current.get(Calendar.DAY_OF_YEAR)) {
                resourceId = scheduleLayout.getId();
                toWhere = ConstraintSet.TOP;
            } else {
                String line_num = "line_";
                if (assignment.deadline.get(Calendar.HOUR_OF_DAY) == 0) {
                    line_num += "23";
                } else {
                    line_num += assignment.deadline.get(Calendar.HOUR_OF_DAY) - 1;
                }
                resourceId = getResources().getIdentifier(line_num, "id", "kr.ac.kaist.orz");
                toWhere = ConstraintSet.BOTTOM;
            }
        }
        else {
            String line_num = "line_" + assignment.deadline.get(Calendar.HOUR_OF_DAY);
            resourceId = getResources().getIdentifier(line_num, "id", "kr.ac.kaist.orz");
            toWhere = ConstraintSet.BOTTOM;
        }

        // Constrain the view to the appropriate resource.
        constraintSet.connect(deadlineView.getId(), ConstraintSet.TOP, resourceId, toWhere, topMarginInPx);

        // Constraint the right of the view to the parent.
        constraintSet.connect(deadlineView.getId(), ConstraintSet.END,
                            scheduleLayout.getId(), ConstraintSet.END);

        constraintSet.applyTo(scheduleLayout);

        // Set onClickListener to the view.
        deadlineView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AssignmentDetailsActivity.class);
                intent.putExtra("assignment_id", assignment.assignmentId);
                startActivity(intent);
            }
        });
    }

    // Calculate the top margin (in dp units) of a deadline view from the hour line directly above it.
    private int calculateDeadlineViewTopMargin(Calendar deadline) {
        int minute = deadline.get(Calendar.MINUTE);
        int margin;

        int slotSize = getResources().getInteger(R.integer.timeline_5min) + 1;
        int numOfSlots;

        if (minute >= 20) {
            numOfSlots = minute / 5;
            margin = slotSize * numOfSlots - getResources().getInteger(R.integer.deadline_view_arrow_position);
        } else {
            if (deadline.get(Calendar.HOUR_OF_DAY) == 0
                    && deadline.get(Calendar.DAY_OF_YEAR) == current.get(Calendar.DAY_OF_YEAR)) {
                numOfSlots = minute / 5;
                margin = 23;
            } else {
                numOfSlots = (60 + minute) / 5;
                margin = 0;
            }
            margin += slotSize * numOfSlots - getResources().getInteger(R.integer.deadline_view_arrow_position);
            margin += 2;
        }

        return margin;
    }

    public static class DatePickerFragment extends DialogFragment {
        // A OnDateSetListener that listens to this fragment's DateSet event.
        private DatePickerDialog.OnDateSetListener mOnDateSetListener;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get arguments passed by CalendarTabFragment
            Bundle args = getArguments();
            int year = args.getInt("year");
            int month = args.getInt("month");
            int day = args.getInt("day");

            // Create a new instance of DatePickerDialog
            return new DatePickerDialog(getActivity(), mOnDateSetListener, year, month, day);
        }

        public void setOnDateSetListener(DatePickerDialog.OnDateSetListener listener) {
            this.mOnDateSetListener = listener;
        }
    }

    // Converts dimensions in dp units into pixel units.
    public static float dpToPx(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    // Implement this interface to handle actions in activity.
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction();
    }

    // TODO: Temporary assignment class. Replace with actual assignment from server.
    class Assignment {
        int assignmentId;
        String courseName;
        String description; // Not needed, but defined anyway.
        Calendar deadline;

        Assignment(int id, String courseName, String description, Calendar deadline) {
            this.assignmentId = id;
            this.courseName = courseName;
            this.description = description;
            this.deadline = deadline;
        }
    }

    // Creates dummy schedules.
    private PersonalSchedule[] createDummyPersonalSchedules(int num) {
        PersonalSchedule[] schedules = new PersonalSchedule[num];

        int i;
        for (i = 0; i < num; i++) {
            Calendar start = Calendar.getInstance();
            start.set(Calendar.HOUR_OF_DAY, i);
            start.set(Calendar.MINUTE, 0);

            Calendar end = Calendar.getInstance();
            end.set(Calendar.HOUR_OF_DAY, (i + 1));
            end.set(Calendar.MINUTE, 0);

            schedules[i] = new PersonalSchedule(i, 1, start, end, new ArrayList<Integer>(), "Schedule " + i);
        }

        return schedules;
    }

    // Creates dummy schedules.
    private TimeForAssignment[] createDummyTimeForAssignments(int num) {
        TimeForAssignment[] schedules = new TimeForAssignment[num];

        int i;
        for (i = 0; i < num; i++) {
            Calendar start = Calendar.getInstance();
            start.set(Calendar.HOUR_OF_DAY, 12 + i);
            start.set(Calendar.MINUTE, 0);

            Calendar end = Calendar.getInstance();
            end.set(Calendar.HOUR_OF_DAY, 12 + (i + 1));
            end.set(Calendar.MINUTE, 0);

            schedules[i] = new TimeForAssignment(i, 1, start, end, new ArrayList<Integer>(), i, "Assignment "+i, "Course " + i);
        }

        return schedules;
    }


    private Assignment[] createDummyAssignments(int num) {
        Assignment[] assignments = new Assignment[num];

        int i;
        for (i = 0; i < num; i++) {
            Calendar deadline = Calendar.getInstance();
            deadline.set(Calendar.HOUR_OF_DAY, num*i);
            deadline.set(Calendar.MINUTE, 30);
            String courseName;
            if (i % 2 == 1) {
                courseName = "Logical writing";
            } else {
                courseName = "Computer Science";
            }

            assignments[i] = new Assignment(num * i, courseName, "Hi", deadline);
        }

        return assignments;
    }
}
