package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.application.BasicApp;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.entity.Classroom;

public class ClassroomListViewModel extends DashboardViewModel {

    private static ClassroomListViewModel instance;

    private LiveData<List<Classroom>> classrooms;

    public static ClassroomListViewModel getInstance(Application application) {
        if (instance == null) {
            instance = new ClassroomListViewModel(application);
        }
        return instance;
    }

    public ClassroomListViewModel(Application application) {
        super(application);
        classroomRepository = BasicApp.getClassroomRepository();
        classrooms = classroomRepository.getAllClassrooms();
    }

    public LiveData<List<Classroom>> getClassrooms() {
        return classrooms;
    }

}
