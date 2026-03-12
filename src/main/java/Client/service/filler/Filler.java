package Client.service.filler;

import Client.model.User;

import java.util.List;

public interface Filler {

    List<User> fill(int count);

    String getDescription();
}