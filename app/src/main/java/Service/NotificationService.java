package Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.example.sololingo.MainActivity;
import com.example.sololingo.R;

public class NotificationService extends Service {

    final static String ACTION = "NotifyServiceAction";
    final static String STOP_SERVICE = "";
    final static int RQS_STOP_SERVICE = 1;
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

    NotifyServiceReceiver notifyServiceReceiver;

    private static final int MY_NOTIFICATION_ID=1;
    private NotificationManager notificationManager;
    private Notification myNotification;

    @Override
    public void onCreate() {
// TODO Auto-generated method stub
        notifyServiceReceiver = new NotifyServiceReceiver();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION);
        registerReceiver(notifyServiceReceiver, intentFilter);

        //Get data for notification
        //Randomizer for notification type
        String contentTitle = notificationTitle[(int) (Math.random()*notificationTitle.length)];
        String data = "Hello";
        int randomInt = 0;
                //(int) (Math.random()*3);
        switch (randomInt){
            case 0: //Normal notification
                data = defaultNotification[(int) (Math.random()*defaultNotification.length)];
                break;
            case 1:
                //Memo notification
                break;
            case 2:
                //Flashcard notification
                break;
        }

        Intent i = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT |  PendingIntent.FLAG_IMMUTABLE);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_action_learn)
                .setContentTitle(contentTitle)
                .setContentText(data)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(data))
                .setShowWhen(true)
                .setWhen(System.currentTimeMillis()+60*1000)
                .build();

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }
        manager.notify(ID++, notification);

        return super.onStartCommand(intent, flags, startId);
    }

    public String[] getNotificationContent(){
        //Randomizer for notification type
        String contentTitle = notificationTitle[(int) (Math.random()*notificationTitle.length)];
        String data = "Hello";
        int randomInt = 0;
        //(int) (Math.random()*3);
        switch (randomInt){
            case 0: //Normal notification
                data = defaultNotification[(int) (Math.random()*defaultNotification.length)];
                break;
            case 1:
                //Memo notification
                break;
            case 2:
                //Flashcard notification
                break;
        }
        return new String[]{contentTitle, data};
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        this.unregisterReceiver(notifyServiceReceiver);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public class NotifyServiceReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            int rqs = arg1.getIntExtra("RQS", 0);
            if (rqs == RQS_STOP_SERVICE){
                stopSelf();
            }
        }
    }

}
