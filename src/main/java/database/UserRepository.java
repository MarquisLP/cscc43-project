package database;

import database.models.User;

import java.util.NoSuchElementException;

public class UserRepository {
    public static User getUser(String sin) throws NoSuchElementException {
        if (sin.equals("123")) {
            return new User();
        }
        else {
            throw new NoSuchElementException();
        }
    }
}
