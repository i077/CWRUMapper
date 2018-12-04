package edu.cwru.students.cwrumapper.user;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface DaoLocations {


    @Query("SELECT * FROM Location")
    List<Location> getAll();

    @Query("SELECT * FROM Location WHERE LocationName = :name")
    Location getLocation(String name);

    @Insert
    void insertAll(Location... locations);
}
