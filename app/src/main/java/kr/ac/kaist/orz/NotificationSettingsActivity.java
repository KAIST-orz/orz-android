package kr.ac.kaist.orz;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class NotificationSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);
    }

    public void choose_personal_schedule_notification_time(View v) {
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

    public void choose_time_for_assignment_notification_time(View v) {
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

    public void choose_assignment_due_notification_time(View v) {
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
}
