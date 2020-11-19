package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.activity;

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

public class ClassroomRegistrationActivity extends AppCompatActivity {

    private ClassroomRegistrationViewModel classroomRegistrationViewModel;
    private BroadcastReceiver receiver;

    @BindView(R.id.editTextClassroomName)
    EditText editTextClassroomName;

    @BindView(R.id.editTextClassroomStartDate)
    EditText editTextClassroomStartDate;

    @BindView(R.id.editTextClassroomEndDate)
    EditText editTextClassroomEndDate;

    @BindView(R.id.editTextClassroomSchedule)
    EditText editTextClassroomSchedule;

    @BindString(R.string.low_battery)
    String lowBatteryTextTitle;

    @BindString(R.string.low_battery_warning)
    String lowBatteryWarningText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_classroom_registration);
        ButterKnife.bind(this);

        classroomRegistrationViewModel = new ViewModelProvider(this).get(ClassroomRegistrationViewModel.class);
        receiver = new BatteryBroadcastReceiver(lowBatteryTextTitle, lowBatteryWarningText);
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

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date startDate, endDate;
        try {
            startDate = formatter.parse(startDateStr);
            endDate = formatter.parse(endDateStr);
            classroomRegistrationViewModel.insert(new Classroom(nameStr, startDate, endDate, scheduleStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent moveback = new Intent(ClassroomRegistrationActivity.this, DashboardActivity.class);
        startActivity(moveback);
        finish();
    }

    @Override
    protected void onStart() {
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_BATTERY_LOW));
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(receiver);
        super.onStop();
    }
}
