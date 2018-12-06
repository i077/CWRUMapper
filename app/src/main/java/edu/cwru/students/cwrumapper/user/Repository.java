package edu.cwru.students.cwrumapper.user;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Repository {

    private UserDatabase db;
    private LocationsDatabase ldb;
    private DaoAccess mDaoAccess;
    private DaoLocations mDaoLocations;
    private static int FACTOR = 100;

    private static String TAG = "Repository";

    public Repository(Context context) {
        db = UserDatabase.getDatabase(context);
        mDaoAccess = db.daoAccess();
        ldb = LocationsDatabase.getInstance(context);
        mDaoLocations = ldb.dataDao();
        mDaoLocations.insertAll(Location.populateData());
    }

    public void insertUser(User user) {
        int userID = user.getId();
        deleteUser(user);
        List<Itinerary> itineraries = user.getItineraries();
        for (int i = 0; i < itineraries.size(); i++) {
            itineraries.get(i).setUserID(userID);
            int itineraryID = userID + FACTOR * (i + 1);
            itineraries.get(i).setId(itineraryID);
            List<DayItinerary> dayItineraries = itineraries.get(i).getItinerariesForDays();
            for (int j = 0; j < dayItineraries.size(); j++) {
                dayItineraries.get(j).setItineraryID(itineraryID);
                int dayItineraryID = userID + FACTOR * (i + 1) + FACTOR * FACTOR * (j + 1);
                dayItineraries.get(j).setId(dayItineraryID);
                List<Event> events = dayItineraries.get(j).getEvents();
                for (int k = 0; k < events.size(); k++) {
                    events.get(k).setDayItineraryID(dayItineraryID);
                    int eventID = userID + FACTOR * (i + 1) + FACTOR * FACTOR * (j + 1) + FACTOR * FACTOR * FACTOR * (k + 1);
                    Log.v(TAG, "Wrote mEvent " + events.get(k).getName() + " to ID " + eventID);
                    events.get(k).setId(eventID);
                }
                mDaoAccess.insertEventList(events);
            }
            mDaoAccess.insertDayItineraryList(dayItineraries);
        }
        mDaoAccess.insertItineraryList(itineraries);
        mDaoAccess.insertUser(user);
    }

    private void deleteUser(User user){
        int userID = user.getId();
        List<Itinerary> itineraries = user.getItineraries();
        for (int i = 0; i < itineraries.size(); i++) {
            int itineraryID = itineraries.get(i).getId();
            List<DayItinerary> dayItineraries = itineraries.get(i).getItinerariesForDays();
            for (int j = 0; j < dayItineraries.size(); j++) {
                int dayItineraryID = dayItineraries.get(j).getId();
                mDaoAccess.deleteEvent(dayItineraryID);
            }
            mDaoAccess.deleteDayItinerary(itineraryID);
        }
        mDaoAccess.deleteItinerary(userID);
        mDaoAccess.deleteUser(userID);

    }

    public User getUser(int userID) {
        User user = mDaoAccess.getUser(userID);
        if (user == null) {
            return null;
        }
        List<Itinerary> itineraries = mDaoAccess.getItineraryList(userID);
        for (int i = 0; i < itineraries.size(); i++) {
            int itineraryID = itineraries.get(i).getId();
            List<DayItinerary> dayItineraries = mDaoAccess.getDayItineraryList(itineraryID);
            for (int j = 0; j < dayItineraries.size(); j++) {
                int dayItineraryID = dayItineraries.get(j).getId();
                List<Event> events = mDaoAccess.getEventList(dayItineraryID);
                Log.v(TAG, "Read " + events.size() + " mEvent(s) for " + user.getId());
                if (events == null) {
                    events = new ArrayList<Event>();
                }
                dayItineraries.get(j).setEvents(events);
            }

            itineraries.get(i).setItinerariesForDays(dayItineraries);
        }
        user.setItineraries(itineraries);
        return user;
    }

    public void nukeUserTable() {
        db.clearAllTables();
    }

    public List<Location> getAllLocation() {
        return mDaoLocations.getAll();
    }

    public Location getLocation(String name) {
        return mDaoLocations.getLocation(name);
    }

    public void nukeLocationsTable() {
        ldb.clearAllTables();
    }

    /*

    public void update(User user){
        new updateAsyncTask(mDaoAccess).execute(user);
    }

    public void updateUser(User user) {
        User tempUser = fetchUser(user.getId());
        delete(tempUser);
        insert(user);
    }

    public User fetchUser(int userID) {
        return mDaoAccess.fetchUserbyID(userID);
    }

    public ArrayList<User> fetchUsers() {
        return mDaoAccess.fetchUsers();
    }

    public void insert(User user) {
        new insertAsyncTask(mDaoAccess).execute(user);
    }

    public void delete(User user) {
        mDaoAccess.deleteUser(user);
    }


    private static class updateAsyncTask extends AsyncTask<User, Void, Void> {

        private DaoAccess mAsyncTaskDao;

        updateAsyncTask(DaoAccess dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final User... params) {
            mAsyncTaskDao.updateUser(params[0]);
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<User, Void, Void> {

        private DaoAccess mAsyncTaskDao;

        insertAsyncTask(DaoAccess dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final User... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
    */

}
