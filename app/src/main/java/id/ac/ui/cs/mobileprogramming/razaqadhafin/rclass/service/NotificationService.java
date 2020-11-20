package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.R;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.activity.LoginActivity;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.application.BasicApp;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.entity.Attendance;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.entity.Classroom;

public class NotificationService extends Service {

    private static final String NOTIFICATION_CHANNEL_ID = "rclass";
    private static final String NOTIFICATION_CHANNEL_NAME = "rClass Reminder";
    private static final String NOTIFICATION_CHANNEL_DESC = "Courses that you have to attend";
    private static final String NOTIFICATION_NAME = "rClass";
    private static final String NOTIFICATION_TYPE = "Information";
    private boolean running = true;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new ScheduleCheckerAsyncTask().execute();
        running = true;
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        running = false;
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void notificationDialog(String title, String description) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant")
            NotificationChannel notificationChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_MAX);
            notificationChannel.setDescription(NOTIFICATION_CHANNEL_DESC);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Intent i = new Intent(this, LoginActivity.class);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                this, NOTIFICATION_CHANNEL_ID);
        PendingIntent notificationIntent = PendingIntent.getActivity(
                this, 999, i, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker(NOTIFICATION_NAME)
                .setContentTitle(title)
                .setContentText(description)
                .setContentIntent(notificationIntent)
                .setLargeIcon(BitmapFactory.decodeResource(
                        this.getResources(), R.mipmap.ic_razaaf_transparent))
                .setSmallIcon(R.mipmap.ic_razaaf_transparent)
                .setContentInfo(NOTIFICATION_TYPE);

        notificationManager.notify(1, notificationBuilder.build());
    }

    private class ScheduleCheckerAsyncTask extends AsyncTask<String, Void, Void> {
        private String minute = "";

        @SuppressLint("SimpleDateFormat")
        @Override
        protected Void doInBackground(String... types) {
            while (running) {
                Date dNow = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                String dNowStr = formatter.format(dNow);

                String temp = new SimpleDateFormat("mm").format(dNow);

                if (! temp.equalsIgnoreCase(minute)) {
                    minute = temp;
                    List<Attendance> attendances = BasicApp.getAttendanceRepository().getAllAttendances();
                    for (Attendance attendance : attendances) {
                        Classroom classroom = BasicApp.getClassroomRepository().getClassroomById(attendance.getClass_id());
                        String startDate = formatter.format(attendance.getStartHour());
                        if (startDate.equals(dNowStr)) {
                            String title = classroom.getName() + " " + getResources().getString(R.string.notif_title);
                            String description = getResources().getString(R.string.notif_desc);
                            notificationDialog(title, description);
                        }
                    }
                }
            }

            return null;
        }
    }
}
