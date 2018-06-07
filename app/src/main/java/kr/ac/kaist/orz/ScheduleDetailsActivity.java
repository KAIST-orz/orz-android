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


        // Set page title according to the type of schedule (personal or time for assignment).
        if (schedule instanceof PersonalSchedule) {
            collapsingToolbarLayout.setTitle(((PersonalSchedule) schedule).getName());
            collapsingToolbarLayout.setSubtitle("Custom Schedule");
        }
        else if (schedule instanceof TimeForAssignment) {
            collapsingToolbarLayout.setTitle(((TimeForAssignment) schedule).getCourseName());
            collapsingToolbarLayout.setSubtitle(((TimeForAssignment) schedule).getAssignmentName());
        }

        // Set the page color according to the type of schedule.
        int backgroundColor = Colors.getScheduleColor(this, schedule);
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
