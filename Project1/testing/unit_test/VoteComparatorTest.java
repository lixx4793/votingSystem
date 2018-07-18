import org.junit.Test;

import static org.junit.Assert.*;

public class VoteComparatorTest {

    Vote v1 = new Vote(16, 1);
    Vote v2 = new Vote(28, 2);
    Vote v3 = new Vote(27, 1);
    VoteComparator vc = new VoteComparator();

    @Test
    public void compare() {
        int result = vc.compare(v1, v2);
        assertEquals(result, -1);
        result = vc.compare(v1, v3);
        assertEquals(result, 0);
        result = vc.compare(v2, v1);
        assertEquals(result, 1);
    }
}