import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Owner on 4/22/2018.
 */
public class DroopQuotaTest {
    VotingSystem vs = new VotingSystem();
    Database db = new Database();

    //@PBI1-test 1.01   test droop quota algorithm
    @Test
    public void run() throws Exception {
        vs.set_database(db);
        vs.set_filename("/testing/unit_test/files/droop_testing1.csv");
        vs.read_file();
        DroopQuota dq = new DroopQuota(db);
        dq.run();
        assertEquals(db.winners.get(0).get_name(), "A");
        assertEquals(db.winners.get(1).get_name(), "B");
        assertEquals(db.winners.get(2).get_name(), "D");

    //@PBI1-test 1.03  test droop quota algorithm
        Database db2 = new Database();
        vs.set_database(db2);
        vs.set_filename("/testing/unit_test/files/Test1.csv");
        vs.read_file();
        DroopQuota dq2 = new DroopQuota(db2);
        dq2.run();
        assertEquals(db2.winners.get(0).get_name(), "B");
        assertEquals(db2.winners.get(1).get_name(), "A");
        assertEquals(db2.winners.get(2).get_name(), "D");

    }

}