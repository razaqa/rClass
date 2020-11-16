package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.application.BasicApp;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.database.AppDatabase;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.entity.User;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private static UserViewModel instance;

    private LiveData<Integer> userCount;
    private LiveData<User> currentUser;
    private UserRepository userRepository;

    public static UserViewModel getInstance(Application application) {
        if (instance == null) {
            instance = new UserViewModel(application);
        }
        return instance;
    }

    public UserViewModel (Application application) {
        super(application);
        userRepository = BasicApp.getUserRepository();
        currentUser = userRepository.getCurrentUser();
        userCount = userRepository.getUserCount();
    }

    public List<User> getAllUsers() {
        return userRepository.loadAllUsers();
    }

    public LiveData<Integer> getUserCount() {
        return userRepository.getUserCount();
    }

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }

    public void insert(User user) {
        userRepository.insert(user);
    }

    public void replace(User newUser) {
        userRepository.deleteAll();
        userRepository.insert(newUser);
    }
}
