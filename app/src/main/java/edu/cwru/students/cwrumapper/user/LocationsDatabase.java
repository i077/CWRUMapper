package edu.cwru.students.cwrumapper.user;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.concurrent.Executors;


@Database(entities = {Location.class}, version = 2, exportSchema = false)
@TypeConverters({DataTypeConverter.class})
public abstract class LocationsDatabase extends RoomDatabase {

    private static LocationsDatabase INSTANCE;

    public abstract DaoLocations dataDao();

    public synchronized static LocationsDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = buildDatabase(context);
        }
        return INSTANCE;
    }

    private static LocationsDatabase buildDatabase(final Context context) {
        return Room.databaseBuilder(context,
                LocationsDatabase.class,
                "locations-database")
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                getInstance(context).dataDao().insertAll(Location.populateData());
                            }
                        });
                    }
                })
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();
    }

}
