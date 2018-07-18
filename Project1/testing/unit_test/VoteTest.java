import org.junit.Test;

import static org.junit.Assert.*;

public class VoteTest {

    Vote v1 = new Vote(16, 1);
    Vote v2 = new Vote(28, 2);
    Vote v3 = new Vote(57, 3);

    @Test
    public void get_priority() {
        assertEquals(v1.get_priority(), 1);
        assertEquals(v2.get_priority(), 2);
        assertEquals(v3.get_priority(), 3);
    }

    @Test
    public void get_candidate() {
        assertEquals(v1.get_candidate(), 16);
        assertEquals(v2.get_candidate(), 28);
        assertEquals(v3.get_candidate(), 57);
    }
}