package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import butterknife.BindString;
import butterknife.ButterKnife;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.R;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.fragment.ClassroomInfoFragment;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.fragment.ClassroomListFragment;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.receiver.BatteryBroadcastReceiver;

public class DashboardActivity extends AppCompatActivity {

    protected ClassroomListFragment classroomListFragment;
    protected ClassroomInfoFragment classroomInfoFragment;
    private BroadcastReceiver receiver;

    @BindString(R.string.low_battery)
    String lowBatteryTextTitle;

    @BindString(R.string.low_battery_warning)
    String lowBatteryWarningText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        receiver = new BatteryBroadcastReceiver(lowBatteryTextTitle, lowBatteryWarningText);

        if (savedInstanceState == null) {
            classroomListFragment = new ClassroomListFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activityDashboard, classroomListFragment, ClassroomListFragment.CLASS_NAME)
                    .commit();
        }
    }

    public void showClassroomInfoFragment() {
        classroomInfoFragment = new ClassroomInfoFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activityDashboard, classroomInfoFragment, ClassroomInfoFragment.CLASS_NAME)
                .addToBackStack(ClassroomInfoFragment.CLASS_NAME)
                .commit();
    }

    public void onButtonAddNewClassroomOnClicked(View view) {
        Intent intent = new Intent(this, ClassroomRegistrationActivity.class);
        startActivity(intent);
    }

    public void onButtonAttendClassClicked(View view) {
        classroomInfoFragment.attendClick();
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    public void onButtonAbsentClassClicked(View view) {
        classroomInfoFragment.absentClick();
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    public void onButtonSyncClicked(View view) {
        classroomInfoFragment.syncWithCalendar();
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(DashboardActivity.this, DashboardActivity.class);
        startActivity(intent);
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