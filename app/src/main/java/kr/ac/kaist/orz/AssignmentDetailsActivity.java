package kr.ac.kaist.orz;

import android.app.ApplicationErrorReport;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.SubtitleCollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
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

import kr.ac.kaist.orz.models.StudentAssignment;
import kr.ac.kaist.orz.models.TimeForAssignment;
import kr.ac.kaist.orz.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignmentDetailsActivity extends AppCompatActivity {

    List<String> notification_time = new ArrayList<String>();
    List<String> time_for_assignment = new ArrayList<String>();

    TextArrayAdapter adapter1;
    TextArrayAdapter adapter2;

    Intent intent;
    StudentAssignment assignment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        intent = getIntent();
        assignment = (StudentAssignment) intent.getExtras().getSerializable("assignment");

        // CollapsingToolbarLayout
        SubtitleCollapsingToolbarLayout collapsingToolbarLayout =
                (SubtitleCollapsingToolbarLayout) findViewById(R.id.subtitlecollapsingtoolbarlayout);

        // Set text colors.
        int titleColor = ContextCompat.getColor(this, R.color.colorPrimary);
        int subtitleColor = ContextCompat.getColor(this, R.color.colorButtonNormal);
        collapsingToolbarLayout.setCollapsedTitleTextColor(titleColor);
        collapsingToolbarLayout.setCollapsedSubtitleTextColor(subtitleColor);
        collapsingToolbarLayout.setExpandedTitleTextColor(titleColor);
        collapsingToolbarLayout.setExpandedSubtitleTextColor(subtitleColor);

        // Display 'X' button on the toolbar.
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        time_for_assignment.add("Add more time");

        OrzApi api = ApplicationController.getInstance().getApi();
        User user = ApplicationController.getInstance().getUser();
        final List<TimeForAssignment> time_for_assignment_list = new ArrayList<>();
        Call<List<TimeForAssignment>> call = api.getTimesForAssignment(user.getID(), assignment.getID());
        call.enqueue(new Callback<List<TimeForAssignment>>() {
            @Override
            public void onResponse(Call<List<TimeForAssignment>> call, Response<List<TimeForAssignment>> response) {
                if(response.isSuccessful() && response.code()==200) {
                    //Toast.makeText(AssignmentDetailsActivity.this, "getting durations success", Toast.LENGTH_LONG).show();
                    time_for_assignment_list.addAll(response.body());
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, dd MMMM yyyy");
                    for(TimeForAssignment tfa : time_for_assignment_list)
                        time_for_assignment.add(time_for_assignment.size() - 1, sdf.format(tfa.getStart().getTime()) + "\n" + sdf.format(tfa.getEnd().getTime()));
                    adapter2.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren((ListView) findViewById(R.id.listView_time_for_assignment));
                }
                else {
                    Toast.makeText(AssignmentDetailsActivity.this, "getting durations failed " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<TimeForAssignment>> call, Throwable t) {
                Toast.makeText(AssignmentDetailsActivity.this, "Not connected to server", Toast.LENGTH_LONG).show();
            }
        });

        final AlertDialog.Builder adb = new AlertDialog.Builder(this);

        final String[] times = new String[] {"1min", "3min", "5min", "10min", "15min", "30min", "1hour", "2hour", "3hour"};
        final Integer[] times_int = new Integer[] {1, 3, 5, 10, 15, 30, 60, 120, 180};

        notification_time.add(times[Arrays.asList(times_int).indexOf(ApplicationController.getInstance().getAlarms().getAssignmentDueAlarm())] + " (default)");

        for(Integer alarm : assignment.getAlarms()) {
            notification_time.add(times[ Arrays.asList(times_int).indexOf(alarm) ]);
        }

        notification_time.add("Add more notification");

        ListView listView1 = findViewById(R.id.listView_notification);
        adapter1 = new TextArrayAdapter(this, notification_time);
        listView1.setAdapter(adapter1);

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
                                    body.put("timeEstimation", String.valueOf(assignment.getTimeEstimation()));
                                    body.put("significance", String.valueOf(assignment.getSignificance()));
                                    assignment.removeAlarm(position);
                                    body.put("alarms", assignment.getAlarms());
                                    Call<Void> call = api.updateStudentAssignment(user.getID(), assignment.getID(), body);
                                    call.enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            if(response.isSuccessful() && response.code()==200) {
                                                Toast.makeText(AssignmentDetailsActivity.this, "alarm remove success", Toast.LENGTH_LONG).show();
                                                notification_time.remove(position);
                                                adapter1.notifyDataSetChanged();
                                                setListViewHeightBasedOnChildren((ListView) findViewById(R.id.listView_notification));
                                            }
                                            else {
                                                Toast.makeText(AssignmentDetailsActivity.this, "alarm remove failed " + response.code(), Toast.LENGTH_LONG).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {
                                            Toast.makeText(AssignmentDetailsActivity.this, "Not connected to server", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    Toast.makeText(getApplicationContext(), "remove alarm #" + String.valueOf(position), Toast.LENGTH_LONG).show();
                                }
                            });
                    adb.setNegativeButton("No", null);
                    adb.show();
                }
            }
        });

        ListView listView2 = findViewById(R.id.listView_time_for_assignment);
        adapter2 = new TextArrayAdapter(this, time_for_assignment);
        listView2.setAdapter(adapter2);

        // TODO: get appropriate time_for_assignment array (make it sorted), and need to pass as extra in the intent.

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if(time_for_assignment.get(position).equals("Add more time")) {
                    selectDuration();
                }
                else {
                    OrzApi api = ApplicationController.getInstance().getApi();
                    User user = ApplicationController.getInstance().getUser();
                    final List<TimeForAssignment> time_for_assignment_list = new ArrayList<>();
                    Call<List<TimeForAssignment>> call = api.getTimesForAssignment(user.getID(), assignment.getID());
                    call.enqueue(new Callback<List<TimeForAssignment>>() {
                        @Override
                        public void onResponse(Call<List<TimeForAssignment>> call, Response<List<TimeForAssignment>> response) {
                            if(response.isSuccessful() && response.code()==200) {
                                //Toast.makeText(AssignmentDetailsActivity.this, "getting durations success", Toast.LENGTH_LONG).show();
                                time_for_assignment_list.addAll(response.body());

                                Intent intent = new Intent(AssignmentDetailsActivity.this, ScheduleDetailsActivity.class);
                                intent.putExtra("schedule", time_for_assignment_list.get(position));
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(AssignmentDetailsActivity.this, "getting durations failed " + response.code(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<TimeForAssignment>> call, Throwable t) {
                            Toast.makeText(AssignmentDetailsActivity.this, "Not connected to server", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        setListViewHeightBasedOnChildren(listView1);
        setListViewHeightBasedOnChildren(listView2);

        collapsingToolbarLayout.setTitle(assignment.getCourseName());
        collapsingToolbarLayout.setSubtitle(assignment.getName());

        // TODO: Set appropriate color.
        int backgroundColor = ContextCompat.getColor(this, R.color.colorAccent);
        collapsingToolbarLayout.setContentScrimColor(backgroundColor);
        collapsingToolbarLayout.setBackgroundColor(backgroundColor);

        TextView due = findViewById(R.id.textView_due);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, dd MMMM yyyy");
        due.setText(sdf.format(assignment.getDue().getTime()));

        TextView estimatedTime = findViewById(R.id.textView_estimated_time);
        estimatedTime.setText(assignment.getTimeEstimation() + " hours");

        TextView estimatedTimeDesc = findViewById(R.id.textView_estimated_time_description);
        estimatedTimeDesc.setText("Other students estimated " + String.valueOf(assignment.getAverageTimeEstimation()) + " hours");

        TextView description = findViewById(R.id.textView_description);
        description.setText(assignment.getDescription());

        RatingBar ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setRating(assignment.getSignificance());
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, final float rating, boolean fromUser) {
                OrzApi api = ApplicationController.getInstance().getApi();
                User user = ApplicationController.getInstance().getUser();
                Map<String, Object> body = new HashMap<>();
                body.put("timeEstimation", String.valueOf(assignment.getTimeEstimation()));
                body.put("significance", String.valueOf((int) rating));
                body.put("alarms", assignment.getAlarms());
                Call<Void> call = api.updateStudentAssignment(user.getID(), assignment.getID(), body);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful() && response.code()==200) {
                            assignment.setSignificance((int) rating);
                            Toast.makeText(AssignmentDetailsActivity.this, "rating change success", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(AssignmentDetailsActivity.this, "rating change failed " + response.code(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(AssignmentDetailsActivity.this, "Not connected to server", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
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
                        body.put("timeEstimation", String.valueOf(assignment.getTimeEstimation()));
                        body.put("significance", String.valueOf(assignment.getSignificance()));
                        assignment.addAlarm(times_int[lw.getCheckedItemPosition()]);
                        Collections.sort(assignment.getAlarms());
                        body.put("alarms", assignment.getAlarms());
                        Call<Void> call = api.updateStudentAssignment(user.getID(), assignment.getID(), body);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if(response.isSuccessful() && response.code()==200) {
                                    Toast.makeText(AssignmentDetailsActivity.this, "alarm set success", Toast.LENGTH_LONG).show();
                                    notification_time.add(notification_time.size() - 1, times[lw.getCheckedItemPosition()]);
                                    adapter1.notifyDataSetChanged();
                                    setListViewHeightBasedOnChildren((ListView) findViewById(R.id.listView_notification));
                                }
                                else {
                                    Toast.makeText(AssignmentDetailsActivity.this, "alarm set failed " + response.code(), Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(AssignmentDetailsActivity.this, "Not connected to server", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
        adb.show();
    }

    public void selectDuration() {
        final AlertDialog.Builder adb = new AlertDialog.Builder(this);
        final DatePicker dp1 = new DatePicker(this);
        final TimePicker tp1 = new TimePicker(this);
        final DatePicker dp2 = new DatePicker(this);
        final TimePicker tp2 = new TimePicker(this);

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
                                        adb.setPositiveButton("set",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //Toast.makeText(getApplicationContext(), "test3", Toast.LENGTH_LONG).show();
                                                        adb.setPositiveButton("set",
                                                                new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        //Toast.makeText(getApplicationContext(), "test4", Toast.LENGTH_LONG).show();
                                                                        Calendar startCalendar = Calendar.getInstance();
                                                                        startCalendar.set(dp1.getYear(), dp1.getMonth(), dp1.getDayOfMonth(), tp1.getCurrentHour(), tp1.getCurrentMinute());
                                                                        final Date startTime = startCalendar.getTime();

                                                                        Calendar endCalendar = Calendar.getInstance();
                                                                        endCalendar.set(dp2.getYear(), dp2.getMonth(), dp2.getDayOfMonth(), tp2.getCurrentHour(), tp2.getCurrentMinute());
                                                                        final Date endTime = endCalendar.getTime();

                                                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.KOREA);

                                                                        OrzApi api = ApplicationController.getInstance().getApi();
                                                                        User user = ApplicationController.getInstance().getUser();
                                                                        Map<String, Object> body = new HashMap<String, Object>();
                                                                        body.put("start", sdf.format(startTime));
                                                                        body.put("end", sdf.format(endTime));
                                                                        body.put("alarms", new ArrayList<Integer>());
                                                                        Call<Void> call = api.addTimeForAssignment(user.getID(), assignment.getID(), body);
                                                                        call.enqueue(new Callback<Void>() {
                                                                            @Override
                                                                            public void onResponse(Call<Void> call, Response<Void> response) {
                                                                                if(response.isSuccessful() && response.code()==200) {
                                                                                    Toast.makeText(AssignmentDetailsActivity.this, "adding duration success", Toast.LENGTH_LONG).show();
                                                                                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, dd MMMM yyyy");
                                                                                    time_for_assignment.add(time_for_assignment.size() - 1, sdf.format(startTime) + "\n" + sdf.format(endTime));
                                                                                    adapter2.notifyDataSetChanged();
                                                                                    setListViewHeightBasedOnChildren((ListView) findViewById(R.id.listView_time_for_assignment));
                                                                                }
                                                                                else {
                                                                                    Toast.makeText(AssignmentDetailsActivity.this, "adding duration failed " + response.code(), Toast.LENGTH_LONG).show();
                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onFailure(Call<Void> call, Throwable t) {
                                                                                Toast.makeText(AssignmentDetailsActivity.this, "Not connected to server", Toast.LENGTH_LONG).show();
                                                                            }
                                                                        });

                                                                        //Toast.makeText(getApplicationContext(), String.valueOf(dp1.getYear()) + "-" + String.valueOf(dp1.getMonth() + 1) + "-" + String.valueOf(dp1.getDayOfMonth()) + "\n" + String.valueOf(tp1.getCurrentHour()) + ":" + String.valueOf(tp1.getCurrentMinute()) + "\n" + String.valueOf(dp2.getYear()) + "-" + String.valueOf(dp2.getMonth() + 1) + "-" + String.valueOf(dp2.getDayOfMonth()) + "\n" + String.valueOf(tp2.getCurrentHour()) + ":" + String.valueOf(tp2.getCurrentMinute()), Toast.LENGTH_LONG).show();
                                                                    }
                                                                });
                                                        adb.setNegativeButton("cancel", null);
                                                        adb.setView(tp2);
                                                        adb.show();
                                                    }
                                                });
                                        adb.setNegativeButton("cancel", null);
                                        adb.setView(dp2);
                                        adb.show();
                                    }
                                });
                        adb.setNegativeButton("cancel", null);
                        adb.setView(tp1);
                        adb.show();
                    }
                });
        adb.setNegativeButton("cancel", null);
        adb.setView(dp1);
        adb.show();
    }

    public void selectTime() {
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

    public void enter_expected_time(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("enter expected time");

        final EditText time = new EditText(this);
        time.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(time);

        alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                OrzApi api = ApplicationController.getInstance().getApi();
                User user = ApplicationController.getInstance().getUser();
                Map<String, Object> body = new HashMap<>();
                body.put("timeEstimation", time.getText().toString());
                body.put("significance", String.valueOf(assignment.getSignificance()));
                body.put("alarms", assignment.getAlarms());
                Call<Void> call = api.updateStudentAssignment(user.getID(), assignment.getID(), body);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful() && response.code()==200) {
                            assignment.setTimeEstimation(Integer.valueOf(time.getText().toString()));
                            Toast.makeText(AssignmentDetailsActivity.this, "expected time set success", Toast.LENGTH_LONG).show();
                            TextView estimatedTime = findViewById(R.id.textView_estimated_time);
                            estimatedTime.setText(time.getText().toString() + " hours");
                        }
                        else {
                            Toast.makeText(AssignmentDetailsActivity.this, "expected time set failed " + response.code(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(AssignmentDetailsActivity.this, "Not connected to server", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        alert.setNegativeButton("cancel", null);

        alert.show();
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
