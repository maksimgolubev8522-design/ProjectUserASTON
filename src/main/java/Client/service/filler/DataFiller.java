package Client.service.filler;

import Client.model.User;
import java.util.List;

public interface DataFiller {
    List<User> fill(int count);
}
