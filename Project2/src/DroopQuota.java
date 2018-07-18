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
 * @version 1.1
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
    database.shuffle();

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
    Iterator itr = database.shuffled_list.listIterator();
    while (itr.hasNext()) {
      int cur_voter = (int) itr.next();
      if (database.votes.get(cur_voter).size() < database.candidate_list.size()/2) {
        database.invalidated_voter_list.add(cur_voter);
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
        database.invalidated_voter_list.add(cur_voter);
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

        // check if have all seats fulfilled
        if(database.winners.size() == database.get_num_winners()) {
          break;
        }
      }
    }

    // Done with full vote distribution
    // Redistribute votes from losers
    while(database.winners.size() + database.losers.size() < database.candidate_list.size()) {
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

        // Redistribute votes;
        while (!c.get_voter_id_list().isEmpty()) {
          int voter = c.get_voter_id_list().poll();
          c.get_dis_voter_id_list().add(voter);

          if (database.votes.get(voter).isEmpty()) {
            database.invalidated_voter_list.add(voter);
            continue;
          }
          Vote v = database.votes.get(voter).poll();
          Candidate cc = database.candidate_list.get(v.get_candidate());
          while (cc.get_status() != 0 && !database.votes.get(voter).isEmpty()) {
            v = (Vote) ((PriorityQueue) database.votes.get(voter)).poll();
            cc = database.candidate_list.get(v.get_candidate());
          }
          if (cc.get_status() != 0) {
            database.invalidated_voter_list.add(voter);
            continue;
          }
          cc.update_votes();
          cc.update_voter_id_list(voter);
          if (cc.get_num_vote() == quota) {
            cc.set_status(1);
            database.winners.add(cc);

            // check if have all seats fulfilled
            if(database.winners.size() == database.get_num_winners()) {
              break;
            }
          }
        }
      }
    }

    // If seats are not filled, then pop candidate from the loser list
    while (database.winners.size() < database.get_num_winners()) {
      Candidate c = database.losers.pop();
      c.set_status(1);
      database.winners.add(c);
    }

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
