package edu.cwru.students.cwrumapper.user;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.arch.lifecycle.LiveData;

/**
 * This class is used to access teh user database, it serves as a wrapper class
 * for the Room Persistance Library. Methods are used to insert, update, and delete
 * user and user information.
 */
@Dao
public interface DaoAccess {

    @Insert
    void insert(User user);

    @Query("SELECT * FROM User WHERE id = :userID")
    User fetchUserbyID(int userID);

//    @Update
//    int updateUser(User user);

    @Delete
    void deleteUser(User user);

}
