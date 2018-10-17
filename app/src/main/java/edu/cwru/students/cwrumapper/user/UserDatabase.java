package edu.cwru.students.cwrumapper.user;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {User.class},version = 1, exportSchema = false)
public abstract class UserDatabase extends RoomDatabase {
    public abstract DaoAccess daoAccess();
    private static volatile UserDatabase INSTANCE;
    static UserDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (UserDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            UserDatabase.class, "user_database").build();
                }
            }
        }
        return INSTANCE;
    }

}

