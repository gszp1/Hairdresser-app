package model;

import java.util.ArrayList;
import java.util.Optional;

public class UserList {

    private final ArrayList<User> users;

    public UserList() {
        users = new ArrayList<>();
    }

    synchronized public void addUser(User user) {
        users.add(user);
    }

    synchronized public boolean removeUser(User user) {
        return users.remove(user);
    }

    synchronized public Optional<User> getUser(String userId) {
        return users.stream().filter(user -> user.getClientId().equals(userId)).findFirst();
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    @Override
    public String toString() {
        return "UserList{" +
                "users=" + users +
                '}';
    }
}
