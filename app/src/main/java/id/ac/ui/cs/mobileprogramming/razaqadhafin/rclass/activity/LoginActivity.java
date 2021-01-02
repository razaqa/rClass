package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    private static final int PERMISSION_CODE = 1;

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
    @BindString(R.string.request_permission) String requestPermissionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        System.loadLibrary("uppercase-word-jni");

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        broadcastReceiver = new BatteryBroadcastReceiver(lowBatteryTextTitle, lowBatteryWarningText);

        setUpLayoutBasedOnUser();
        stopService(new Intent(LoginActivity.this, NotificationService.class));
        startService(new Intent(LoginActivity.this, NotificationService.class));

        checkPermission();
    }

    public native String uppercaseStringFromJNI(String input);

    protected void setUpLayoutBasedOnUser() {
        loginViewModel.getUserCount().observe(this, count -> {
            if(count != 0) {
                loginViewModel.getCurrentUser().observe(this, user -> {
                    String capitalizedName = user.getName() != null ? uppercaseStringFromJNI(user.getName()) : "";
                    textViewHello.setText(String.format("%s, %s!", welcomeBackText, capitalizedName));
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

    protected void checkPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                    new String[] {
                            Manifest.permission.WRITE_CALENDAR,
                            Manifest.permission.READ_CALENDAR,
                            Manifest.permission.INTERNET,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.ACCESS_NETWORK_STATE
                    }, PERMISSION_CODE);
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (!(grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, requestPermissionText, Toast.LENGTH_SHORT).show();
            }
        }
    }
}