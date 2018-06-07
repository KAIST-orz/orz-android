package kr.ac.kaist.orz;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.ScientificNumberFormatter;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import kr.ac.kaist.orz.models.Assignment;
import kr.ac.kaist.orz.models.PersonalSchedule;
import kr.ac.kaist.orz.models.Schedule;
import kr.ac.kaist.orz.models.StudentAssignment;
import kr.ac.kaist.orz.models.TimeForAssignment;

public class ScheduleDetailsActivity extends AppCompatActivity {

    List<String> notification_time = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // CollapsingToolbarLayout
        SubtitleCollapsingToolbarLayout collapsingToolbarLayout
                = (SubtitleCollapsingToolbarLayout) findViewById(R.id.subtitlecollapsingtoolbarlayout);

        // Set text colors.
        int titleColor = ContextCompat.getColor(this, R.color.colorPrimary);
        int subtitleColor = ContextCompat.getColor(this, R.color.colorButtonNormal);
        collapsingToolbarLayout.setCollapsedTitleTextColor(titleColor);
        collapsingToolbarLayout.setCollapsedSubtitleTextColor(subtitleColor);
        collapsingToolbarLayout.setExpandedTitleTextColor(titleColor);
        collapsingToolbarLayout.setExpandedSubtitleTextColor(subtitleColor);

        // Display 'X' button on toolbar.
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        notification_time.add("5 minute before (Default)");
        notification_time.add("10 minute before");
        notification_time.add("Add more notification");

        final AlertDialog.Builder adb = new AlertDialog.Builder(this);

        Intent intent = getIntent();
        Schedule schedule = (Schedule) intent.getExtras().getSerializable("schedule");

        // The ListView to show notification times set.
        ListView listView1 = findViewById(R.id.listView_notification);
        TextArrayAdapter adapter = new TextArrayAdapter(this, notification_time);
        listView1.setAdapter(adapter);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if(notification_time.get(position).equals("Add more notification")) {
                    selectAlarm();
                }
                else {
                    adb.setTitle("remove notification?");
                    adb.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), "remove alarm #" + String.valueOf(position), Toast.LENGTH_LONG).show();
                                }
                            });
                    adb.setNegativeButton("No", null);
                    adb.show();
                }
            }
        });
        setListViewHeightBasedOnChildren(listView1);

        // TODO: Get list of assignments from the server.
        // TODO: Sort by some criteria (e.g. due date).
        ArrayList<StudentAssignment> assignments = null;

        // Make a list of strings containing "<course name>\n<assignment name>
        // with header "Custom schedule".
        ArrayList<String> scheduleTypes = new ArrayList<>();
        scheduleTypes.add("Custom schedule");

        for (Assignment assignment : assignments) {
            scheduleTypes.add(assignment.getCourseName() + "\n" + assignment.getName());
        }

        // Set which checkbox should be checked.
        int currentType = 0;
        if (schedule instanceof TimeForAssignment) {
            for (int i = 0; i < assignments.size(); i++) {
                // TODO: implement TimeForAssignment$getAssignmentId()
                if (assignments.get(i).getID() == ((TimeForAssignment) schedule).getAssignmentId()) {
                    currentType = i + 1;
                    break;
                }
            }
        }

        // The ListView to show which type (personal schedule or time for which assignment) of schedule this is.
        ListView listView2 = findViewById(R.id.listView_schedule_type);
        ArrayAdapter<String> scheduleTypeAdapter = new ArrayAdapter<>(this, R.layout.schedule_type_list_item, scheduleTypes);
        listView2.setAdapter(scheduleTypeAdapter);
        listView2.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // Check appropriate checkbox.
        listView2.setItemChecked(currentType, true);

        // Set OnItemSelectedListener.
        final int[] currentTypeContainer = new int[]{currentType};
        listView2.setOnItemSelectedListener(new ListView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (currentTypeContainer[0] == position) {
                    // Do nothing.
                } else {
                    // TODO: Send the server that change occurred on this schedule.
                    // TODO: Set the background color of the action bar and modify fields of 'schedule'.
                    // TODO: Might have to refresh this activity.
                    currentTypeContainer[0] = position;
                    finish();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Not possible.
            }
        });

        // Display the title of this page according to the schedule type.
        // TODO: Set appropriate color according to the schedule type.
        if (PersonalSchedule.class.isInstance(schedule)) {
            collapsingToolbarLayout.setTitle(((PersonalSchedule) schedule).getName());
            collapsingToolbarLayout.setSubtitle("Custom Schedule");
        }
        else if (TimeForAssignment.class.isInstance(schedule)) {
            collapsingToolbarLayout.setTitle(((TimeForAssignment) schedule).getCourseName());
            collapsingToolbarLayout.setSubtitle(((TimeForAssignment) schedule).getAssignmentName());
        }

        int backgroundColor = ContextCompat.getColor(this, R.color.colorAccent);
        collapsingToolbarLayout.setContentScrimColor(backgroundColor);
        collapsingToolbarLayout.setBackgroundColor(backgroundColor);

        // Display start and end time of the schedule.
        TextView startText = findViewById(R.id.textView_start_time);
        TextView endText = findViewById(R.id.textView_end_time);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, dd MMMM yyyy");
        startText.setText(sdf.format(schedule.getStart().getTime()));
        endText.setText(sdf.format(schedule.getEnd().getTime()));
    }

    public void selectAlarm() {
        final String[] times = new String[] {"1min", "3min", "5min", "10min", "30min", "1hour", "2hour", "3hour"};
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("set time");
        adb.setSingleChoiceItems(times, 1, null);
        adb.setPositiveButton("close",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListView lw = ((AlertDialog)dialog).getListView();
                        Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                        Toast.makeText(getApplicationContext(), "time set to ".concat(checkedItem.toString()), Toast.LENGTH_LONG).show();
                    }
                });
        adb.show();
    }

    public void selectTime(View v) {
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
                                        //Toast.makeText(getApplicationContext(), "test2", Toast.LENGTH_LONG).show();
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

    public void delete(View v) {
        Toast.makeText(this, "schedule deleted", Toast.LENGTH_LONG).show();
        finish();
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
