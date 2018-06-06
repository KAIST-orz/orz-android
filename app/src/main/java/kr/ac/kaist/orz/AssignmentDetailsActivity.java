package kr.ac.kaist.orz;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AssignmentDetailsActivity extends AppCompatActivity {

    List<String> notification_time = new ArrayList<String>();
    List<String> time_for_assignment = new ArrayList<String>();

    static int e_time = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_details);

        notification_time.add("5 minute before (Default)");
        notification_time.add("10 minute before");
        notification_time.add("Add more notification");

        time_for_assignment.add("7:00PM ~ 11:00PM, April 5th");
        time_for_assignment.add("Add more time");

        final AlertDialog.Builder adb = new AlertDialog.Builder(this);

        ListView listview1 = findViewById(R.id.listView_notification);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notification_time);
        listview1.setAdapter(adapter);
        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        ListView listview2 = findViewById(R.id.listView_time_for_assignment);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, time_for_assignment);
        listview2.setAdapter(adapter);
        listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(time_for_assignment.get(position).equals("Add more time")) {
                    selectDuration();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), ScheduleDetailsActivity.class);
                    startActivity(intent);
                }
            }
        });

        setListViewHeightBasedOnChildren(listview1);
        setListViewHeightBasedOnChildren(listview2);

        TextView subject_assignment = findViewById(R.id.textView_subject_assignment);
        subject_assignment.setText("Computer Architecture\nHomework 4");

        Button due = findViewById(R.id.button_due);
        due.setText("11:59 PM, April 5th");
        due.setEnabled(false);

        Button estimated_time = findViewById(R.id.button_estimated_time);
        estimated_time.setText(String.valueOf(e_time) .concat(" hours\nOther students estimated 4.6 hours"));

        TextView description = findViewById(R.id.textView_description);
        description.setText("P. 3-11, 3-17, 3-19, 3-20, 3-21");
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
                                                                        Date startDate = startCalendar.getTime();

                                                                        Calendar endCalendar = Calendar.getInstance();
                                                                        startCalendar.set(dp2.getYear(), dp2.getMonth(), dp2.getDayOfMonth(), tp2.getCurrentHour(), tp2.getCurrentMinute());
                                                                        Date endDate = endCalendar.getTime();

                                                                        Toast.makeText(getApplicationContext(), String.valueOf(dp1.getYear()) + "-" + String.valueOf(dp1.getMonth() + 1) + "-" + String.valueOf(dp1.getDayOfMonth()) + "\n" + String.valueOf(tp1.getCurrentHour()) + ":" + String.valueOf(tp1.getCurrentMinute()) + "\n" + String.valueOf(dp2.getYear()) + "-" + String.valueOf(dp2.getMonth() + 1) + "-" + String.valueOf(dp2.getDayOfMonth()) + "\n" + String.valueOf(tp2.getCurrentHour()) + ":" + String.valueOf(tp2.getCurrentMinute()), Toast.LENGTH_LONG).show();
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
                Toast.makeText(getApplicationContext(), "time set to ".concat(time.getText().toString()).concat(String.valueOf(e_time)), Toast.LENGTH_LONG).show();
                e_time = Integer.parseInt(time.getText().toString());
                finish();
                startActivity(getIntent());
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
