package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.application.BasicApp;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.entity.User;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.repository.UserRepository;

public class LoginViewModel extends AndroidViewModel {

    private static LoginViewModel instance;
    private UserRepository userRepository;

    private LiveData<Integer> userCount;
    private LiveData<User> currentUser;

    public static LoginViewModel getInstance(Application application) {
        if (instance == null) {
            instance = new LoginViewModel(application);
        }
        return instance;
    }

    public LoginViewModel(Application application) {
        super(application);
        userRepository = BasicApp.getUserRepository();
        currentUser = userRepository.getCurrentUser();
        userCount = userRepository.getUserCount();
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
