import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * DroopQuota.java
 * <p>
 * This is the DroopQuota object class. It implements the Algorithm interface.
 * It contains two private attributes and a run() method that processes the
 * input voting data based on droop quota algorithm, updates the database and
 * generates election results.
 * </p>
 * @author Xueman Liang, Floyd Chen and Yuhao Li
 * @version 1.0
 */

public class DroopQuota implements Algorithm {
    private Database database;  // Database used to store data
    private int quota;  // calculated quota

    /**
     * Constructor
     *
     * @param db database associates with this algorithm
     */
    public DroopQuota(Database db) {
        database = db;
        if(database.get_mode() == 0) {  // only shuffle when the program is run in regular mode
            database.shuffle();
        }
        quota = (database.get_num_voters()/(database.get_num_winners()+1))+1;  // calculate quota based on formula
    }

    /**
     * Main method that processes the voting data based on droop quota algorithm.
     * The method takes no parameter, it reads data from the database structure.
     * It reads and processes one ballot each time, validates the ballot, adds it to
     * corresponding candidate. It than checks if this candidate reaches the quota,
     * and if all seats are fulfilled. It maintains both a winner list and a loser
     * list, and prints the two lists with the votes received by each candidate on
     * console when done processing the data.
     *
     * {@inheritDoc}
     */
    @Override
    public void run() {
        // Process voting results
        int order = 0;
        outerloop:
        for (int i=0; i<database.get_num_candidates(); i++) {
            boolean found_winner = false;
            Iterator itr = database.shuffled_list.listIterator();
            while (itr.hasNext()) {
                int cur_voter = (int) itr.next();
                if(database.votes.get(cur_voter).isEmpty()) {
                    // This voter has no more valid vote
                    // Continue to the next voter
                    continue;
                }
                Vote v = database.votes.get(cur_voter).poll();
                Candidate c = database.candidate_list.get(v.get_candidate());
                while (c.get_status() != 0 && !database.votes.get(cur_voter).isEmpty()) {
                    // This candidate is either on winner list or on loser list (invalid)
                    // Then continue reading votes
                    v = (Vote) ((PriorityQueue) database.votes.get(cur_voter)).poll();
                    c = database.candidate_list.get(v.get_candidate());
                }
                if (c.get_status() != 0) {
                    // This vote is invalids
                    continue;
                }

                // Record the order of each candidate receives the first vote
                if(c.get_order() == -1) {
                    c.set_order(order);
                    order++;
                }
                c.update_votes();
                c.update_voter_id_list(cur_voter);
                if (c.get_num_vote() == quota) {
                    c.set_status(1);
                    database.winners.add(c);
                    found_winner = true;

                    // check if have all seats fulfilled
                    if(database.winners.size() == database.get_num_winners()) {
                        break outerloop;
                    }
                }
            }

            // End of one iteration
            // If not found a winner at this iteration, then update loser list
            if(!found_winner) {
                int loser_id = -1;
                int min_votes = Integer.MAX_VALUE;
                Candidate c;
                // Gets the current min number of votes among all valid candidates
                for (int j = 0; j < database.get_num_candidates(); j++) {
                    c = database.candidate_list.get(j);
                    if (c.get_status() == 0) { // Active candidate
                        if (c.get_num_vote() < min_votes) {
                            loser_id = c.get_id();
                            min_votes = c.get_num_vote();
                        } else if (c.get_num_vote() == min_votes &&
                                c.get_order() > database.candidate_list.get(loser_id).get_order()) {
                            // If current candidate receives his first vote later than the current loser_id
                            loser_id = c.get_id();
                        }
                    }
                }

                if (loser_id != -1) {  // found one loser
                    c = database.candidate_list.get(loser_id);
                    database.losers.push(c);
                    c.set_status(-1);
                }
            }
        }

        // PART 1: Final update for loser list
        // The only chance to get into this if statement is when all winners are selected from previous iterations
        // There are possible un-assigned candidates (neither on winner list nor on loser list)
        // Then just add non-elected candidates to loser list (order does not matter in this case)
        if(database.losers.size() + database.winners.size() != database.candidate_list.size()) {
            Iterator loser_i = database.candidate_list.listIterator();
            while(loser_i.hasNext()) {
                Candidate c = (Candidate) loser_i.next();
                if(c.get_status() == 0) {
                    database.losers.push(c);
                    c.set_status(-1);
                }
            }
        }

        // PART 2: Final update for winner list
        // The only change to get into this while loop is when not all seats are fulfilled after processing all ballots
        // At this point, each candidate is either on winner list or loser list (no un-assigned candidate)
        while(database.winners.size() < database.get_num_winners()) {
            // If not, pop the least added candidate from the loser list
            database.winners.add(database.losers.pop());
        }
        // NOTE: only either PART 1 or PART 2 will be run; These two conditions cannot appear at the same time

        // Print out election results on console
        print();
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
        System.out.println("====== Election Results Using Droop Quota Algorithm =====");
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
