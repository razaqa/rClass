package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "attendances")
public class Attendance {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "start_hour")
    private Date startHour;

    @ColumnInfo(name = "end_hour")
    private Date endHour;

    @ColumnInfo(name = "is_present")
    private int isPresent;

    @ForeignKey(entity = Classroom.class,
            parentColumns = "id",
            childColumns = "class_id",
            onDelete = CASCADE)
    private int class_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public Date getStartHour() {
        return startHour;
    }

    public void setStartHour(Date startDate) {
        this.startHour = startDate;
    }

    public Date getEndHour() {
        return endHour;
    }

    public void setEndHour(Date endDate) {
        this.endHour = endDate;
    }

    public int getIsPresent() {
        return isPresent;
    }

    public void setIsPresent(int isPresent) {
        this.isPresent = isPresent;
    }

    public Attendance() {
    }

    @Ignore
    public Attendance(Date startHour, Date endHour) {
        this.startHour = startHour;
        this.endHour = endHour;
        this.isPresent = 0;
    }
}
