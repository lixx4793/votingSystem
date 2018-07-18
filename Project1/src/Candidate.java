import java.util.ArrayList;

/**
 * Candidate.java
 * <p>
 * This is the Candidate object class. It contains various attributes of a candidate,
 * and getter and setter methods.
 * </p>
 * @author Xueman Liang, Floyd Chen and Yuhao Li
 * @version 1.0
 */

public class Candidate {
    private int id;  // unique id of this candidate
    private String name;  // name of this candidate that reads in from the input file
    private int num_vote;  // number of votes received
    private int status; // current status of this candidate; 1 for elected (winner), -1 for fail (loser), 0 otherwise
    private ArrayList<Integer> voter_id_list;  // a list of voter id(s) that have voted for this candidate
    private int order; // relative order of this candidate receives his first vote among all candidates (droop quota)

    /**
     * Constructor
     *
     * @param id (required) id of the candidate
     * @param name (required) name of the candidate. Can be any string.
     */
    public Candidate(int id, String name) {
        this.id = id;
        this.name = name;
        num_vote = 0;
        status = 0;
        voter_id_list = new ArrayList<>();
        order = -1;
    }

    /** @return id of the candidate passed by constructor */
    public int get_id() { return id; }

    /** @return name of the candidate passed by constructor */
    public String get_name() { return name; }

    /**
     * Sets the number of vote(s) received by this candidate
     * @param vote number of votes received
     */
    public void set_num_vote(int vote) { num_vote = vote; }

    /** @return number of vote(s) currently received by this candidate */
    public int get_num_vote() { return num_vote; }

    /**
     * Updates the number of vote received by this candidate by 1. This method is called
     * whenever the candidate receives 1 new vote from voters
     */
    public void update_votes() { num_vote++; }

    /**
     * Adds this voter with id i to the voter_id_list of the candidate
     * @param i id of this voter (that has voted to this candidate)
     */
    public void update_voter_id_list(int i) { voter_id_list.add(i); }

    /** @return voter_id_list of this candidate */
    public ArrayList<Integer> get_voter_id_list() { return voter_id_list; }

    /**
     * Sets status s to this candidate. The status is an integer value of either -1, 0, and 1.
     * The status is initialized to be 0. This method is called whenever this candidate is
     * selected as a winner (status = 1) or a loser (status = -1).
     * @param s status of this candidate
     */
    public void set_status(int s) {status = s;}

    /** @return status of this candidate */
    public int get_status() {return status;}

    /**
     * Sets the relative order of this candidate receives his or her first ballot among all candidates.
     * The order is initialized to be -1. The method is called whenever this candidate receives his
     * or her first ballot.
     * @param o an integer indicating the order of this candidate receives his or her first ballot
     */
    public void set_order(int o) {order = o;}

    /** @return order of this candidate receives his or her first ballot */
    public int get_order() {return order;}
}
