package Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.sololingo.MainActivity;
import com.example.sololingo.R;

public class ReminderBroadcast extends BroadcastReceiver {
    private static int ID = 1001;
    private static final String CHANNEL_ID = "sololingo";
    private static final String name = "sololingo";
    private static final String[] defaultNotification = {
            "Don't give up so soon! Hurry up and continue learning",
            "You can do it! Master your studies now~",
            "Sololingo time!",
            "If you can't help yourself, no one can."
    };

    private static final String[] notificationTitle = {
            "We need you!",
            "Start learning!",
            "Sharpen your language skills"
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_action_learn)
                .setContentTitle(intent.getStringExtra("title"))
                .setContentText(intent.getStringExtra("data"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(intent.getStringExtra("data")))
                .setWhen(when)
                .setShowWhen(true)
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(ID++, notification);
    }
}
