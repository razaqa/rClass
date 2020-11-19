package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.R;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.activity.ClassroomRegistrationActivity;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.activity.DashboardActivity;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.application.BasicApp;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.entity.Attendance;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.entity.Classroom;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.viewmodel.ClassroomInfoViewModel;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.viewmodel.DashboardViewModel;

public class ClassroomInfoFragment extends Fragment {

    public static final String CLASS_NAME = "ClassroomInfoFragment";

    private ClassroomInfoViewModel classroomInfoViewModel;
    private DashboardViewModel dashboardViewModel;
    private Classroom selectedClass;

    @BindView(R.id.textViewHeaderUsername)
    TextView textViewHeaderUsername;

    @BindView(R.id.textViewHeaderPercentage)
    TextView textViewHeaderPercentage;

    @BindView(R.id.textViewValueName)
    TextView textViewValueName;

    @BindView(R.id.textViewValueStartDate)
    TextView textViewValueStartDate;

    @BindView(R.id.textViewValueEndDate)
    TextView textViewValueEndDate;

    @BindView(R.id.textViewValuePresent)
    TextView textViewValuePresent;

    @BindView(R.id.textViewValueAbsent)
    TextView textViewValueAbsent;

    @BindView(R.id.textViewValuePercentage)
    TextView textViewValuePercentage;

    @BindView(R.id.textViewValueToday)
    TextView textViewValueToday;

    @BindView(R.id.textViewValueSchedule)
    TextView textViewValueSchedule;

    @BindString(R.string.present)
    String present;

    @BindString(R.string.absent)
    String absent;

    @BindString(R.string.not_started)
    String notStarted;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_classroom_info, container, false);
        ButterKnife.bind(this, view);

        classroomInfoViewModel = new ViewModelProvider(requireActivity()).get(ClassroomInfoViewModel.class);
        dashboardViewModel = DashboardViewModel.getInstance(BasicApp.getInstance());

        setUpContent();

        return view;
    }

    protected void setUpContent() {
        classroomInfoViewModel.getCurrentUser().observe(getActivity(), user -> {
            textViewHeaderUsername.setText(user.getName());
        });
        dashboardViewModel.getSelectedClassroom().observe(getActivity(), item -> {
            dashboardViewModel.loadClassroomInfo(item);
            dashboardViewModel.getClassroomInfo().observe(getActivity(), classroom -> {
                dashboardViewModel.loadAttendanceInfo(classroom.getLastAttendanceId());
                float presentCount = (float) classroom.getPresentCount();
                float absentCount = (float) classroom.getAbsentCount();
                float percentage = (presentCount * 100f) / (presentCount + absentCount);

                textViewValueName.setText(classroom.getName());
                textViewValuePresent.setText(String.format("%s", classroom.getPresentCount()));
                textViewValueAbsent.setText(String.format("%s", classroom.getAbsentCount()));
                textViewValuePercentage.setText(String.format("%s%%", percentage).equals("NaN%") ? "0%" : String.format("%s%%", percentage));
                textViewValueSchedule.setText(classroom.getSchedule());
                textViewHeaderPercentage.setText(String.format("%s%%", percentage).equals("NaN%") ? "0%" : String.format("%s%%", percentage));

                dashboardViewModel.getAttendanceInfo().observe(getActivity(), attendance -> {
                    SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMMM HH:mm");
                    String startDate = formatter.format(attendance.getStartHour());
                    String endDate = formatter.format(attendance.getEndHour());

                    String status = notStarted;
                    if (attendance.getIsPresent() != 0) {
                        status = attendance.getIsPresent() == 1 ? present : absent;
                    }

                    textViewValueStartDate.setText(startDate);
                    textViewValueEndDate.setText(endDate);
                    textViewValueToday.setText(status);

                });
            });
        });
    }

    public void attendClick() {
        dashboardViewModel.getClassroomInfo().observe(getActivity(), classroom -> {
            classroomInfoViewModel.attendClass(classroom.getLastAttendanceId(), classroom.getId());
            dashboardViewModel.getClassroomInfo().removeObservers(getActivity());
        });
    }

    public void absentClick() {
        dashboardViewModel.getClassroomInfo().observe(getActivity(), classroom -> {
            classroomInfoViewModel.absentClass(classroom.getLastAttendanceId(), classroom.getId());
            dashboardViewModel.getClassroomInfo().removeObservers(getActivity());
        });
    }
}
