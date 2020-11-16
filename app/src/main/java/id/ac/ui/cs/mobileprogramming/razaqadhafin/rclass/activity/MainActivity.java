package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.R;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.entity.User;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.viewmodel.UserViewModel;

public class MainActivity extends AppCompatActivity {

    private UserViewModel userViewModel;

    @BindView(R.id.textViewHello)
    TextView textViewHello;

    @BindView(R.id.editTextPersonName)
    EditText editTextPersonName;

    @BindView(R.id.buttonCreateAccount)
    Button buttonCreateAccount;

    @BindView(R.id.buttonLogin)
    Button buttonLogin;

    @BindString(R.string.hello)
    String helloText;

    @BindString(R.string.welcome)
    String welcomeText;

    @BindString(R.string.create_new_account)
    String createNewAccountText;

    @BindString(R.string.change_new_account)
    String changeNewAccountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        setUpLayoutBasedOnUser();
    }

    public void setUpLayoutBasedOnUser() {
        userViewModel.getUserCount().observe(this, count -> {
            if(count != 0) {
                userViewModel.getCurrentUser().observe(this, user -> {
                    textViewHello.setText(String.format("%s, %s!", welcomeText, user.getName()));
                });
                buttonCreateAccount.setText(changeNewAccountText);
                buttonLogin.setEnabled(true);
            } else {
                textViewHello.setText(helloText);
                buttonCreateAccount.setText(createNewAccountText);
                buttonLogin.setEnabled(false);
            }
        });
    }

    public void onButtonLoginClicked(View view) {
        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
        startActivity(intent);
    }

    public void onButtonChangeNewAccountClicked(View view) {
        replaceAccount();
        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
        startActivity(intent);
    }

    protected void replaceAccount() {
        String name = editTextPersonName.getText().toString();
        userViewModel.replace(new User(name));
    }
}