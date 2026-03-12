package Client.service.filler;

import static org.junit.jupiter.api.Assertions.*;


import Client.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

public class FileFillerTest {

    private FileFiller fileFiller;

    @BeforeEach
    public void setUp() {
        fileFiller = new FileFiller();
    }

    @Test
    public void testConstructor() {
        assertNotNull(fileFiller);
    }

    @Test
    public void testFillWithInvalidCount() {
        List<User> result = fileFiller.fill(0);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFillFromPathWithNullPath() {
        List<User> result = fileFiller.fillFromPath(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFillFromPathWithEmptyPath() {
        List<User> result = fileFiller.fillFromPath("");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFillFromPathWithNonExistentFile() {
        List<User> result = fileFiller.fillFromPath("nonexistentfile.txt");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}