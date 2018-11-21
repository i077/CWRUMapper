package edu.cwru.students.cwrumapper.user;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;
import android.arch.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * This class is used to access teh user database, it serves as a wrapper class
 * for the Room Persistance Library. Methods are used to insert, update, and delete
 * user and user information.
 */
@Dao
@TypeConverters({ConverterItinerary.class})
public interface DaoAccess {

    @Insert
    void insert(User user);

    @Query("SELECT * FROM user_table WHERE id = :userID")
    User fetchUserbyID(int userID);

    @Update(onConflict = REPLACE)
    int updateUser(User user);

    @Query("SELECT * FROM user_table ORDER BY id")
    List<User> fetchUsers();


    @Delete
    void deleteUser(User user);

}
