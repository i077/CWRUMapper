package edu.cwru.students.cwrumapper.user;

import android.arch.persistence.room.TypeConverter;

import java.util.Calendar;
import java.util.Date;

public class DataTypeConverter{
    @TypeConverter
    public static Calendar toCalendar(Long value) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(value);
        return value == null ? null : cal;
    }

    @TypeConverter
    public static Long toLong(Calendar value) {
        return value == null ? null : value.getTimeInMillis();
    }

}
