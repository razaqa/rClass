package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.application;

import android.app.Application;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.database.AppDatabase;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.database.AppExecutors;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.repository.AttendanceRepository;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.repository.ClassroomRepository;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.repository.UserRepository;
import okhttp3.OkHttpClient;

public class BasicApp extends Application {

    private static BasicApp instance;
    private static AppExecutors executors;
    private static AppDatabase database;
    private static UserRepository userRepository;
    private static ClassroomRepository classroomRepository;
    private static AttendanceRepository attendanceRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        executors = new AppExecutors();
        database = AppDatabase.getInstance(this);

        userRepository = UserRepository.getInstance(this);
        classroomRepository = ClassroomRepository.getInstance(this);
        attendanceRepository = AttendanceRepository.getInstance(this);

        initStetho();
    }

    public static BasicApp getInstance() {
        if (instance == null) {
            instance = new BasicApp();
        }
        return instance;
    }

    public static AppDatabase getDatabase() {
        return database;
    }

    public static AppExecutors getExecutors() {
        return executors;
    }

    public static UserRepository getUserRepository() {
        return userRepository;
    }

    public static ClassroomRepository getClassroomRepository() {
        return classroomRepository;
    }

    public static AttendanceRepository getAttendanceRepository() {
        return attendanceRepository;
    }

    private void initStetho() {
        Stetho.initializeWithDefaults(this);
        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }
}
