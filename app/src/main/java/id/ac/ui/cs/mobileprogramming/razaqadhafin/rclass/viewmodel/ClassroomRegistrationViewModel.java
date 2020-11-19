package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.application.BasicApp;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.entity.Classroom;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.repository.ClassroomRepository;

public class ClassroomRegistrationViewModel extends AndroidViewModel {

    private static ClassroomRegistrationViewModel instance;

    private ClassroomRepository classroomRepository;

    public static ClassroomRegistrationViewModel getInstance(Application application) {
        if (instance == null) {
            instance = new ClassroomRegistrationViewModel(application);
        }
        return instance;
    }

    public ClassroomRegistrationViewModel(Application application) {
        super(application);
        classroomRepository = BasicApp.getClassroomRepository();
    }

    public void insert(Classroom classroom) {
        classroomRepository.insert(classroom);
    }

}
