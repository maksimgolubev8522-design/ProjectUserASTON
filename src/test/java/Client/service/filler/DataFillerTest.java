package Client.service.filler;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;

public class DataFillerTest {

    @Test
    public void testConstructor() {
        DataFiller dataFiller = new DataFiller();
        assertNotNull(dataFiller);
    }
}