import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.Stack;
import java.util.Collections;

/**
 * Database.java
 * <p>
 * This is the Database object class. It is used as the primary data structure to
 * in this program to store data. It contains various private attributes that can
 * only accessed via getter or setter methods, and public attributes that allow
 * direct access from main driver and other object classes, such as Plurality and
 * DroopQuota. It contains only 1 member method shuffle(), and setter and getter
 * methods.
 * </p>
 * @author Xueman Liang, Floyd Chen and Yuhao Li
 * @version 1.1
 */

public class Database {
  private int num_voter;  // Number of voter(s)
  private int num_candidate;  // Number of candidate(s)
  private int num_winner;  // Number of winner(s)
  private int algorithm;  // 0 for plurality; 1 for droop quota
  private  boolean shuffle;  // boolean value


  public ArrayList<Candidate> candidate_list;  // a list of candidates
  // key: voter id; value: a priority queue of Vote object associates with this voter.
  public HashMap<Integer, PriorityQueue<Vote>> votes;
  public ArrayList<Integer> shuffled_list;  // a list of voters id that gets shuffled if shuffle() is called
  public ArrayList<Candidate> winners;  // a list of Candidates that are selected as winner
  public Stack<Candidate> losers;  // a stack of Candidates that are selected as losers
  public ArrayList<Integer> invalidated_voter_list;


  /**
   * Constructor
   */
  public Database() {
    num_winner = 0;
    num_candidate = 0;
    num_voter = 0;
    algorithm = 0;
    shuffle = true;

    candidate_list = new ArrayList<>();
    votes = new HashMap<>();
    shuffled_list = new ArrayList<>();
    this.winners = new ArrayList<>();
    losers = new Stack<>();
    invalidated_voter_list = new ArrayList<>();
  }

  /**
   * If shuffle is true, then shuffles the shuffled_list (a list of voter ids)
   * 5 times; Otherwise, does nothing.
   */
  public void shuffle() {
    // initialized the shuffled_list.
    // The list is initialized in the same order as voters read from the input file
    for(int i = 0; i < num_voter; i++) {
      shuffled_list.add(i);
    }

    if(shuffle) {
      System.out.println("shuffling...");
      for(int i=0; i<5; i++) { // shuffle the list 5 times
        Collections.shuffle(shuffled_list);
      }
    }
  }

  /**
   * Sets the number of voter(s)
   * @param v number of voter(s)
   */
  public void set_num_voters(int v) { num_voter = v; }

  /** @return number of voter(s) */
  public int get_num_voters() { return num_voter; }

  /**
   * Sets the number of candidate(s)
   * @param c number of candidate(s)
   */
  public void set_num_candidates(int c) { num_candidate = c; }

  /** @return number of candidate(s) */
  public int get_num_candidates() { return num_candidate; }

  /**
   * Sets the number of winner(s)
   * @param w number of winner(s)
   */
  public void set_num_winners(int w) { num_winner = w; }

  /** @return number of winner(s) */
  public int get_num_winners() { return num_winner; }

  /**
   * Sets the algorithm
   * @param a algorithm
   */
  public void set_algorithm(int a) { algorithm = a; }

  /** @return algorithm */
  public int get_algorithm() { return algorithm; }
  
  /**
   * sets the shuffle option
   * @param s boolean value of shuffle option
   */
  public void set_shuffle(boolean s) { shuffle = s; }
}
