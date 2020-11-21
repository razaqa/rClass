package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.entity.User;

public class DataGenerator {

    private static final String[] FIRST = new String[]{};
    private static final String[] SECOND = new String[]{};

    public static List<User> generateUsers() {
        List<User> users = new ArrayList<>(FIRST.length * SECOND.length);
        Random rnd = new Random();
        for (int i = 0; i < FIRST.length; i++) {
            for (int j = 0; j < SECOND.length; j++) {
                User user = new User();
                user.setName(FIRST[i] + " " + SECOND[j]);
                user.setId(FIRST.length * i + j + 1);
                users.add(user);
            }
        }
        return users;
    }

}