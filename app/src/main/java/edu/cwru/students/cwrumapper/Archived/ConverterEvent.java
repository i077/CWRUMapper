package edu.cwru.students.cwrumapper.Archived;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import edu.cwru.students.cwrumapper.user.Event;
import edu.cwru.students.cwrumapper.user.Itinerary;

public class ConverterEvent {

    @TypeConverter
    public static ArrayList<Itinerary> toArrayList(String value) {
        Type listType = new TypeToken<ArrayList<Event>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }
    @TypeConverter
    public static String fromArrayList(ArrayList<Event> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
