import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by lixx4793 on 4/21/18.
 */
public class VotingSystemTest {
    VotingSystem vs = new VotingSystem();
    Database database = new Database();

    @Test
    public void read_file() throws Exception {
        vs.set_database(database);
        boolean err = false;
        boolean err2 = false;
        boolean mode = true;
        boolean mode2 = false;
        int ag1, ag2, ag3, winner1, winner2, winner3;

        //@PBI test 2.01 - set shuffle, get shuffle
        vs.get_database().set_shuffle(false);
        mode = vs.get_database().get_shuffle();
        assertEquals(mode, false);
        vs.get_database().set_shuffle(true);
        mode2 = vs.get_database().get_shuffle();
        assertEquals(mode2, true);

        // @PBI test 3.01  - using regular file for plurality algorithm
        vs.set_filename("/testing/unit_test/files/plu_testing1.csv");
        vs.read_file();
        ag1 = vs.get_database().get_algorithm();
        winner1 = vs.get_database().get_num_winners();

        //@PBI test 3.02  - using regular file for droop quota algorithm
        vs.set_filename("/testing/unit_test/files/droop_testing1.csv");
        vs.read_file();
        ag2 = vs.get_database().get_algorithm();
        winner2 = vs.get_database().get_num_winners();


        // @PBI test 3.03 input path to a non existing file
        vs.set_filename("/testing/unit_test/files/notAfile.csv");
        try {
            vs.read_file();
        }catch (NullPointerException e) {
            err = true;
        }

        // @PBI test 3.04 input path to a file contains incorrect algorithms choice
        vs.set_filename("testing/unit_test/files/wrongAlgorithm.csv");
        try{
            vs.read_file();
        } catch(NullPointerException e) {
            err2 =true;
        }

        // @PBI test 3.05 - path to a file has a large size of input data set
        vs.set_filename("testing/unit_test/files/largeInput.csv");
        vs.read_file();
        ag3 = vs.get_database().get_algorithm();
        winner3 = vs.get_database().get_num_winners();

        assertEquals(ag1, 0);
        assertEquals(winner1, 2);
        assertEquals(ag2, 1);
        assertEquals(winner2, 3);
        assertEquals(err, true);
        assertEquals(err2, true);
        assertEquals(ag3, 0);
        assertEquals(winner3, 3);


    }

    @Test
    public void write_output() throws Exception {

    }

}