package io.github.acaimingus.worqspace.ui.calendar;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "events")
public class CalendarEvent {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "title")
    public String title;
    @ColumnInfo(name = "location")
    public String location;
    @ColumnInfo(name = "type")
    public CalendarEventType type;

    @ColumnInfo(name = "start_time")
    public long startTime;
    @ColumnInfo(name = "end_time")
    public long endTime;

    public CalendarEvent() {
    }

    public CalendarEvent(String title, String location, CalendarEventType type, long startTime, long endTime) {
        this.title = title;
        this.location = location;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
