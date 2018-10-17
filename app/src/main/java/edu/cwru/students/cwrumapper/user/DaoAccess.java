package edu.cwru.students.cwrumapper.user;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import androidx.lifecycle.LiveData;

@Dao
public interface DaoAccess {

    @Insert
    void insert(User user);

    @Query("SELECT * FROM User WHERE id = :userID")
    LiveData<User> fetchUserbyID(int userID);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

}
