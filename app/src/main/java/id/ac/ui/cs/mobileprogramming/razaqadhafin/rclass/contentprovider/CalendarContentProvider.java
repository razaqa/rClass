package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.contentprovider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CalendarContentProvider {

    private ContentResolver cr;
    private Cursor cur;
    private long calendarID;

    public static final String[] EVENT_PROJECTION = new String[] {
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };

    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

    public CalendarContentProvider(Context context) {
        this.cr = context.getContentResolver();
        this.cur = loadCursor();
        this.calendarID = getOwnerCalendarID();
    }

    private Cursor loadCursor() {
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        return cr.query(uri, EVENT_PROJECTION, null, null, null);
    }

    public long getOwnerCalendarID() {
        long calID;
        long selectedCalID = 3;     // default calID for many devices
        String displayName;
        String accountName;
        String ownerName;

        while (cur.moveToNext()) {
            calID = cur.getLong(PROJECTION_ID_INDEX);
            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);

            if (accountName.equalsIgnoreCase(ownerName)) {
                selectedCalID = calID;
            }
        }
        return selectedCalID;
    }

    public long getCalendarID() {
        return calendarID;
    }

    public ContentResolver getCr() {
        return cr;
    }

    public Cursor getCur() {
        return cur;
    }

    public long addEvent(CalendarEvent calendarEvent) {
        ContentValues values = new ContentValues();

        Calendar startDate = dateToCalendar(calendarEvent.getStartDate());
        long startDateMillis = startDate.getTimeInMillis();
        Calendar endDate = dateToCalendar(calendarEvent.getEndDate());
        long endDateMillis = endDate.getTimeInMillis();

        values.put(CalendarContract.Events.CALENDAR_ID, getCalendarID());
        values.put(CalendarContract.Events.TITLE, calendarEvent.getTitle());
        values.put(CalendarContract.Events.DESCRIPTION, calendarEvent.getDescription());
        values.put(CalendarContract.Events.DTSTART, startDateMillis);
        values.put(CalendarContract.Events.DTEND, endDateMillis);
        values.put(CalendarContract.Events.EVENT_LOCATION, calendarEvent.getLocation());
        values.put(CalendarContract.Events.ALL_DAY, calendarEvent.isAllDay());
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        values.put(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE);
        values.put(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_FREE);

        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
        return Long.parseLong(uri.getLastPathSegment());
    }

    public Intent getEvent(long eventID) {
        Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        return new Intent(Intent.ACTION_VIEW)
                .setData(uri);
    }

    private Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
}
