package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.application.BasicApp;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.dao.UserDao;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.database.AppDatabase;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.database.AppExecutors;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.entity.User;

public class UserRepository {

    private AppDatabase db;
    private static UserRepository instance;
    private static UserDao userDao;
    private List<User> allUsers;
    private LiveData<User> currentUser;
    private LiveData<Integer> userCount;

    public static UserRepository getInstance(Application application) {
        if (instance == null) {
            instance = new UserRepository(application);
        }
        return instance;
    }

    public UserRepository(Application application) {
        db = AppDatabase.getInstance(application);
        userDao = db.userDao();
        BasicApp.getExecutors().diskIO().execute(() -> {
            BasicApp.getDatabase().runInTransaction(() -> {
                userCount = userDao.getUsersCount();
            });
        });
        BasicApp.getExecutors().diskIO().execute(() -> {
            BasicApp.getDatabase().runInTransaction(() -> {
                currentUser = userDao.getSomeUsers(1);
            });
        });
        BasicApp.getExecutors().diskIO().execute(() -> {
            BasicApp.getDatabase().runInTransaction(() -> {
                allUsers = userDao.getAllUsers();
            });
        });
    }

    public List<User> getAllUsers() {
        return allUsers;
    }

    public LiveData<User> getCurrentUser() {
        BasicApp.getExecutors().diskIO().execute(() -> {
            BasicApp.getDatabase().runInTransaction(() -> {
                currentUser = userDao.getSomeUsers(1);
            });
        });
        return currentUser;
    }

    public LiveData<Integer> getUserCount() {
        BasicApp.getExecutors().diskIO().execute(() -> {
            BasicApp.getDatabase().runInTransaction(() -> {
                userCount = userDao.getUsersCount();
            });
        });
        return userCount;
    }

    public List<User> loadAllUsers() {
        BasicApp.getExecutors().diskIO().execute(() -> {
            BasicApp.getDatabase().runInTransaction(() -> {
                allUsers = db.userDao().getAllUsers();
            });
        });
        return allUsers;
    }

    public void insert(User user) {
        BasicApp.getExecutors().diskIO().execute(() -> {
            BasicApp.getDatabase().runInTransaction(() -> {
                userDao.insert(user);
            });
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
