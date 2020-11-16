package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.application;

import android.app.Application;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.database.AppDatabase;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.database.AppExecutors;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.repository.UserRepository;
import okhttp3.OkHttpClient;

public class BasicApp extends Application {

    private static AppExecutors executors;
    private static AppDatabase database;
    private static UserRepository userRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        executors = new AppExecutors();
        database = AppDatabase.getInstance(this);
        userRepository = UserRepository.getInstance(this);

        initStetho();
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

    private void initStetho() {
        Stetho.initializeWithDefaults(this);
        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }
}
