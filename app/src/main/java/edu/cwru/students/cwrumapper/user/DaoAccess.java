package edu.cwru.students.cwrumapper.user;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
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
public interface DaoAccess {

    @Insert(onConflict = REPLACE)
    void insertUser(User user);

    @Insert(onConflict = REPLACE)
    void insertItineraryList(List<Itinerary> itinList);

    @Insert(onConflict = REPLACE)
    void insertDayItineraryList(List<DayItinerary> dItinList);

    @Insert(onConflict = REPLACE)
    void insertEventList(List<Event> eventList);

    @Query("SELECT * FROM user_table WHERE id = :userID")
    User getUser(int userID);

    @Query("SELECT * FROM Itinerary WHERE userID = :userID")
    List<Itinerary> getItineraryList(int userID);

    @Query("SELECT * FROM DayItinerary WHERE itineraryID = :itineraryID")
    List<DayItinerary> getDayItineraryList(int itineraryID);

    @Query("SELECT * FROM Event WHERE dayItineraryID = :dayItineraryID")
    List<Event> getEventList(int dayItineraryID);


    @Query("DELETE FROM user_table WHERE id = :userID")
    void deleteUser(int userID);

    @Query("DELETE FROM Itinerary WHERE id = :itineraryID")
    void deleteItinerary(int itineraryID);

    @Query("DELETE FROM DayItinerary WHERE id = :dayItineraryID")
    void deleteDayItinerary(int dayItineraryID);

    @Query("DELETE FROM Event WHERE id = :eventID")
    void deleteEvent(int eventID);

    /*
    @Update(onConflict = REPLACE)
    int updateUser(User user);

    @Query("SELECT * FROM user_table ORDER BY id")
    List<User> fetchUsers();


    @Delete
    void deleteUser(User user);
    */

}
