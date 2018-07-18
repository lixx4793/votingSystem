import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Owner on 4/22/2018.
 */
public class PluralityTest {
    VotingSystem vs = new VotingSystem();
    Database db = new Database();

    // PBI1 - test02  test plurality algorithm with regular input
    @Test
    public void run() throws Exception {
        vs.set_database(db);
        vs.set_filename("/testing/unit_test/files/plurality1.csv");
        vs.read_file();
        Plurality plt = new Plurality(db);
        plt.run();
        assertEquals(db.winners.get(0).get_name(), "F");
        assertEquals(db.winners.get(1).get_name(), "B");

    //PBI1 - test03 test  plurality algorithm with large input data set
        Database db2 = new Database();
        vs.set_database(db2);
        vs.set_filename("/testing/unit_test/files/largeInput.csv");
        vs.read_file();
        Plurality pl2 = new Plurality(db2);
        pl2.run();
        assertEquals(db2.winners.get(0).get_name(), "B");
        assertEquals(db2.winners.get(1).get_name(), "F");
    }

}