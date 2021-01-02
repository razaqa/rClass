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
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.R;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.activity.LoginActivity;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.api.WeatherAPIResponse;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.api.WeatherClientAPI;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.application.BasicApp;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.entity.Attendance;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.entity.Classroom;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.manager.ConnectivityCheck;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class NotificationService extends Service {

    private static final String NOTIFICATION_CHANNEL_ID = "rclass";
    private static final String NOTIFICATION_CHANNEL_NAME = "rClass Reminder";
    private static final String NOTIFICATION_CHANNEL_DESC = "Courses that you have to attend";
    private static final String NOTIFICATION_NAME = "rClass";
    private static final String NOTIFICATION_TYPE = "Information";
    private static final String NOTIFICATION_WEATHER_TIME = "12:06";

    private static final String SOURCE_URL = "https://data.bmkg.go.id/";

    private static final String WEATHER_CITY = "Depok";
    private static final String WEATHER_TYPE = "weather";
    private static final String WEATHER_HOUR = "30";

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
    public void notificationDialog(String title, String description) {
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

        protected void checkCourse(String dNowStr) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            List<Attendance> attendances = BasicApp.getAttendanceRepository().getAllAttendances();

            for (Attendance attendance : attendances) {
                Classroom classroom = BasicApp.getClassroomRepository().getClassroomById(attendance.getClass_id());
                String startDate = formatter.format(attendance.getStartHour());
                if (startDate.equals(dNowStr)) {
                    String title = classroom.getName() + " " + getResources().getString(R.string.notif_course_title);
                    String description = getResources().getString(R.string.notif_course_desc);
                    notificationDialog(title, description);
                }
            }
        }

        protected WeatherClientAPI connentToServer() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(SOURCE_URL)
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .build();

            return retrofit.create(WeatherClientAPI.class);
        }

        protected void notifyWeather() {
            WeatherClientAPI api = connentToServer();
            Call<WeatherAPIResponse> request = api.getWeather();

            request.enqueue(new Callback<WeatherAPIResponse>() {
                @Override
                public void onResponse(Call<WeatherAPIResponse> call, Response<WeatherAPIResponse> response) {
                    try {
                        String depokMorningWeather = response.body()
                                .getWeatherList()
                                .stream()
                                .filter(area -> area.getDescription().equalsIgnoreCase(WEATHER_CITY))
                                .findAny()
                                .orElse(null)
                                .getParameterList()
                                .stream()
                                .filter(parameter -> parameter.getId().equalsIgnoreCase(WEATHER_TYPE))
                                .findAny()
                                .orElse(null)
                                .getTimerangeList()
                                .stream()
                                .filter(timerange -> timerange.getH().equalsIgnoreCase(WEATHER_HOUR))
                                .findAny()
                                .orElse(null)
                                .getValue();

                        depokMorningWeather = decodeWeatherStatus(depokMorningWeather);

                        String title = getResources()
                                .getString(R.string.notif_weather_title) + " " + depokMorningWeather;
                        String description = getResources()
                                .getString(R.string.notif_weather_desc);

                        notificationDialog(title, description);

                    } catch (Exception e) {
                        Log.d("Wather Response Error:", e.toString());
                    }
                }
                @Override
                public void onFailure(Call<WeatherAPIResponse> call, Throwable t) {
                    Log.d("Wather Response Error:", t.toString());
                }
            });
        }

        protected void notifyWeatherWhenNoInternet() {
            String title = getResources()
                    .getString(R.string.notif_weather_title) + " " +
                    getResources().getString(R.string.weather_null);
            String description = getResources()
                    .getString(R.string.notif_not_connected);

            notificationDialog(title, description);
        }

        protected String decodeWeatherStatus(String value) {
            String result;
            switch (value) {
                case "0":
                    result = getResources().getString(R.string.weather_0);
                    break;
                case "1":
                    result = getResources().getString(R.string.weather_1);
                    break;
                case "2":
                    result = getResources().getString(R.string.weather_2);
                    break;
                case "3":
                    result = getResources().getString(R.string.weather_3);
                    break;
                case "4":
                    result = getResources().getString(R.string.weather_4);
                    break;
                case "5":
                    result = getResources().getString(R.string.weather_5);
                    break;
                case "10":
                    result = getResources().getString(R.string.weather_10);
                    break;
                case "45":
                    result = getResources().getString(R.string.weather_45);
                    break;
                case "60":
                    result = getResources().getString(R.string.weather_60);
                    break;
                case "61":
                    result = getResources().getString(R.string.weather_61);
                    break;
                case "63":
                    result = getResources().getString(R.string.weather_63);
                    break;
                case "80":
                    result = getResources().getString(R.string.weather_80);
                    break;
                case "95":
                    result = getResources().getString(R.string.weather_95);
                    break;
                case "97":
                    result = getResources().getString(R.string.weather_97);
                    break;
                case "100":
                    result = getResources().getString(R.string.weather_100);
                    break;
                case "101":
                    result = getResources().getString(R.string.weather_101);
                    break;
                case "102":
                    result = getResources().getString(R.string.weather_102);
                    break;
                case "103":
                    result = getResources().getString(R.string.weather_103);
                    break;
                case "104":
                    result = getResources().getString(R.string.weather_104);
                    break;
                default:
                    result = getResources().getString(R.string.weather_null);
                    break;
            }
            return result;
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected Void doInBackground(String... types) {
            while (running) {
                Date dNow = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                String dNowStr = formatter.format(dNow);

                String temp = new SimpleDateFormat("mm").format(dNow);
                String dNowHourStr = new SimpleDateFormat("HH:mm").format(dNow);

                if (! temp.equalsIgnoreCase(minute)) {
                    minute = temp;
                    checkCourse(dNowStr);
                    if (dNowHourStr.equals(NOTIFICATION_WEATHER_TIME)) {
                        boolean isConnected = ConnectivityCheck.isNetworkConnected(getApplicationContext());
                        if (isConnected) {
                            notifyWeather();
                        } else {
                            notifyWeatherWhenNoInternet();
                        }
                    }
                }
            }
            return null;
        }
    }
}
