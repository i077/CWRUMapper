package edu.cwru.students.cwrumapper.user;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface DaoLocations {


    @Query("SELECT * FROM Location")
    List<Location> getAll();

    @Query("SELECT * FROM Location WHERE LocationName = :name")
    Location getLocation(String name);

    @Insert(onConflict = REPLACE)
    void insertAll(Location... locations);
}
