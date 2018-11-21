package edu.cwru.students.cwrumapper.user;

import android.arch.persistence.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ConverterItinerary {
    @TypeConverter
    public static ArrayList<Itinerary> toArrayList(String value) {
        Type listType = new TypeToken<ArrayList<Itinerary>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }
    @TypeConverter
    public static String fromArrayList(ArrayList<Itinerary> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }



}