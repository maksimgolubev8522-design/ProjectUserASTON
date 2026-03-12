package Client.collection;

import Client.model.User;
import Client.strategy.SortStrategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class UserList {
    private final List<User> users;

    public UserList() {
        this.users = new ArrayList<>();
    }

    public UserList(List<User> users) {
        this.users = new ArrayList<>(users);
    }

    public void addUser(User user) {
        if (user != null) {
            users.add(user);
        }
    }

    public void addAll(List<User> newUsers) {
        if (newUsers != null) {
            users.addAll(newUsers);
        }
    }

    public User getUser(int index) {
        if (index >= 0 && index < users.size()) {
            return users.get(index);
        }
        return null;
    }

    public List<User> getUsers() {
        return new ArrayList<>(users); // возвращаем копию для неизменяемости
    }

    public int size() {
        return users.size();
    }

    public boolean isEmpty() {
        return users.isEmpty();
    }

    public void clear() {
        users.clear();
    }

    public void sort(SortStrategy strategy, Comparator<User> comparator) {
        if (strategy != null && comparator != null && !users.isEmpty()) {
            List<User> sorted = strategy.sort(users, comparator);
            users.clear();
            users.addAll(sorted);
        }
    }

    @Override
    public String toString() {
        return "UserList{" +
                "size=" + users.size() +
                '}';
    }
}
