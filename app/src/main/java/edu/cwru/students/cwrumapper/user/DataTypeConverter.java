package edu.cwru.students.cwrumapper.user;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
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

    @TypeConverter
    public static ArrayList<Double> fromString(String value) {
        Type listType = new TypeToken<ArrayList<Double>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<Double> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

}
