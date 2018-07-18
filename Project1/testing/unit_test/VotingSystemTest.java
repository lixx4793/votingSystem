import org.junit.Test;

import static org.junit.Assert.*;

public class VotingSystemTest {

    String input1 = "/testing/unit_test/files/input1_test.csv";
    VotingSystem sys = new VotingSystem();
    Database db = new Database(2, 3, 4, 1,1);

    @Test
    public void read_file() {
        boolean thrown = false;

        sys.set_database(db);
        sys.set_filename(input1);
        try {
            sys.read_file();
        } catch  (Exception e) {
            thrown = true;
        }
        assertTrue("read_file() failed", !thrown);
        Database db = sys.get_database();
        assertEquals(sys.get_database(), db);

    }

    @Test
    public void write_output() {
        boolean thrown = false;

        sys.set_database(db);
        sys.set_filename(input1);
        try {
            sys.write_output();
        } catch  (Exception e) {
            thrown = true;
        }
        assertTrue("read_file() failed", !thrown);
        Database db = sys.get_database();
        assertEquals(sys.get_database(), db);
    }

    @Test
    public void write_report() {
        boolean thrown = false;

        sys.set_database(db);
        sys.set_filename(input1);
        try {
            sys.write_report();
        } catch  (Exception e) {
            thrown = true;
        }
        assertTrue("read_file() failed", !thrown);
        Database db = sys.get_database();
        assertEquals(sys.get_database(), db);
    }
}