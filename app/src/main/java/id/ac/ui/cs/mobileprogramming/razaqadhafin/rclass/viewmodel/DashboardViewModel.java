package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.application.BasicApp;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.entity.Attendance;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.entity.Classroom;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.entity.User;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.repository.AttendanceRepository;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.repository.ClassroomRepository;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.repository.UserRepository;

public class DashboardViewModel extends AndroidViewModel {

    private static DashboardViewModel instance;

    protected ClassroomRepository classroomRepository;
    protected UserRepository userRepository;
    protected AttendanceRepository attendanceRepository;

    protected final MutableLiveData<String> selectedClassroom = new MutableLiveData<>();

    protected LiveData<User> currentUser;
    private LiveData<Classroom> classroom;
    private LiveData<Attendance> attendance;

    public static DashboardViewModel getInstance(Application application) {
        if (instance == null) {
            instance = new DashboardViewModel(application);
        }
        return instance;
    }

    public DashboardViewModel(Application application) {
        super(application);
        classroomRepository = BasicApp.getClassroomRepository();
        userRepository = BasicApp.getUserRepository();
        attendanceRepository = BasicApp.getAttendanceRepository();

        currentUser = userRepository.getCurrentUser();
    }

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }

    public String getHeaderPercentage() {
        float presentCount = (float) classroomRepository.getTotalPresentCount();
        float absentCount = (float) classroomRepository.getTotalAbsentCount();

        float denominator = presentCount + absentCount;

        if (denominator == 0) {
            return "0%";
        }

        float percentage = (presentCount * 100f) / denominator;
        return percentage + "%";
    }

    public LiveData<String> getSelectedClassroom() {
        return selectedClassroom;
    }

    public void selectClassroom(String className) {
        selectedClassroom.setValue(className);
    }

    public void loadClassroomInfo(String name) {
        classroom = classroomRepository.getClassroomByName(name);
    }

    public void loadAttendanceInfo(int id) {
        attendance = attendanceRepository.getAttendanceById(id);
    }

    public LiveData<Classroom> getClassroomInfo() {
        return classroom;
    }

    public LiveData<Attendance> getAttendanceInfo() {
        return attendance;
    }

    public LiveData<Attendance> getAttendanceInfo(int id) {
        attendance = attendanceRepository.getAttendanceById(id);
        return attendance;
    }

}
