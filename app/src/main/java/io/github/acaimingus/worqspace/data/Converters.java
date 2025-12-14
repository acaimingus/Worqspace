package io.github.acaimingus.worqspace.data;

import androidx.room.TypeConverter;

import io.github.acaimingus.worqspace.ui.calendar.CalendarEventType;

public class Converters {
    @TypeConverter
    public static CalendarEventType toType(String value) {
        if (value == null) {
            return CalendarEventType.TASK;
        }
        try {
            return CalendarEventType.valueOf(value);
        } catch (IllegalArgumentException e) {
            return CalendarEventType.TASK;
        }
    }

    @TypeConverter
    public static String fromType(CalendarEventType type) {
        return type == null ? null : type.name();
    }
}
