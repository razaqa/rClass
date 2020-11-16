package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.entity.User;

@Dao
public interface UserDao {
    @Query("SELECT COUNT(name) FROM users")
    LiveData<Integer> getUsersCount();

    @Query("SELECT * FROM users")
    List<User> getAllUsers();

    @Query("SELECT * FROM users LIMIT :limit")
    LiveData<User> getSomeUsers(int limit);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<User> users);

    @Query("DELETE FROM users")
    void deleteAll();
}
