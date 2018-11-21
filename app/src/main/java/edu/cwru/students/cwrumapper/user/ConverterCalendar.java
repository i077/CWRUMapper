package edu.cwru.students.cwrumapper.user;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Calendar;

public class ConverterCalendar {

    @TypeConverter
    public static Calendar toCalenedar(String value) {
        Type calendarType = new TypeToken<Calendar>() {}.getType();
        return new Gson().fromJson(value, calendarType);
    }
    @TypeConverter
    public static String fromCalendar(Calendar cal) {
        Gson gson = new Gson();
        String json = gson.toJson(cal);
        return json;
    }
}
