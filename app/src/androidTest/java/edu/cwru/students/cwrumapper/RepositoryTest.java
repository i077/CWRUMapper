package edu.cwru.students.cwrumapper;

/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Application;
import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import edu.cwru.students.cwrumapper.user.*;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;


/**
 * This is not meant to be a full set of tests. For simplicity, most of your samples do not
 * include tests. However, when building the Room, it is helpful to make sure it works before
 * adding the UI.
 */

@RunWith(AndroidJUnit4.class)
public class RepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private DaoAccess mUserDao;
    private UserDatabase mDb;
    private Repository repo;
    public User user;



    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();

        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        /*
        mDb = Room.inMemoryDatabaseBuilder(context, UserDatabase.class)
                // Allowing main thread queries, just for testing.
                .allowMainThreadQueries()
                .build();
        mUserDao = mDb.daoAccess();
        */
        repo = new Repository(context);
        user = new User(1,"Amrish");
        repo.insertUser(user);
    }
    /*

    @After
    public void closeDb() {
        mDb.close();
    }
    */

    @Test
    public void insertAndGetUser() throws Exception {
        User userTest = repo.getUser(1);
        assertEquals(user.getName(), userTest.getName());
    }

    @Test
    public void insertAndGetWrongUser() throws Exception {
        User userTest = repo.getUser(0);
        assertNull(userTest);
    }

    @Test
    public void insertTwoUsers() throws Exception {

        User user2 = new User(1, "Tim");
        repo.insertUser(user2);
        User userTest = repo.getUser(1);
        assertNotEquals(user.getName(), userTest.getName());;
    }

    @Test
    public void updateUser() throws Exception {
        user.setName("Imran");
        //user.newItinerary();
        repo.insertUser(user);
        User userTest = repo.getUser(1);
        userTest.newItinerary();
        userTest.addEvent(0,"Jolly", new edu.cwru.students.cwrumapper.user.Location("Millis Schmitt", new LatLng[]{
                new LatLng(41.504099, -81.606873),
                new LatLng(41.503729, -81.607005)}), 100, "100", 9, 0, 0);
        repo.insertUser(userTest);
        User userTest2 = repo.getUser(1);
        userTest.deleteEvent(0,0);
        repo.insertUser(userTest);
        User userTest3 = repo.getUser(1);
        assertEquals(userTest3.getEvents(0).size(), 0);
    }

    @Test
    public void testLocation() throws Exception {
        Location test = repo.getLocation("Millis Schmitt");
        assertEquals(test.getLatitudes().get(1), 41.503729, .00001);
    }
/*
    @Test
    public void getAllWords() throws Exception {
        Word word = new Word("aaa");
        mWordDao.insert(word);
        Word word2 = new Word("bbb");
        mWordDao.insert(word2);
        List<Word> allWords = LiveDataTestUtil.getValue(mWordDao.getAlphabetizedWords());
        assertEquals(allWords.get(0).getWord(), word.getWord());
        assertEquals(allWords.get(1).getWord(), word2.getWord());
    }

    @Test
    public void deleteAll() throws Exception {
        Word word = new Word("word");
        mWordDao.insert(word);
        Word word2 = new Word("word2");
        mWordDao.insert(word2);
        mWordDao.deleteAll();
        List<Word> allWords = LiveDataTestUtil.getValue(mWordDao.getAlphabetizedWords());
        assertTrue(allWords.isEmpty());
    }
    */

    @After
    public void clearALL() {
        repo.nukeUserTable();
        repo.nukeLocationsTable();
    }

}
