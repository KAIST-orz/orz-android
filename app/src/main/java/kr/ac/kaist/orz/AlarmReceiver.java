package kr.ac.kaist.orz;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");

        Intent intent1 = new Intent(context, AlarmIntentService.class);
        intent1.putExtra("title", title);
        intent1.putExtra("message", message);
        context.startService(intent1);
    }
}
