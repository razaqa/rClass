package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.R;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.application.BasicApp;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.viewmodel.UserViewModel;

public class DashboardActivity extends AppCompatActivity {

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        BasicApp.getExecutors().diskIO().execute(() -> {
            userViewModel.getAllUsers().forEach((user) -> Log.d("users", user.getName()));
        });
    }
}