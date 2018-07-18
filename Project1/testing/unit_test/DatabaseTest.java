import org.junit.Test;

import static org.junit.Assert.*;

public class DatabaseTest {

    // droop quota, test mode
    Database db1 = new Database(3, 6,10, 1, 1);
    // plurality, test mode
    Database db2 = new Database(40, 1000,10000, 0, 1);

    @Test
    // unit test is not suitable for testing randomness, so only test that this method is not making an exception
    public void shuffleDroopQuota() {
        boolean thrown1 = false;
        try {
            for (int i = 0; i < 6; i++) {
                db1.shuffled_list.add(i);
            }
            db1.shuffle();
        } catch (Exception e) {
            thrown1 = true;
        }
        assertTrue("[droop quota, test mode] shuffle() failed", !thrown1);
    }

    @Test
    public void set_num_voters() {
        db1.set_num_voters(100);
        assertEquals("[droop quota, test mode] set_num_voters() failed ", db1.get_num_voters(), 100);
        db2.set_num_voters(20000);
        assertEquals("[plurality, test mode] set_num_voters() failed ", db2.get_num_voters(), 20000);
    }

    @Test
    public void get_num_voters() {
        db1.set_num_voters(200);
        assertEquals("[droop quota, test mode] get_num_voters() failed ", db1.get_num_voters(), 200);
        db2.set_num_voters(20000);
        assertEquals("[plurality, test mode] get_num_voters() failed ", db2.get_num_voters(), 20000);
    }

    @Test
    public void set_num_candidates() {
        db1.set_num_candidates(100);
        assertEquals("[droop quota, test mode] set_num_candidates() failed ", db1.get_num_candidates(), 100);
        db2.set_num_candidates(200);
        assertEquals("[plurality, test mode] set_num_candidates() failed ", db2.get_num_candidates(), 200);
    }

    @Test
    public void get_num_candidates() {
        db1.set_num_candidates(100);
        assertEquals("[droop quota, test mode] get_num_candidates() failed ", db1.get_num_candidates(), 100);
        db2.set_num_candidates(200);
        assertEquals("[plurality, test mode] get_num_candidates() failed ", db2.get_num_candidates(), 200);
    }

    @Test
    public void get_num_winners() {
        assertEquals("[droop quota, test mode] get_num_winners() failed ", db1.get_num_winners(), 3);
        assertEquals("[plurality, test mode] get_num_winners() failed ", db2.get_num_winners(), 40);
    }

    @Test
    public void get_algorithm() {
        assertEquals("[droop quota, test mode] get_algorithm() failed ", db1.get_algorithm(), 1);
        assertEquals("[plurality, test mode] get_algorithm() failed ", db2.get_algorithm(), 0);
    }

    @Test
    public void get_mode() {
        assertEquals("[droop quota, test mode] get_mode() failed ", db1.get_mode(), 1);
        assertEquals("[plurality, test mode] get_mode() failed ", db2.get_mode(), 1);
    }
}