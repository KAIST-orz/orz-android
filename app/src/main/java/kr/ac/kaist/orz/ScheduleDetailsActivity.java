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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import kr.ac.kaist.orz.models.Assignment;
import kr.ac.kaist.orz.models.PersonalSchedule;
import kr.ac.kaist.orz.models.Schedule;
import kr.ac.kaist.orz.models.StudentAssignment;
import kr.ac.kaist.orz.models.TimeForAssignment;
import kr.ac.kaist.orz.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleDetailsActivity extends AppCompatActivity {

    List<String> notification_time = new ArrayList<String>();

    TextArrayAdapter adapter;

    Schedule schedule;

    boolean is_personal_schedule;
    boolean is_time_for_assignment;

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

        final AlertDialog.Builder adb = new AlertDialog.Builder(this);

        Intent intent = getIntent();
        schedule = (Schedule) intent.getExtras().getSerializable("schedule");

        if (PersonalSchedule.class.isInstance(schedule)) {
            is_personal_schedule = true;
            is_time_for_assignment = false;
        }

        final String[] times = new String[] {"1min", "3min", "5min", "10min", "15min", "30min", "1hour", "2hour", "3hour"};
        final Integer[] times_int = new Integer[] {1, 3, 5, 10, 15, 30, 60, 120, 180};

        if(is_personal_schedule)
            notification_time.add(times[Arrays.asList(times_int).indexOf(ApplicationController.getInstance().getAlarms().getPersonalScheduleAlarm())] + " (default)");
        else
            notification_time.add(times[Arrays.asList(times_int).indexOf(ApplicationController.getInstance().getAlarms().getTimeForAssignmentAlarm())] + " (default)");

        for(Integer alarm : schedule.getAlarms()) {
            notification_time.add(times[ Arrays.asList(times_int).indexOf(alarm) ]);
        }

        notification_time.add("Add more notification");

        // The ListView to show notification times set.
        ListView listView1 = findViewById(R.id.listView_notification);
        adapter = new TextArrayAdapter(this, notification_time);
        listView1.setAdapter(adapter);
        setListViewHeightBasedOnChildren(listView1);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if(notification_time.get(position).equals("Add more notification")) {
                    selectAlarm();
                }
                else if(position > 0) {
                    adb.setTitle("remove notification?");
                    adb.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    OrzApi api = ApplicationController.getInstance().getApi();
                                    User user = ApplicationController.getInstance().getUser();
                                    Map<String, Object> body = new HashMap<>();
                                    schedule.removeAlarm(position - 1);
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.KOREA);
                                    body.put("start", sdf.format(schedule.getStart().getTime()));
                                    body.put("end", sdf.format(schedule.getEnd().getTime()));
                                    body.put("alarms", schedule.getAlarms());
                                    if(is_personal_schedule)
                                        body.put("name", ((PersonalSchedule) schedule).getName());
                                    Call<Void> call;
                                    if(is_personal_schedule)
                                        call = api.updatePersonalSchedule(user.getID(), schedule.id, body);
                                    else
                                        call = api.updateTimeForAssignment(user.getID(), ((TimeForAssignment) schedule).getAssignmentID(), schedule.id, body);
                                    call.enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            if(response.isSuccessful() && response.code()==200) {
                                                Toast.makeText(ScheduleDetailsActivity.this, "alarm remove success", Toast.LENGTH_LONG).show();
                                                notification_time.remove(position);
                                                adapter.notifyDataSetChanged();
                                                setListViewHeightBasedOnChildren((ListView) findViewById(R.id.listView_notification));
                                            }
                                            else {
                                                Toast.makeText(ScheduleDetailsActivity.this, "alarm remove failed " + response.code(), Toast.LENGTH_LONG).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {
                                            Toast.makeText(ScheduleDetailsActivity.this, "Not connected to server", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            });
                    adb.setNegativeButton("No", null);
                    adb.show();
                }
            }
        });

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
        final String[] times = new String[] {"1min", "3min", "5min", "10min", "15min", "30min", "1hour", "2hour", "3hour"};
        final int[] times_int = new int[] {1, 3, 5, 10, 15, 30, 60, 120, 180};
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("set time");
        adb.setSingleChoiceItems(times, 0, null);
        adb.setPositiveButton("close",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        final ListView lw = ((AlertDialog)dialog).getListView();
                        OrzApi api = ApplicationController.getInstance().getApi();
                        User user = ApplicationController.getInstance().getUser();
                        Map<String, Object> body = new HashMap<>();
                        schedule.addAlarm(times_int[lw.getCheckedItemPosition()]);
                        Collections.sort(schedule.getAlarms());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.KOREA);
                        body.put("start", sdf.format(schedule.getStart().getTime()));
                        body.put("end", sdf.format(schedule.getEnd().getTime()));
                        body.put("alarms", schedule.getAlarms());
                        if(is_personal_schedule)
                            body.put("name", ((PersonalSchedule) schedule).getName());
                        Call<Void> call;
                        if(is_personal_schedule)
                            call = api.updatePersonalSchedule(user.getID(), schedule.id, body);
                        else
                            call = api.updateTimeForAssignment(user.getID(), ((TimeForAssignment) schedule).getAssignmentID(), schedule.id, body);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if(response.isSuccessful() && response.code()==200) {
                                    Toast.makeText(ScheduleDetailsActivity.this, "alarm set success", Toast.LENGTH_LONG).show();
                                    notification_time.add(notification_time.size() - 1, times[lw.getCheckedItemPosition()]);
                                    adapter.notifyDataSetChanged();
                                    setListViewHeightBasedOnChildren((ListView) findViewById(R.id.listView_notification));
                                }
                                else {
                                    Toast.makeText(ScheduleDetailsActivity.this, "alarm set failed " + response.code(), Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(ScheduleDetailsActivity.this, "Not connected to server", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
        adb.show();
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
                                        //Toast.makeText(getApplicationContext(), "test2", Toast.LENGTH_LONG).show();
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.set(dp.getYear(), dp.getMonth(), dp.getDayOfMonth(), tp.getCurrentHour(), tp.getCurrentMinute());
                                        final Date date = calendar.getTime();

                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.KOREA);

                                        OrzApi api = ApplicationController.getInstance().getApi();
                                        User user = ApplicationController.getInstance().getUser();
                                        Map<String, Object> body = new HashMap<>();
                                        if(v.equals(findViewById(R.id.textView_start_time))) {
                                            body.put("start", sdf.format(date));
                                            body.put("end", sdf.format(schedule.getEnd().getTime()));
                                        }
                                        else {
                                            body.put("start", sdf.format(schedule.getStart().getTime()));
                                            body.put("end", sdf.format(date));
                                        }
                                        body.put("alarms", schedule.getAlarms());
                                        if(is_personal_schedule)
                                            body.put("name", ((PersonalSchedule) schedule).getName());

                                        Call<Void> call;
                                        if(is_personal_schedule)
                                            call = api.updatePersonalSchedule(user.getID(), schedule.id, body);
                                        else
                                            call = api.updateTimeForAssignment(user.getID(), ((TimeForAssignment) schedule).getAssignmentID(), schedule.id, body);
                                        call.enqueue(new Callback<Void>() {
                                            @Override
                                            public void onResponse(Call<Void> call, Response<Void> response) {
                                                if(response.isSuccessful() && response.code()==200) {
                                                    Toast.makeText(ScheduleDetailsActivity.this, "time set success", Toast.LENGTH_LONG).show();
                                                    SimpleDateFormat parser = new SimpleDateFormat("HH:mm, dd MMMM yyyy");
                                                    ((TextView) v).setText(parser.format(date));
                                                }
                                                else {
                                                    Toast.makeText(ScheduleDetailsActivity.this, "Please check your duration is valid and not overlapping", Toast.LENGTH_LONG).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Void> call, Throwable t) {
                                                Toast.makeText(ScheduleDetailsActivity.this, "Not connected to server", Toast.LENGTH_LONG).show();
                                            }
                                        });



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

        final OrzApi api = ApplicationController.getInstance().getApi();
        final User user = ApplicationController.getInstance().getUser();

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Are you sure to delete this assignment?");
        adb.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Call<Void> call;
                if(is_personal_schedule)
                    call = api.deletePersonalSchedule(user.getID(), schedule.id);
                else
                    call = api.deleteTimeForAssignment(user.getID(), ((TimeForAssignment) schedule).getAssignmentID(), schedule.id);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful() && response.code()==200) {
                            Toast.makeText(ScheduleDetailsActivity.this, "Successfully deleted", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else {
                            Toast.makeText(ScheduleDetailsActivity.this, "Deletion failed", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(ScheduleDetailsActivity.this, "Not connected to server", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        adb.setNegativeButton("no", null);
        adb.show();
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
