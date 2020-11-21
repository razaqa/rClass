package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.application.BasicApp;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.entity.Classroom;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.repository.ClassroomRepository;

public class ClassroomListViewModel extends AndroidViewModel {

    private static ClassroomListViewModel instance;
    protected ClassroomRepository classroomRepository;

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
