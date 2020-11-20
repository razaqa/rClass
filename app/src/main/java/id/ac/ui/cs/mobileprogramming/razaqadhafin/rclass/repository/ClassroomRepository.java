package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.application.BasicApp;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.dao.ClassroomDao;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.database.AppDatabase;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.entity.Attendance;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.entity.Classroom;

public class ClassroomRepository {

    private static ClassroomRepository instance;
    private static ClassroomDao classroomDao;

    private LiveData<List<Classroom>> allClassrooms;
    private LiveData<Classroom> classroom;
    private int presentCount;
    private int absentCount;
    private int maxId;

    public static ClassroomRepository getInstance(Application application) {
        if (instance == null) {
            instance = new ClassroomRepository(application);
        }
        return instance;
    }

    public ClassroomRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        classroomDao = db.classroomDao();
        BasicApp.getExecutors().diskIO().execute(() -> {
            allClassrooms = classroomDao.getAllClassrooms();
        });
        BasicApp.getExecutors().diskIO().execute(() -> {
            maxId = classroomDao.getMaxId();
        });
    }

    public LiveData<List<Classroom>> getAllClassrooms() {
        BasicApp.getExecutors().diskIO().execute(() -> {
                allClassrooms = classroomDao.getAllClassrooms();
        });
        return allClassrooms;
    }

    public Classroom getClassroomById(int id) {
        return classroomDao.getClassroomById(id);
    }

    public void insert(Classroom classroom) {
        BasicApp.getExecutors().diskIO().execute(() -> {
            BasicApp.getDatabase().runInTransaction(() -> {
                int classId = getMaxId() + 1;
                classroomDao.insert(classroom);
                int lastAttendanceId = BasicApp.getAttendanceRepository().getMaxId() + 1;

                List<String> schedules = Arrays.asList(classroom.getSchedule().split(";"));
                List<Attendance> attendances = new ArrayList<>();

                Calendar start = Calendar.getInstance();
                start.setTime(classroom.getStartDate());
                Calendar end = Calendar.getInstance();
                end.setTime(classroom.getEndDate());

                for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {

                    SimpleDateFormat dayFormatter = new SimpleDateFormat("EEEE");
                    String startDayStr = dayFormatter.format(date);
                    String endDayStr = dayFormatter.format(date);

                    String dateFormat = "dd/MM/yyyy";
                    SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);
                    String startDateStr = dateFormatter.format(date);
                    String endDateStr = dateFormatter.format(date);

                    for (int j = 0; j < schedules.size(); j++) {
                        boolean isStartSameDay = schedules.get(j).contains(startDayStr);
                        boolean isEndSameDay = schedules.get(j).contains(endDayStr);

                        if (isStartSameDay && isEndSameDay) {
                            String startHour = schedules.get(j).split("-")[0].split(",")[1];
                            String endHour = schedules.get(j).split("-")[1].split(",")[1];

                            String startDateFinal = startDateStr + " " + startHour;
                            String endDateFinal = endDateStr + " " + endHour;

                            try {
                                Date startDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(startDateFinal);
                                Date endDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(endDateFinal);

                                Attendance attendance = new Attendance(startDate, endDate);
                                attendance.setClass_id(classId);
                                attendances.add(attendance);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
                BasicApp.getAttendanceRepository().insertAll(attendances);
                updateLastAttendanceId(lastAttendanceId, classId);
            });
        });
    }

    public LiveData<Classroom> getClassroomByName(String name) {
        BasicApp.getExecutors().diskIO().execute(() -> {
                classroom = classroomDao.getClassroomByName(name);
        });
        return classroom;
    }

    public int getTotalPresentCount() {
        BasicApp.getExecutors().diskIO().execute(() -> {
                presentCount = classroomDao.getTotalPresentCount();
        });
        return presentCount;
    }

    public int getTotalAbsentCount() {
        BasicApp.getExecutors().diskIO().execute(() -> {
                absentCount = classroomDao.getTotalAbsentCount();
        });
        return absentCount;
    }

    public void addPresentCount(int id) {
        BasicApp.getExecutors().diskIO().execute(() -> {
            BasicApp.getDatabase().runInTransaction(() -> {
                classroomDao.updatePresentCount(getTotalPresentCount() + 1, id);
            });
        });
    }

    public void addAbsentCount(int id) {
        BasicApp.getExecutors().diskIO().execute(() -> {
            BasicApp.getDatabase().runInTransaction(() -> {
                classroomDao.updateAbsentCount(getTotalAbsentCount() + 1, id);
            });
        });
    }

    public void updateLastAttendanceId(int newAttendanceId, int id) {
        BasicApp.getExecutors().diskIO().execute(() -> {
            BasicApp.getDatabase().runInTransaction(() -> {
                classroomDao.updateLastAttendanceId(newAttendanceId, id);
            });
        });
    }

    public int getMaxId() {
        BasicApp.getExecutors().diskIO().execute(() -> {
            maxId = classroomDao.getMaxId();
        });
        return maxId;
    }
}
