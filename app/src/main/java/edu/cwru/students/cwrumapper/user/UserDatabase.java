package edu.cwru.students.cwrumapper.user;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;


/**
 * This class abstracts the user database and allows functionality of the room database.
 * This is used to create a database, and ensure it functions as a singleton.
 */
@Database(entities = {User.class, Itinerary.class, DayItinerary.class, Event.class},version = 2, exportSchema = false)
@TypeConverters({DataTypeConverter.class})
public abstract class UserDatabase extends RoomDatabase {
    public abstract DaoAccess daoAccess();
    private static volatile UserDatabase INSTANCE;
    static UserDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (UserDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            UserDatabase.class, "user_database").fallbackToDestructiveMigration().allowMainThreadQueries().build();

                }
            }
        }
        return INSTANCE;
    }

}

