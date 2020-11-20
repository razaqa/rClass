package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.entity.Classroom;

@Dao
public interface ClassroomDao {

    @Query("SELECT MAX(id) FROM classrooms")
    int getMaxId();

    @Query("SELECT * FROM classrooms")
    LiveData<List<Classroom>> getAllClassrooms();

    @Query("SELECT * FROM classrooms where name = :name LIMIT 1")
    LiveData<Classroom> getClassroomByName(String name);

    @Query("SELECT * FROM classrooms where id = :id")
    Classroom getClassroomById(int id);

    @Query("SELECT SUM(present_count) FROM classrooms")
    int getTotalPresentCount();

    @Query("SELECT SUM(absent_count) FROM classrooms")
    int getTotalAbsentCount();

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Classroom classroom);

    @Query("UPDATE classrooms SET present_count = :presentCount WHERE id = :id")
    void updatePresentCount(int presentCount, int id);

    @Query("UPDATE classrooms SET absent_count = :absentCount WHERE id = :id")
    void updateAbsentCount(int absentCount, int id);

    @Query("UPDATE classrooms SET last_attendance_id = :last_attendance_id WHERE id = :id")
    void updateLastAttendanceId(int last_attendance_id, int id);
}
