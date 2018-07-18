import org.junit.Test;

import static org.junit.Assert.*;

public class CandidateComparatorTest {

    Candidate c1 = new Candidate(1, "c1");
    Candidate c2 = new Candidate(2, "c2");
    CandidateComparator cc = new CandidateComparator();

    @Test
    public void compare() {
        c1.set_num_vote(12345);
        c2.set_num_vote(12345);
        int result = cc.compare(c1, c2);
        assertEquals(result, 0);
        c2.set_num_vote(12344);
        result = cc.compare(c1, c2);
        assertEquals(result, -1);
        c2.set_num_vote(12346);
        result = cc.compare(c1, c2);
        assertEquals(result, 1);
    }
}