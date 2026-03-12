package Client.service.filler;

import Client.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class RandomFillerTest {

    private RandomFiller randomFiller;

    @BeforeEach
    public void setUp() {
        randomFiller = new RandomFiller();
    }

    @Test
    public void testConstructor() {
        assertNotNull(randomFiller);
    }

    @Test
    public void testFillWithZeroCount() {
        List<User> users = randomFiller.fill(0);
        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    @Test
    public void testFillWithNegativeCount() {
        List<User> users = randomFiller.fill(-5);
        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    @Test
    public void testFillWithPositiveCount() {
        List<User> users = randomFiller.fill(3);
        assertNotNull(users);
        assertEquals(3, users.size());
    }

    @Test
    public void testGeneratedUsersHaveValidData() {
        List<User> users = randomFiller.fill(2);

        for (User user : users) {
            assertNotNull(user.getName());
            assertFalse(user.getName().isEmpty());

            assertNotNull(user.getPassword());
            assertFalse(user.getPassword().isEmpty());
            assertTrue(user.getPassword().length() >= 6);

            assertNotNull(user.getMail());
            assertFalse(user.getMail().isEmpty());
            assertTrue(user.getMail().contains("@"));
        }
    }
}