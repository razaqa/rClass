package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.R;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.entity.Classroom;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.receiver.BatteryBroadcastReceiver;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.viewmodel.ClassroomRegistrationViewModel;

@SuppressLint("NonConstantResourceId")
public class ClassroomRegistrationActivity extends AppCompatActivity {

    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private ClassroomRegistrationViewModel classroomRegistrationViewModel;
    private BroadcastReceiver broadcastReceiver;

    @BindView(R.id.editTextClassroomName) EditText editTextClassroomName;
    @BindView(R.id.editTextClassroomStartDate) EditText editTextClassroomStartDate;
    @BindView(R.id.editTextClassroomEndDate) EditText editTextClassroomEndDate;
    @BindView(R.id.editTextClassroomSchedule) EditText editTextClassroomSchedule;
    @BindString(R.string.low_battery) String lowBatteryTextTitle;
    @BindString(R.string.low_battery_warning) String lowBatteryWarningText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_classroom_registration);
        ButterKnife.bind(this);

        classroomRegistrationViewModel = new ViewModelProvider(this)
                .get(ClassroomRegistrationViewModel.class);
        broadcastReceiver = new BatteryBroadcastReceiver(lowBatteryTextTitle, lowBatteryWarningText);
    }

    public void onButtonCreateClassroomClicked(View view) {
        createClassroom();
        Intent intent = new Intent(ClassroomRegistrationActivity.this, DashboardActivity.class);
        startActivity(intent);
    }

    protected void createClassroom() {
        String nameStr = editTextClassroomName.getText().toString();
        String startDateStr = editTextClassroomStartDate.getText().toString();
        String endDateStr = editTextClassroomEndDate.getText().toString();
        String scheduleStr = editTextClassroomSchedule.getText().toString();

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        Date startDate, endDate;

        try {
            startDate = formatter.parse(startDateStr);
            endDate = formatter.parse(endDateStr);

            Classroom classroom = new Classroom(nameStr, startDate, endDate, scheduleStr);
            classroomRegistrationViewModel.insert(classroom);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(ClassroomRegistrationActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
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
