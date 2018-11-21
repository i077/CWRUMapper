package edu.cwru.students.cwrumapper.user;

import android.app.Application;
import android.os.AsyncTask;

import android.arch.lifecycle.LiveData;

public class Repository {

    private DaoAccess mDaoAccess;

    public Repository(Application application) {
        UserDatabase db = UserDatabase.getDatabase(application);
        mDaoAccess = db.daoAccess();
    }

    public void update(User user){
        new updateAsyncTask(mDaoAccess).execute(user);
    }

    public LiveData<User> fetchUser(int userID) {
        return mDaoAccess.fetchUserbyID(userID);
    }

    public void insert(User user) {
        new insertAsyncTask(mDaoAccess).execute(user);
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

}