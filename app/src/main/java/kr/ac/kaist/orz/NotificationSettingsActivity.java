package kr.ac.kaist.orz;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashMap;

import kr.ac.kaist.orz.models.Alarms;
import kr.ac.kaist.orz.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationSettingsActivity extends AppCompatActivity {
    int[] alarms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);

        OrzApi api = ApplicationController.getInstance().getApi();
        User user = ApplicationController.getInstance().getUser();

        Alarms a = ApplicationController.getInstance().getAlarms();
        alarms = new int[] {a.getAssignmentDueAlarm(), a.getPersonalScheduleAlarm(), a.getTimeForAssignmentAlarm()};
        updateText();
    }

    public void choose_personal_schedule_notification_time(View v) {
        selectAlarm(1);
    }

    public void choose_time_for_assignment_notification_time(View v) {
        selectAlarm(2);
    }

    public void choose_assignment_due_notification_time(View v) {
        selectAlarm(0);
    }

    public void selectAlarm(final int type) {
        final String[] times = new String[] {"1min", "3min", "5min", "10min", "15min", "30min", "1hour", "2hour", "3hour"};
        final Integer[] timesValue = new Integer[] {1, 3, 5, 10, 15, 30, 60, 120, 180};
        int currentValueIdx = Arrays.asList(timesValue).indexOf(alarms[type]);
        currentValueIdx = currentValueIdx==-1 ? 4 : currentValueIdx;
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("set time");
        adb.setSingleChoiceItems(times, currentValueIdx, null);
        adb.setPositiveButton("close",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListView lw = ((AlertDialog)dialog).getListView();
                        Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                        String checkedTime = checkedItem.toString();
                        int checkedTimeValue = timesValue[Arrays.asList(times).indexOf(checkedTime)];
                        Toast.makeText(getApplicationContext(), "time set to ".concat(checkedItem.toString()), Toast.LENGTH_LONG).show();
                        alarms[type] = checkedTimeValue;
                        alarmsUpdated();
                        updateText();
                    }
                });
        adb.show();
    }

    private void updateText() {
        final String[] times = new String[] {"1min", "3min", "5min", "10min", "15min", "30min", "1hour", "2hour", "3hour"};
        final Integer[] timesValue = new Integer[] {1, 3, 5, 10, 15, 30, 60, 120, 180};

        TextView assignmnetDueText = (TextView) findViewById(R.id.button_assignment_due);
        assignmnetDueText.setText(getResources().getText(R.string.notification_settings_assignment_due)+" : "+times[Arrays.asList(timesValue).indexOf(alarms[0])]);

        TextView personalScheduleText = (TextView) findViewById(R.id.button_personal_schedule);
        personalScheduleText.setText(getResources().getText(R.string.notification_settings_personal_schedule)+" : "+times[Arrays.asList(timesValue).indexOf(alarms[1])]);

        TextView timeForAssignmentText = (TextView) findViewById(R.id.button_time_for_assignment);
        timeForAssignmentText.setText(getResources().getText(R.string.notification_settings_time_for_assignment)+" : "+times[Arrays.asList(timesValue).indexOf(alarms[2])]);
    }

    private void alarmsUpdated() {
        Alarms a = ApplicationController.getInstance().getAlarms();
        a.setAssignmentDueAlarm(alarms[0]);
        a.setPersonalScheduleAlarm(alarms[1]);
        a.setTimeForAssignmentAlarm(alarms[2]);

        OrzApi api = ApplicationController.getInstance().getApi();
        User user = ApplicationController.getInstance().getUser();
        HashMap<String, String> body = new HashMap<>();
        body.put("assignmentDueAlarm", String.valueOf(alarms[0]));
        body.put("personalScheduleAlarm", String.valueOf(alarms[1]));
        body.put("timeForAssignmentAlarm", String.valueOf(alarms[2]));

        Call<Alarms> call = api.putStudentAlarms(user.getID(), body);
        call.enqueue(new Callback<Alarms>() {
            @Override
            public void onResponse(Call<Alarms> call, Response<Alarms> response) {
                if(response.isSuccessful()) {
                    Alarms a = response.body();
                    alarms = new int[] {a.getAssignmentDueAlarm(), a.getPersonalScheduleAlarm(), a.getTimeForAssignmentAlarm()};
                }
                else {
                    Log.e("orzApi", response.message());
                }
            }

            @Override
            public void onFailure(Call<Alarms> call, Throwable t) {
                Log.e("orzApi", t.getMessage());
            }
        });
    }
}
