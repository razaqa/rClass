package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
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
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel userViewModel;

    @BindView(R.id.textViewHello)
    TextView textViewHello;

    @BindView(R.id.editTextPersonName)
    EditText editTextPersonName;

    @BindView(R.id.buttonCreateAccount)
    Button buttonCreateAccount;

    @BindView(R.id.buttonLogin)
    Button buttonLogin;

    @BindString(R.string.greeting)
    String greetingText;

    @BindString(R.string.welcome_back)
    String welcomeBackText;

    @BindString(R.string.create_new_account)
    String createNewAccountText;

    @BindString(R.string.change_new_account)
    String changeNewAccountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        userViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        setUpLayoutBasedOnUser();
    }

    public void setUpLayoutBasedOnUser() {
        userViewModel.getUserCount().observe(this, count -> {
            if(count != 0) {
                userViewModel.getCurrentUser().observe(this, user -> {
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
        userViewModel.replace(new User(name));
    }
}