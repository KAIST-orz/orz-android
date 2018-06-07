package kr.ac.kaist.orz;

import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.SubtitleCollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.ac.kaist.orz.models.Alarms;
import kr.ac.kaist.orz.models.Assignment;
import kr.ac.kaist.orz.models.PersonalSchedule;
import kr.ac.kaist.orz.models.Schedule;
import kr.ac.kaist.orz.models.StudentAssignment;
import kr.ac.kaist.orz.models.TimeForAssignment;
import kr.ac.kaist.orz.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterScheduleActivity extends AppCompatActivity {
    // The index in the list of schedule types (custom/time for assignment).
    private int currentTypePosition = 0;

    // The collapsing toolbar layout.
    private SubtitleCollapsingToolbarLayout collapsingToolbarLayout;

    private List<StudentAssignment> assignments = new ArrayList<StudentAssignment>();

    public static final int RESULT_OK_PERSONAL_SCHEDULE = 4867;
    public static final int RESULT_OK_TIME_FOR_ASSIGNMENT = 4868;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_schedule);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // CollapstingToolbarLayout
        collapsingToolbarLayout
                = (SubtitleCollapsingToolbarLayout) findViewById(R.id.subtitlecollapsingtoolbarlayout);

        // Set text colors.
        int titleColor = ContextCompat.getColor(this, R.color.colorPrimary);
        int subtitleColor = ContextCompat.getColor(this, R.color.colorButtonNormal);
        collapsingToolbarLayout.setCollapsedTitleTextColor(titleColor);
        collapsingToolbarLayout.setCollapsedSubtitleTextColor(subtitleColor);
        collapsingToolbarLayout.setExpandedTitleTextColor(titleColor);
        collapsingToolbarLayout.setExpandedSubtitleTextColor(subtitleColor);

        // Set title.
        collapsingToolbarLayout.setTitle("Register a custom schedule");

        // Display 'X' button on toolbar.
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // The ListView to show assignments.
        final ListView listView = findViewById(R.id.listView_schedule_type);

        // Add "<course name>\n<assignment name> strings to scheduleTypes list.
        final ArrayList<String> scheduleTypes = new ArrayList<>();
        scheduleTypes.add("Custom schedule");

        // Attach adapter to the ListView.
        final ArrayAdapter<String> scheduleTypeAdapter
                            = new ArrayAdapter<>(RegisterScheduleActivity.this, R.layout.schedule_type_list_item, scheduleTypes);
        listView.setAdapter(scheduleTypeAdapter);

        // Do not allow multiple choices.
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        // Check "Custom schedule" by default.
        listView.setItemChecked(0, true);

        // Get list of assignments from the server.
        OrzApi api = ApplicationController.getInstance().getApi();
        User user = ApplicationController.getInstance().getUser();
        Call<List<StudentAssignment>> call = api.getStudentAssignments(user.getID());
        call.enqueue(new Callback<List<StudentAssignment>>() {
            @Override
            public void onResponse(Call<List<StudentAssignment>> call, Response<List<StudentAssignment>> response) {
                if (response.isSuccessful()) {
                    // Get all assignments and sort it by due date.
                    assignments.addAll(response.body());
                    Collections.sort(assignments, StudentAssignment.DUE_COMPARATOR);

                    for (Assignment assignment : assignments) {
                        scheduleTypes.add(assignment.getCourseName() + "\n" + assignment.getName());
                    }

                    scheduleTypeAdapter.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(listView);
                } else {
                }
            }

            @Override
            public void onFailure(Call<List<StudentAssignment>> call, Throwable t) {

            }
        });

        // Set default background color: background for personal schedule.
        int backgroundColor = Colors.getPersonalScheduleColor(this);
        collapsingToolbarLayout.setContentScrimColor(backgroundColor);
        collapsingToolbarLayout.setBackgroundColor(backgroundColor);

        // Set OnItemSelectedListener.
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (currentTypePosition != position) {
                    currentTypePosition = position;

                    // The text box to edit schedule name.
                    EditText scheduleNameText = findViewById(R.id.editText_schedule_name);
                    // The color of the toolbar.
                    int color;

                    if (position == 0) {
                        // Show appropriate page title.
                        collapsingToolbarLayout.setTitle("Register a custom schedule");
                        collapsingToolbarLayout.setSubtitle("");

                        // The text box should be enabled when adding a personal schedule.
                        scheduleNameText.setEnabled(true);
                        scheduleNameText.setHint("Schedule Name");

                        // Set toolbar color as personal schedule's color.
                        color = Colors.getPersonalScheduleColor(RegisterScheduleActivity.this);
                    } else {
                        Assignment assignment = assignments.get(position - 1);
                        collapsingToolbarLayout.setTitle(assignment.getCourseName());
                        collapsingToolbarLayout.setSubtitle("Register time to do " + assignment.getName());

                        // The text box should be disabled when adding a time for assignment.
                        scheduleNameText.setEnabled(false);
                        scheduleNameText.setHint("Time to do " + assignment.getName());

                        // Set toolbar color as the course's color.
                        color = Colors.getCourseColor(RegisterScheduleActivity.this, assignment.getCourseID());
                    }

                    // Set color of the toolbar.
                    collapsingToolbarLayout.setContentScrimColor(color);
                    collapsingToolbarLayout.setBackgroundColor(color);
                }
            }
        });
    }

    public void selectTime(final View v) {
        final AlertDialog.Builder adb = new AlertDialog.Builder(this);
        final DatePicker dp = new DatePicker(this);
        final TimePicker tp = new TimePicker(this);

        adb.setView(dp);
        adb.setPositiveButton("set",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getApplicationContext(), "test1", Toast.LENGTH_LONG).show();
                        adb.setPositiveButton("set",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Calendar cal = Calendar.getInstance();
                                        cal.set(dp.getYear(), dp.getMonth(), dp.getDayOfMonth(), tp.getCurrentHour(), tp.getCurrentMinute());
                                        Date date = cal.getTime();

                                        SimpleDateFormat parser = new SimpleDateFormat("HH:mm, dd MMMM yyyy");
                                        ((TextView) v).setText(parser.format(date));

                                        Toast.makeText(getApplicationContext(), String.valueOf(dp.getYear()) + "-" + String.valueOf(dp.getMonth() + 1) + "-" + String.valueOf(dp.getDayOfMonth()) + "\n" + String.valueOf(tp.getCurrentHour()) + ":" + String.valueOf(tp.getCurrentMinute()), Toast.LENGTH_LONG).show();
                                    }
                                });
                        adb.setNegativeButton("cancel", null);
                        adb.setView(tp);
                        adb.show();
                    }
                });
        adb.setNegativeButton("cancel", null);
        adb.show();
    }

    public void create(View v) throws ParseException {
        EditText schedule_name = findViewById(R.id.editText_schedule_name);
        TextView start_time = findViewById(R.id.textView_start_time);
        TextView end_time = findViewById(R.id.textView_end_time);

        if(schedule_name.length() == 0) {
            if (currentTypePosition == 0) {
                Toast.makeText(this, "Schedule name can not be empty", Toast.LENGTH_LONG).show();
            }
        }
        else if(start_time.getText().equals("Start Time")) {
            Toast.makeText(this, "Start time can not be empty", Toast.LENGTH_LONG).show();
        }
        else if(end_time.getText().equals("End Time")) {
            Toast.makeText(this, "End time can not be empty", Toast.LENGTH_LONG).show();
        }
        else {
            // Post the new schedule.
            OrzApi api = ApplicationController.getInstance().getApi();
            User user = ApplicationController.getInstance().getUser();
            Map<String, Object> body = new HashMap<>();

            // Put start and end time.
            SimpleDateFormat parser1 = new SimpleDateFormat("HH:mm, dd MMMM yyyy");
            SimpleDateFormat parser2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            body.put("start", parser2.format(parser1.parse(start_time.getText().toString())));
            body.put("end", parser2.format(parser1.parse(end_time.getText().toString())));

            if (currentTypePosition == 0) { // Post a personal schedule.
                body.put("name", schedule_name.getText().toString());
                body.put("alarms", new ArrayList<Integer>());
                Call<PersonalSchedule> call = api.registerStudentPersonalSchedule(user.getID(), body);

                call.enqueue(new Callback<PersonalSchedule>() {
                    @Override
                    public void onResponse(Call<PersonalSchedule> call, Response<PersonalSchedule> response) {
                        if (response.isSuccessful()) {
                            // Prompt a toast message notifying success.
                            Toast.makeText(RegisterScheduleActivity.this, "Successfully registered your schedule. ", Toast.LENGTH_LONG).show();

                            // Pass the schedule to the OrzMainActivity so that calendar tab shows this new schedule.
                            Intent intent = new Intent();
                            intent.putExtra("schedule", response.body());

                            setResult(RESULT_OK_PERSONAL_SCHEDULE, intent);
                            finish();
                        } else {
                            Toast.makeText(RegisterScheduleActivity.this, response.code(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PersonalSchedule> call, Throwable t) {
                        Toast.makeText(RegisterScheduleActivity.this, "Failed to register your schedule. ", Toast.LENGTH_LONG).show();
                    }
                });

            } else {    // Post a time for assignment.
                body.put("studentAssignment", assignments.get(currentTypePosition - 1));
                body.put("alarms", new ArrayList<Integer>());
                Call<TimeForAssignment> call = api.registerStudentTimeForAssignment(user.getID(), assignments.get(currentTypePosition - 1).getID(), body);

                call.enqueue(new Callback<TimeForAssignment>() {
                    @Override
                    public void onResponse(Call<TimeForAssignment> call, Response<TimeForAssignment> response) {
                        if (response.isSuccessful()) {
                            // Prompt a toast message notifying success.
                            Toast.makeText(RegisterScheduleActivity.this, "Successfully registered your schedule. ", Toast.LENGTH_LONG).show();

                            // Pass the schedule to the OrzMainActivity so that calendar tab shows this new schedule.
                            Intent intent = new Intent();
                            intent.putExtra("schedule", response.body());

                            setResult(RESULT_OK_PERSONAL_SCHEDULE, intent);
                            finish();
                        } else {
                            Toast.makeText(RegisterScheduleActivity.this, response.code(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<TimeForAssignment> call, Throwable t) {
                        Toast.makeText(RegisterScheduleActivity.this, "Failed to register your schedule. ", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

     public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
