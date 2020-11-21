package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.application.BasicApp;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.entity.Attendance;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.entity.Classroom;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.repository.AttendanceRepository;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.repository.ClassroomRepository;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.repository.UserRepository;

public class ClassroomInfoViewModel extends AndroidViewModel {

    private static ClassroomInfoViewModel instance;

    private AttendanceRepository attendanceRepository;
    protected ClassroomRepository classroomRepository;

    public static ClassroomInfoViewModel getInstance(Application application) {
        if (instance == null) {
            instance = new ClassroomInfoViewModel(application);
        }
        return instance;
    }

    public ClassroomInfoViewModel(Application application) {
        super(application);
        attendanceRepository = BasicApp.getAttendanceRepository();
        classroomRepository = BasicApp.getClassroomRepository();
    }

    public void attendClass(int attendanceId, int presentCount, int classId) {
        attendanceRepository.updateToPresent(classId);
        classroomRepository.addPresentCount(presentCount, classId);
        classroomRepository.updateLastAttendanceId(attendanceId + 1, classId);
        int maxId = attendanceRepository.getMaxId();
        if (maxId == attendanceId) {
            classroomRepository.updateLastAttendanceId(attendanceId, classId);
        } else {
            classroomRepository.updateLastAttendanceId(attendanceId + 1, classId);
        }
    }

    public void absentClass(int attendanceId, int absentCount, int classId) {
        attendanceRepository.updateToAbsent(classId);
        classroomRepository.addAbsentCount(absentCount, classId);
        int maxId = attendanceRepository.getMaxId();
        if (maxId == attendanceId) {
            classroomRepository.updateLastAttendanceId(attendanceId, classId);
        } else {
            classroomRepository.updateLastAttendanceId(attendanceId + 1, classId);
        }
    }
}
