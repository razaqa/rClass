package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.application.BasicApp;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.dao.AttendanceDao;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.database.AppDatabase;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.entity.Attendance;

public class AttendanceRepository {

    private static AttendanceRepository instance;
    private static AttendanceDao attendanceDao;

    private LiveData<Attendance> selectedAttendance;
    private int maxId;

    public static AttendanceRepository getInstance(Application application) {
        if (instance == null) {
            instance = new AttendanceRepository(application);
        }
        return instance;
    }

    public AttendanceRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        attendanceDao = db.attendanceDao();
        BasicApp.getExecutors().diskIO().execute(() -> {
            maxId = attendanceDao.getMaxId();
        });
    }

    public LiveData<Attendance> getAttendanceById(int id) {
        BasicApp.getExecutors().diskIO().execute(() -> {
                selectedAttendance = attendanceDao.getAttendanceById(id);
        });
        return selectedAttendance;
    }

    public void insertAll(List<Attendance> attendances) {
        BasicApp.getExecutors().diskIO().execute(() -> {
            BasicApp.getDatabase().runInTransaction(() -> {
                attendanceDao.insertAll(attendances);
            });
        });
    }

    public void updateToPresent(int id) {
        BasicApp.getExecutors().diskIO().execute(() -> {
            BasicApp.getDatabase().runInTransaction(() -> {
                attendanceDao.updateIsPresent(1, id);
            });
        });
    }

    public void updateToAbsent(int id) {
        BasicApp.getExecutors().diskIO().execute(() -> {
            BasicApp.getDatabase().runInTransaction(() -> {
                attendanceDao.updateIsPresent(-1, id);
            });
        });
    }

    public int getMaxId() {
        BasicApp.getExecutors().diskIO().execute(() -> {
            maxId = attendanceDao.getMaxId();
        });
        return maxId;
    }
}
