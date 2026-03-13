package Client.service.stream;

import Client.model.User;
import Client.collection.CustomUserCollection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserStreamProcessor {

    public CustomUserCollection filterByPasswordLength(CustomUserCollection collection, int minLength) {
        return collection.stream()
                .filter(user -> user.getPassword().length() >= minLength)
                .collect(CustomUserCollection::new,
                        CustomUserCollection::add,
                        (c1, c2) -> c2.forEach(c1::add));
    }

    public CustomUserCollection filterByEmailDomain(CustomUserCollection collection, String domain) {
        return collection.stream()
                .filter(user -> user.getMail().endsWith("@" + domain))
                .collect(CustomUserCollection::new,
                        CustomUserCollection::add,
                        (c1, c2) -> c2.forEach(c1::add));
    }


    public Map<String, List<User>> groupByEmailDomain(CustomUserCollection collection) {
        return collection.stream()
                .collect(Collectors.groupingBy(
                        user -> {
                            String mail = user.getMail();
                            return mail.substring(mail.indexOf("@") + 1);
                        }
                ));
    }

    public Map<String, Double> getNameLengthStatistics(CustomUserCollection collection) {
        return collection.stream()
                .collect(Collectors.groupingBy(
                        user -> "Средняя длина имени",
                        Collectors.averagingInt(user -> user.getName().length())
                ));
    }

    public CustomUserCollection getUniqueNames(CustomUserCollection collection) {
        return collection.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(
                                User::getName,
                                user -> user,
                                (existing, replacement) -> existing
                        ),
                        map -> new CustomUserCollection(map.values())
                ));
    }


    public List<String> getAllEmails(CustomUserCollection collection) {
        return collection.stream()
                .map(User::getMail)
                .collect(Collectors.toList());
    }

    public boolean allUsersValid(CustomUserCollection collection) {
        return collection.stream()
                .allMatch(user -> user.getName() != null &&
                        user.getPassword() != null &&
                        user.getMail() != null);
    }
}