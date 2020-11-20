package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.entity.Attendance;

@Dao
public interface AttendanceDao {

    @Query("SELECT MAX(id) FROM attendances")
    int getMaxId();

    @Query("SELECT * FROM attendances WHERE id = :id")
    LiveData<Attendance> getAttendanceById(int id);

    @Query("SELECT * FROM attendances")
    List<Attendance> getAllAttendances();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Attendance> attendances);

    @Query("UPDATE attendances SET is_present = :isPresent WHERE id = :id")
    void updateIsPresent(int isPresent, int id);
}
