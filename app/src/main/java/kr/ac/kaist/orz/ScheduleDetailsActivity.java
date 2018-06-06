package kr.ac.kaist.orz;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ScheduleDetailsActivity extends AppCompatActivity {

    List<String> notification_time = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_details);

        notification_time.add("5 minute before (Default)");
        notification_time.add("10 minute before");
        notification_time.add("Add more notification");

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
        setListViewHeightBasedOnChildren(listview1);

        Button start_time = findViewById(R.id.button_start_time);
        Button end_time = findViewById(R.id.button_end_time);

        start_time.setText("7:00 PM, April 5th");
        end_time.setText("11:00 PM, April 5th");
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
