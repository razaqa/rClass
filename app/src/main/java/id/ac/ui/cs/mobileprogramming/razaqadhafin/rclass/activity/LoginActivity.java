package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.R;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.entity.User;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.receiver.BatteryBroadcastReceiver;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.service.NotificationService;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.viewmodel.LoginViewModel;

@SuppressLint("NonConstantResourceId")
public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private BroadcastReceiver broadcastReceiver;

    @BindView(R.id.textViewHello) TextView textViewHello;
    @BindView(R.id.editTextPersonName) EditText editTextPersonName;
    @BindView(R.id.buttonCreateAccount) Button buttonCreateAccount;
    @BindView(R.id.buttonLogin) Button buttonLogin;

    @BindString(R.string.greeting) String greetingText;
    @BindString(R.string.welcome_back) String welcomeBackText;
    @BindString(R.string.create_new_account) String createNewAccountText;
    @BindString(R.string.change_new_account) String changeNewAccountText;
    @BindString(R.string.low_battery) String lowBatteryTextTitle;
    @BindString(R.string.low_battery_warning) String lowBatteryWarningText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        broadcastReceiver = new BatteryBroadcastReceiver(lowBatteryTextTitle, lowBatteryWarningText);

        setUpLayoutBasedOnUser();
        stopService(new Intent(LoginActivity.this, NotificationService.class));
        startService(new Intent(LoginActivity.this, NotificationService.class));
    }

    public void setUpLayoutBasedOnUser() {
        loginViewModel.getUserCount().observe(this, count -> {
            if(count != 0) {
                loginViewModel.getCurrentUser().observe(this, user -> {
                    textViewHello.setText(String.format("%s, %s!", welcomeBackText, user.getName()));
                    editTextPersonName.setText(user.getName());
                });
                buttonCreateAccount.setText(changeNewAccountText);
                buttonLogin.setEnabled(true);
            } else {
                textViewHello.setText(greetingText);
                buttonCreateAccount.setText(createNewAccountText);
                buttonLogin.setEnabled(false);
            }
        });
    }

    public void onButtonLoginClicked(View view) {
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(intent);
    }

    public void onButtonChangeNewAccountClicked(View view) {
        replaceAccount();
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(intent);
    }

    protected void replaceAccount() {
        String name = editTextPersonName.getText().toString();
        loginViewModel.replace(new User(name));
    }
    @Override
    protected void onStart() {
        registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_LOW));
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(broadcastReceiver);
        super.onStop();
    }
}