package kr.ac.kaist.orz;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
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

        ListView listview1 = findViewById(R.id.listView_notification);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notification_time);
        listview1.setAdapter(adapter);
        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(notification_time.get(position).equals("Add more notification")) {
                    alarmSelector();
                }
            }
        });

        ListView listview2 = findViewById(R.id.listView_time_for_assignment);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, time_for_assignment);
        listview2.setAdapter(adapter);
        listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(notification_time.get(position).equals("Add more time")) {
                    dateSelector();
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

    public void alarmSelector() {
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

    public void dateSelector() {

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

        alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alert.show();
    }
}
