package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.application.BasicApp;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.dao.UserDao;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.database.AppDatabase;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.entity.User;

public class UserRepository {

    private static UserRepository instance;
    private static UserDao userDao;
    private LiveData<User> currentUser;
    private LiveData<Integer> userCount;

    public static UserRepository getInstance(Application application) {
        if (instance == null) {
            instance = new UserRepository(application);
        }
        return instance;
    }

    public UserRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        userDao = db.userDao();
        BasicApp.getExecutors().diskIO().execute(() -> {
            userCount = userDao.getUsersCount();
        });
        BasicApp.getExecutors().diskIO().execute(() -> {
            currentUser = userDao.getSomeUsers(1);
        });
    }

    public LiveData<User> getCurrentUser() {
        BasicApp.getExecutors().diskIO().execute(() -> {
            currentUser = userDao.getSomeUsers(1);
        });
        return currentUser;
    }

    public LiveData<Integer> getUserCount() {
        BasicApp.getExecutors().diskIO().execute(() -> {
            userCount = userDao.getUsersCount();
        });
        return userCount;
    }

    public void insert(User user) {
        BasicApp.getExecutors().diskIO().execute(() -> {
            userDao.insert(user);
        });
    }

    public void deleteAll() {
        BasicApp.getExecutors().diskIO().execute(() -> {
            BasicApp.getDatabase().runInTransaction(() -> {
                userDao.deleteAll();
            });
        });
    }

}
