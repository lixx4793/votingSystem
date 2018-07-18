import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Plurality.java
 * <p>
 * This is the Plurality object class. It implements the Algorithm interface.
 * It contains one private attribute and two member methods, tie_solver() that
 * solves a tie condition if detected, and run() that processes the input voting
 * data based on plurality algorithm, updates the database and generates election
 * results.
 * </p>
 * @author Xueman Liang, Floyd Chen and Yuhao Li
 * @version 1.0
 */

public class Plurality implements Algorithm {
    private Database database;  // Database used to store data

    /**
     * Constructor
     *
     * @param db database associates with this algorithm
     */
    public Plurality(Database db) {
        database = db;
    }

    /**
     * Main method that processes the voting data based on plurality algorithm.
     * The method takes no parameter, it reads data from the database structure.
     * It reads and processes one ballot of one voter each time, adds it to
     * corresponding candidate. After done processing all ballots, it sorts the
     * candidate_list in descending order based the votes received by each candidate.
     * If tie condition is detected, the solve tie. It maintains both a winner list
     * and a loser list, and prints the two lists with the votes received by each
     * candidate on console when done processing the data.
     *
     * {@inheritDoc}
     */
    @Override
    public void run() {
        // process voting results
        Iterator itr = database.votes.entrySet().iterator();
        while(itr.hasNext()) {  // read and process voting data
            HashMap.Entry entry = (HashMap.Entry) itr.next();
            Vote v = (Vote) ((PriorityQueue)entry.getValue()).poll();
            int voter = (int) entry.getKey();
            Candidate c = database.candidate_list.get(v.get_candidate());
            c.update_votes();
            c.update_voter_id_list(voter);
        }

        // sort voting results in decreasing order
        Collections.sort(database.candidate_list, new CandidateComparator());

        int v_num = database.get_num_winners();

        // check for tie condition
        // Tie condition occurs when the nth candidate on the sorted list has the same votes as the (n+1)th candidate
        // if to choose n winners
        if(database.candidate_list.get(v_num-1).get_num_vote() == database.candidate_list.get(v_num).get_num_vote()) {
            // tie condition detected
            // using two pointers approach to get the candidate range of which each candidates within the range
            // share the same amount of votes
            int i1 = v_num-1;
            int i2 = v_num;
            while(i1-1 >= 0 &&
                    database.candidate_list.get(i1-1).get_num_vote() == database.candidate_list.get(v_num-1).get_num_vote()) {
                i1--;
            }
            while(i2+1 < database.get_num_candidates() &&
                    database.candidate_list.get(i2+1).get_num_vote() == database.candidate_list.get(v_num-1).get_num_vote()) {
                i2++;
            }

            // solve tie
            ArrayList<Integer> random_winner = tie_solver(i1, i2, database.get_num_winners() - i1);

            // write candidates before tie condition occurs to winner list
            int winner = 0;
            while(winner < i1) {
                Candidate c = database.candidate_list.get(winner);
                database.winners.add(c);
                c.set_status(1);
                winner++;
            }

            // write randomly selected candidates within the tie range to winner list
            winner = 0;
            while(winner < random_winner.size()) {
                Candidate c = database.candidate_list.get(random_winner.get(winner));
                database.winners.add(c);
                c.set_status(1);
                winner++;
            }
        } else { // no tie condition, update winner list
            int winner = 0;
            while (winner < database.get_num_winners()) {
                database.candidate_list.get(winner).set_status(1);
                database.winners.add(database.candidate_list.get(winner));
                winner++;
            }
        }

        // Update loser list
        Iterator loser_i = database.candidate_list.listIterator();
        while(loser_i.hasNext()) {
            Candidate c = (Candidate) loser_i.next();
            if(c.get_status() != 1) {
                c.set_status(-1);
                database.losers.push(c);
            }
        }

        // Print out election results on console
        print();
    }

    /**
     * Randomly selects a certain number of candidates to be winners within tie
     * condition occurrence range
     *
     * @param low lower bound of the tie condition range
     * @param high upper bound of the tie condition range
     * @param number number of candidates needs to be selected
     * @return a list of certain number of candidates that are randomly selected
     */
    private ArrayList<Integer> tie_solver(int low, int high, int number) {
        ArrayList<Integer> list = new ArrayList<>();
        for(int i = low; i <= high; i++) {
            list.add(i);
        }

        for(int i = 0; i < 5; i++) {
            Collections.shuffle(list);
        }

        // Shuffle 5 times, and return the first number candidates on the list.
        return new ArrayList<>(list.subList(0, number));
    }

    /**
     * Retrieves data from the database and prints election results containing both winner(s)
     * and loser(s) of the election. The name of the candidate will be printed along with the
     * amount of valid vote(s) received by this candidate.
     * {@inheritDoc}
     */
    @Override
    public void print() {
        System.out.println();
        System.out.println("====== Election Results Using Plurality Algorithm =====");
        System.out.println("WINNER(S): ");
        Iterator it = database.winners.listIterator();
        while(it.hasNext()) {
            Candidate c = (Candidate) it.next();
            System.out.println(" " + c.get_name() + ": " + c.get_num_vote());
        }
        System.out.println();
        System.out.println("LOSER(S): ");
        it = database.losers.listIterator();
        while(it.hasNext()) {
            Candidate c = (Candidate) it.next();
            System.out.println(" " + c.get_name() + ": " + c.get_num_vote());
        }
    }
}
