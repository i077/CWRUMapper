package edu.cwru.students.cwrumapper.user;

public interface DaoAccess {
    void insertUser(User user);

    User fetchUserbyID(int userID);

    void updateUser(User user);
    void deleteUser(User user);

}
