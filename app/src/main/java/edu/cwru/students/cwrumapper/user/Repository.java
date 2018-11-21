package edu.cwru.students.cwrumapper.user;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import android.arch.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

public class Repository {

    private DaoAccess mDaoAccess;

    public Repository(Context context) {
        UserDatabase db = UserDatabase.getDatabase(context);
        mDaoAccess = db.daoAccess();
    }

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

    public List<User> fetchUsers() {
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

}