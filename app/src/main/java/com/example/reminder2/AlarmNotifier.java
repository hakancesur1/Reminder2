package com.example.reminder2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
//import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
//import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;


public class AlarmNotifier extends BroadcastReceiver {

    public static final String MyPreferences = "MyPrefs";
    public static final String NotificationTypePreferences = "NotificationKey";
    //SharedPreferences sharedPreferences;

    private Db db;

    @Override
    public void onReceive(Context context, Intent intent) {

        db = new Db(context);
        //Calendar cal = Calendar.getInstance();
        // long reminderDate=intent.getLongExtra("ReminderDate",-1);
        //cal.setTimeInMillis(reminderDate);

        Notes notes = new Notes();
        // notes.setTitle(intent.getStringExtra("Message"));
        //notes.setTime(cal);
        //notes.setId(intent.getIntExtra("id",-1));
        int id = intent.getIntExtra("id", -1);
        notes = db.getNoteByID(id);
        //db.deleteReminderNote(notes);

        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String uriString = prefs.getString(NotificationTypePreferences, "");

        Uri alarmsound;
        if(!uriString.equalsIgnoreCase(""))
            alarmsound = Uri.parse(uriString);
        else
            alarmsound=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        //Uri alarmsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);



        Intent intent1 = new Intent(context, NewTask.class);
        intent1.putExtra("id", notes.getId());
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addParentStack(MainActivity.class);
        taskStackBuilder.addNextIntent(intent1);

        PendingIntent intent2 = taskStackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);

        MediaPlayer mp = MediaPlayer.create (context, alarmsound);
        mp.start();



        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel("my_channel_01", "hello", NotificationManager.IMPORTANCE_HIGH);
        }

        Notification notification = builder.setContentTitle("Reminder")
                .setContentText(notes.getTitle()).setAutoCancel(true)
                //.setSound(alarmsound)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(intent2)
                .setChannelId("my_channel_01")
                .addAction(R.drawable.ic_event_note_black_24dp, "Ertele",intent2)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(1, notification);
    }

}