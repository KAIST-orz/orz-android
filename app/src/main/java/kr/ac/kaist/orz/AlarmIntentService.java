package kr.ac.kaist.orz;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.Calendar;

public class AlarmIntentService extends IntentService {
    private static int NOTIFICATION_ID = 0;

    public AlarmIntentService() {
        super("AlarmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "orz");
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder.setChannelId("orz");
        }

        Intent notifyIntent = new Intent(this, OrzMainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, 0);
        builder.setContentIntent(pendingIntent);

        Notification notificationCompat = builder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID++, notificationCompat);
    }
}
