package kr.ac.kaist.orz;

import android.app.Activity;
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
import android.widget.TextView;

import java.util.Calendar;
import java.text.DateFormat;

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
    private Calendar calendar;

    // The layout onto which the schedule views are placed.
    private ConstraintLayout scheduleLayout;

    // TODO: Tracks the number of schedules being displayed currently.
    // TODO: Might keep the list of schedules later on, thus may override this.
    private int numOfCurrentSchedules;

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
        calendar = Calendar.getInstance();
        numOfCurrentSchedules = -1;     // Initialize to -1 to indicate the initiation.
    }

    // Formats the date held by calendar to "MMM DD, YYYY" format.
    public String formatDateOfCalendar() {
        DateFormat formatter = DateFormat.getDateInstance(DateFormat.MEDIUM);
        return formatter.format(calendar.getTime());
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

        // TODO: Dummy schedule for test display

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
                calendar.add(Calendar.DATE, -1);
                pickDate.setText(formatDateOfCalendar());

                // TODO: Communicate with the server and draw schedules on the layout.
                displaySchedules(getSchedules());
            }
        });

        toNextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DATE, 1);
                pickDate.setText(formatDateOfCalendar());

                // TODO: Communicate with the server and draw schedules on the layout.
                displaySchedules(getSchedules());
            }
        });

        // Let the button to show picked date.
        pickDate.setText(formatDateOfCalendar());

        // Display the schedules.
        displaySchedules(getSchedules());

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
        args.putInt("year", calendar.get(Calendar.YEAR));
        args.putInt("month", calendar.get(Calendar.MONTH));
        args.putInt("day", calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setArguments(args);

        // Show to the user. fragment manager is OrzMainActivity's manager.
        dialog.show(parentActivity.getFragmentManager(), "datePicker");
    }

    // Listener for the date picker's DateSet event.
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Change where the calendar is pointing.
        calendar.set(year, month, day);
        pickDate.setText(formatDateOfCalendar());

        // Get schedules (possibly from the server) and display onto screen.
        displaySchedules(getSchedules());
    }

    // TODO: Communicates with the server to acquire the schedules for this day.
    public Schedule[] getSchedules() {
        return createDummySchedules(4);
    }

    // Displays the schedules of the date the user has picked.
    private void displaySchedules(Schedule[] schedules) {
        // If this is not the first time to be called, clear the schedules currently being shown.
        if (numOfCurrentSchedules != -1) {
            clearSchedules();
        } else {
            numOfCurrentSchedules = 0;
        }

        for (Schedule schedule : schedules) {
            // TODO: these schedules should be first filtered before being passed to this method.
            if ((schedule.end.get(Calendar.YEAR) < calendar.get(Calendar.YEAR)
                    || schedule.end.get(Calendar.DAY_OF_YEAR) < calendar.get(Calendar.DAY_OF_YEAR)
                    || schedule.start.get(Calendar.YEAR) > calendar.get(Calendar.YEAR)
                    || schedule.start.get(Calendar.DAY_OF_YEAR) > calendar.get(Calendar.DAY_OF_YEAR))
                    || (schedule.end.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)
                        && schedule.end.get(Calendar.HOUR_OF_DAY) == 0
                        && schedule.end.get(Calendar.MINUTE) == 0)) {
                continue;
            }
            // Display only when the view is for today.
            createScheduleView(schedule);
            numOfCurrentSchedules++;    // Count only the displayed ones.
        }
    }

    // Removes the schedules from the screen.
    private void clearSchedules() {
        if (numOfCurrentSchedules > 0) {
            scheduleLayout.removeViews(scheduleLayout.getChildCount() - numOfCurrentSchedules,
                                        numOfCurrentSchedules);
        }
        numOfCurrentSchedules = 0;
    }

    /*
     * Creates a view for a schedule and adds it to the layout on the screen.
     * Assumptions:
     * 1. Schedules are given in units of 5 minutes.
     * 2. Layout does not change: I used some hard-coded values for the height of the view.
     */
    private void createScheduleView(final Schedule schedule) {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        LayoutInflater inflater = activity.getLayoutInflater();

        // Calculate the height of the view and determine which layout resource to use.
        View scheduleView;
        int height = calculateScheduleViewHeight(schedule.start, schedule.end);
        int numOfTextViews = 1;     // Number of TextViews in the layout.

        // There are four resources we can make use of according to the height.
        if (schedule.description != null
                && height >= getResources().getInteger(R.integer.schedule_view_large_height)) {
            // With description (assignment schedule), and it is large enough.
            scheduleView = inflater.inflate(R.layout.schedule_view_large, null);
            numOfTextViews = 2;
        } else if (height >= getResources().getInteger(R.integer.schedule_view_medium_height)) {
            scheduleView = inflater.inflate(R.layout.schedule_view_medium, null);
        } else if (height >= getResources().getInteger(R.integer.schedule_view_small_height)) {
            scheduleView = inflater.inflate(R.layout.schedule_view_small, null);
        } else {
            scheduleView = new View(activity);
            numOfTextViews = 0;
        }

        // Fill in the title and description of the schedule.
        switch (numOfTextViews) {
            case 2:
                TextView description = scheduleView.findViewById(R.id.schedule_description);
                description.setText(schedule.description);
            case 1:
                TextView title = scheduleView.findViewById(R.id.schedule_title);
                title.setText(schedule.title);
            default:
                break;
        }

        // TODO: Fill appropriate background and font color.
        scheduleView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));

        // Assign a unique ID to setup constraints of the view.
        int viewId = View.generateViewId();
        scheduleView.setId(viewId);

        // Add view to the layout (required to setup constraints).
        scheduleLayout.addView(scheduleView);

        // Get the width of the screen and set it as minimum width (the view then expands as desired).
        WindowManager wm = activity.getWindowManager();
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int windowWidth = size.x;

        // Set width and height of the scheduleView.
        scheduleView.setMinimumHeight((int) dpToPx(height));
        scheduleView.setMinimumWidth(windowWidth);

        // Constrain the top and the left side of the view.
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(scheduleLayout);

        // Constrain the left to the delimiter in the layout.
        int leftMarginInPx = (int) getResources().getDimension(R.dimen.schedule_view_left_margin);
        constraintSet.connect(scheduleView.getId(), ConstraintSet.START,
                            R.id.delimiter, ConstraintSet.END, (int) leftMarginInPx);

        // Constrain the top of the view to appropriate place.
        if (schedule.start.get(Calendar.YEAR) < calendar.get(Calendar.YEAR)
                || schedule.start.get(Calendar.DAY_OF_YEAR) < calendar.get(Calendar.DAY_OF_YEAR)) {
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

        // Add onClickListener to the view to change to the appropriate activity.
        scheduleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ScheduleDetailsActivity.class);
                intent.putExtra("schedule_id", schedule.scheduleId);    // Pass schedule ID.
                startActivity(intent);
            }
        });

        constraintSet.applyTo(scheduleLayout);
    }

    // Calculates the height of a view for a schedule in dp units.
    private int calculateScheduleViewHeight(Calendar start, Calendar end) {
        int height = 0; // The height of the view.

        int startHour;      // The hour the schedule starts in the current day.
        int startMinute;    // The minute in an hour the schedule starts in the current day.
        int endHour;        // The hour the schedule starts in the current day.
        int endMinute;      // The minute in an hour the schedule starts in the current day.

        // Adding top margin in case the schedule starts before the current day.
        if (start.get(Calendar.DAY_OF_YEAR) < calendar.get(Calendar.DAY_OF_YEAR)) {
            height += getResources().getInteger(R.integer.timeline_margin_top) + 2;
            startHour = 0;
            startMinute = 0;
        } else {
            startHour = start.get(Calendar.HOUR_OF_DAY);
            startMinute = start.get(Calendar.MINUTE);
        }

        // Adding bottom margin in case the schedule
        if (end.get(Calendar.DAY_OF_YEAR) > calendar.get(Calendar.DAY_OF_YEAR)) {
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
        // 5 minutes in the calendar tab corresponds to @integer/timeline_5min dp.
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

    }

    // Clears the deadline views.
    public void clearDeadlines() {

    }

    public void createDeadlineView(final Assignment assignment) {

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

    // TODO: Temporary schedule class. Replace with actual schedule from server.
    class Schedule {
        int scheduleId;
        Calendar start;
        Calendar end;
        String title;
        String description;

        Schedule(Calendar start, Calendar end, String title, String description, int id) {
            this.scheduleId = id;
            this.start = start;
            this.end = end;
            this.title = title;
            this.description = description;
        }
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
    private Schedule[] createDummySchedules(int num) {
        Schedule[] schedules = new Schedule[num];

        int i;
        for (i = 0; i < num-1; i++) {
            Calendar start = Calendar.getInstance();
            start.set(Calendar.HOUR_OF_DAY, num * i);
            start.set(Calendar.MINUTE, 0);

            Calendar end = Calendar.getInstance();
            end.set(Calendar.HOUR_OF_DAY, num * (i + 1));
            end.set(Calendar.MINUTE, 0);

            schedules[i] = new Schedule(start, end, "Assignment " + i, "Description " + i, num * i);
        }

        Calendar start = Calendar.getInstance();
        start.set(Calendar.HOUR_OF_DAY, num * i);
        start.set(Calendar.MINUTE, 0);

        Calendar end = Calendar.getInstance();
        end.add(Calendar.DATE, 1);
        end.set(Calendar.HOUR_OF_DAY, 0);
        end.set(Calendar.MINUTE, 0);

        schedules[i] = new Schedule(start, end, "Assignment " + i, "Description " + i, num * i);
        return schedules;
    }

    private Assignment[] createDummyAssignments(int num) {
        Assignment[] assignments = new Assignment[num];

        int i;
        for (i = 0; i < num; i++) {
            Calendar deadline = Calendar.getInstance();
            deadline.set(Calendar.HOUR_OF_DAY, num * i);
            deadline.set(Calendar.MINUTE, 30);

            assignments[i] = new Assignment(num * i, "Computer science", "Hi", deadline);
        }

        return assignments;
    }

}
