package Client.collection;

import Client.model.User;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.Collectors;

public class CustomUserCollection implements Iterable<User> {

    private final List<User> users;

    public CustomUserCollection() {
        this.users = new ArrayList<>();
    }

    public CustomUserCollection(Collection<User> users) {
        this.users = new ArrayList<>(users);
    }

    public boolean add(User user) {
        return user != null && users.add(user);
    }

    public boolean addAll(Collection<User> users) {
        return users != null && this.users.addAll(users);
    }

    public User get(int index) {
        return index >= 0 && index < users.size() ? users.get(index) : null;
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

    public boolean contains(User user) {
        return users.contains(user);
    }

    public boolean remove(User user) {
        return users.remove(user);
    }

    public List<User> toList() {
        return new ArrayList<>(users);
    }

    public Stream<User> stream() {
        return users.stream();
    }

    public Stream<User> parallelStream() {
        return users.parallelStream();
    }

    public static CustomUserCollection fromStream(Stream<User> userStream) {
        return new CustomUserCollection(userStream.collect(Collectors.toList()));
    }

    public CustomUserCollection filter(Predicate<User> predicate) {
        return new CustomUserCollection(users.stream().filter(predicate).collect(Collectors.toList()));
    }

    public CustomUserCollection sorted(Comparator<User> comparator) {
        return new CustomUserCollection(users.stream().sorted(comparator).collect(Collectors.toList()));
    }

    public UserList toUserList() {
        return new UserList(users);
    }

    @Override
    public Iterator<User> iterator() {
        return users.iterator();
    }

    @Override
    public String toString() {
        return "CustomUserCollection{size=" + users.size() + "}";
    }
}