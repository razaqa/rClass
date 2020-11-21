package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.R;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.contentprovider.CalendarContentProvider;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.contentprovider.CalendarEvent;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.entity.Attendance;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.entity.Classroom;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.viewmodel.ClassroomInfoViewModel;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.viewmodel.DashboardViewModel;

@SuppressLint("NonConstantResourceId")
public class ClassroomInfoFragment extends Fragment {

    public static final String CLASS_NAME = "ClassroomInfoFragment";

    private ClassroomInfoViewModel classroomInfoViewModel;
    private DashboardViewModel dashboardViewModel;

    private Classroom selectedClass;
    private Attendance selectedAttendance;

    class DataAdapter {
        String className;
        String classStartHour;
        String classEndHour;
        String classPresentCount;
        String classAbsentCount;
        String classPercentage;
        String classSchedule;
        String classTodayStatus;

        DataAdapter(String className,
                 Date classStartHour,
                 Date classEndHour,
                 int classPresentCount,
                 int classAbsentCount,
                 int classTodayStatus,
                 String classSchedule) {
            this.className = removeNullString(className);
            this.classStartHour = dateToString(classStartHour);
            this.classEndHour = dateToString(classEndHour);
            this.classPresentCount = Integer.toString(classPresentCount);
            this.classAbsentCount = Integer.toString(classAbsentCount);
            this.classPercentage = numbersToPercentage(classPresentCount, classAbsentCount);
            this.classTodayStatus = parseIntToStringStatus(classTodayStatus);
            this.classSchedule = removeNullString(classSchedule);
        }

        String dateToString(Date date) {
            if (date != null) {
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat df = new SimpleDateFormat("EEEE, dd MMMM HH:mm");
                return df.format(date);
            }
            return "-";
        }

        String numbersToPercentage(int x, int y) {
            float numerator = (float) x;
            float denominator = (float) x + (float) y;
            if (denominator == 0) {
                return "0%";
            }
            float percentage = (numerator * 100f) / (denominator);
            return percentage + "%";
        }

        String parseIntToStringStatus(int status) {
            String statusStr;
            switch (status) {
                case 1:
                    statusStr = present;
                    break;
                case -1:
                    statusStr = absent;
                    break;
                case 0:
                default:
                    statusStr = notStarted;
            }
            return statusStr;
        }

        String removeNullString(String text) {
            return text == null ? "-" : text;
        }

    }

    @BindView(R.id.textViewHeaderUsername) TextView textViewHeaderUsername;
    @BindView(R.id.textViewHeaderPercentage) TextView textViewHeaderPercentage;
    @BindView(R.id.textViewValueName) TextView textViewValueName;
    @BindView(R.id.textViewValueStartDate) TextView textViewValueStartDate;
    @BindView(R.id.textViewValueEndDate) TextView textViewValueEndDate;
    @BindView(R.id.textViewValuePresent) TextView textViewValuePresent;
    @BindView(R.id.textViewValueAbsent) TextView textViewValueAbsent;
    @BindView(R.id.textViewValuePercentage) TextView textViewValuePercentage;
    @BindView(R.id.textViewValueToday) TextView textViewValueToday;
    @BindView(R.id.textViewValueSchedule) TextView textViewValueSchedule;

    @BindString(R.string.present) String present;
    @BindString(R.string.absent) String absent;
    @BindString(R.string.not_started) String notStarted;
    @BindString(R.string.calendar_add_notification) String calendarAddNotification;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_classroom_info, container, false);
        ButterKnife.bind(this, view);

        classroomInfoViewModel = new ViewModelProvider(requireActivity()).get(ClassroomInfoViewModel.class);
        dashboardViewModel = new ViewModelProvider(requireActivity()).get(DashboardViewModel.class);

        setUpHeader();
        setUpContent();

        return view;
    }

    protected void setUpHeader() {
        dashboardViewModel.getCurrentUser().observe(Objects.requireNonNull(getActivity()),
                user -> textViewHeaderUsername.setText(String.format(" %s", user.getName())));
    }

    protected void setUpContent() {
        dashboardViewModel.getSelectedClassroom().observe(Objects.requireNonNull(getActivity()),
                classStr -> dashboardViewModel.loadClassroomInfo(classStr));

        Transformations.switchMap(dashboardViewModel.getClassroomInfo(), (classroom) -> {
                selectedClass = classroom;
                return dashboardViewModel.getAttendanceInfo(classroom.getId());
            }).observe(getActivity(), attendance -> {
                    selectedAttendance = attendance;

                    DataAdapter data = new DataAdapter(
                            selectedClass.getName(),
                            selectedAttendance.getStartHour(),
                            selectedAttendance.getEndHour(),
                            selectedClass.getPresentCount(),
                            selectedClass.getAbsentCount(),
                            selectedAttendance.getIsPresent(),
                            selectedClass.getSchedule()
                    );
                    setView(data);
            });
    }

    protected void setView(DataAdapter pageInfo) {
        textViewValueName.setText(pageInfo.className);
        textViewValueStartDate.setText(pageInfo.classStartHour);
        textViewValueEndDate.setText(pageInfo.classEndHour);
        textViewValuePresent.setText(pageInfo.classPresentCount);
        textViewValueAbsent.setText(pageInfo.classAbsentCount);
        textViewValuePercentage.setText(pageInfo.classPercentage);
        textViewValueToday.setText(pageInfo.classTodayStatus);
        textViewValueSchedule.setText(pageInfo.classSchedule);

        textViewHeaderPercentage.setText(pageInfo.classPercentage);
    }

    public void attendClick() {
        classroomInfoViewModel.attendClass(selectedClass.getLastAttendanceId(), selectedClass.getId());
    }

    public void absentClick() {
        classroomInfoViewModel.absentClass(selectedClass.getLastAttendanceId(), selectedClass.getId());
    }

    public void syncWithCalendar() {
        String LOCATION = "Faculty of Computer Science UI";

        CalendarContentProvider provider = new CalendarContentProvider(Objects.requireNonNull(getActivity()));
        CalendarEvent event = new CalendarEvent(
                selectedClass.getName(),
                selectedClass.getName(),
                selectedAttendance.getStartHour(),
                selectedAttendance.getEndHour(),
                LOCATION,
                false
        );
        provider.addEvent(event);
        Toast.makeText(getActivity(), calendarAddNotification, Toast.LENGTH_SHORT).show();
    }
}
