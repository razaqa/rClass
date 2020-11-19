package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "classrooms")
public class Classroom {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "end_date")
    private Date endDate;

    @ColumnInfo(name = "start_date")
    private Date startDate;

    @ColumnInfo(name = "present_count")
    private int presentCount;

    @ColumnInfo(name = "absent_count")
    private int absentCount;

    @ColumnInfo(name = "schedule")
    private String schedule;

    @ColumnInfo(name = "last_attendance_id")
    private int lastAttendanceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getPresentCount() {
        return presentCount;
    }

    public void setPresentCount(int presentCount) {
        this.presentCount = presentCount;
    }

    public int getAbsentCount() {
        return absentCount;
    }

    public void setAbsentCount(int absentCount) {
        this.absentCount = absentCount;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public int getLastAttendanceId() {
        return lastAttendanceId;
    }

    public void setLastAttendanceId(int lastAttendanceId) {
        this.lastAttendanceId = lastAttendanceId;
    }

    public Classroom() {
    }

    @Ignore
    public Classroom(String name, Date startDate, Date endDate, String schedule) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.presentCount = 0;
        this.absentCount = 0;
        this.schedule = schedule;
    }
}
