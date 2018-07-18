import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CandidateTest {

    Candidate c = new Candidate(3, "cand");

    @Test
    public void get_id() {
        assertEquals(c.get_id(), 3);
    }

    @Test
    public void get_name() {
        assertEquals(c.get_name(), "cand");
    }

    @Test
    public void set_num_vote() {
        assertEquals(c.get_num_vote(), 0);
        c.set_num_vote(100);
        assertEquals(c.get_num_vote(), 100);
    }

    @Test
    public void get_num_vote() {
        c.set_num_vote(99);
        assertEquals(c.get_num_vote(), 99);
    }

    @Test
    public void update_votes() {
        c.set_num_vote(1);
        assertEquals(c.get_num_vote(), 1);
        c.update_votes();
        assertEquals(c.get_num_vote(), 2);
    }

    @Test
    public void update_voter_id_list() {
        c.update_voter_id_list(777);
        int temp = c.get_voter_id_list().get(c.get_voter_id_list().size() - 1);
        assertEquals(temp, 777);
    }

    @Test
    public void get_voter_id_list() {
        ArrayList<Integer> id_list = c.get_voter_id_list();
        c.update_voter_id_list(888);
        id_list.add(888);
        assertEquals(c.get_voter_id_list(), id_list);
    }

    @Test
    public void set_status() {
        c.set_status(1);
        assertEquals(c.get_status(), 1);
    }

    @Test
    public void get_status() {
        c.set_status(-1);
        assertEquals(c.get_status(), -1);
    }

    @Test
    public void set_order() {
        assertEquals(c.get_order(), -1);
        c.set_order(78);
        assertEquals(c.get_order(), 78);
    }

    @Test
    public void get_order() {
        c.set_order(55);
        assertEquals(c.get_order(), 55);
    }
}