package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.contentprovider;

import java.util.Date;

public class CalendarEvent {

    private String title;
    private Date startDate;
    private Date endDate;
    private String description;
    private String location;
    private boolean isAllDay;

    public CalendarEvent() {
    }

    public CalendarEvent(String title, String description, Date startDate, Date endDate, String location, boolean isAllDay) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.isAllDay = isAllDay;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isAllDay() {
        return isAllDay;
    }

    public void setAllDay(boolean allDay) {
        isAllDay = allDay;
    }
}
