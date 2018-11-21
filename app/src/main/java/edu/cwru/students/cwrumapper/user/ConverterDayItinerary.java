package edu.cwru.students.cwrumapper.user;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ConverterDayItinerary {
    @TypeConverter
    public static ArrayList<Itinerary> toArrayList(String value) {
        Type listType = new TypeToken<ArrayList<DayItinerary>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }
    @TypeConverter
    public static String fromArrayList(ArrayList<DayItinerary> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
