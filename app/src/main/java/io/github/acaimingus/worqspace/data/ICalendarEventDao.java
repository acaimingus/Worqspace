package io.github.acaimingus.worqspace.data;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.github.acaimingus.worqspace.ui.calendar.CalendarEvent;

@androidx.room.Dao
public interface ICalendarEventDao {
    @Insert
    void insert(CalendarEvent event);
    @Delete
    void delete(CalendarEvent event);
    @Update
    void update(CalendarEvent event);
    @Query("SELECT * FROM events")
    List<CalendarEvent> getAll();

    @Query("SELECT * FROM events WHERE start_time BETWEEN :fromTimestamp AND :toTimestamp")
    List<CalendarEvent> getEventsForDay(long fromTimestamp, long toTimestamp);
}
