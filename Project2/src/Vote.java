/**
 * Vote.java
 * <p>
 * This is the Vote object class. It contains various attributes of a vote,
 * and getter and setter methods.
 * </p>
 * @author Xueman Liang, Floyd Chen and Yuhao Li
 * @version 1.1
 */

public class Vote {
  private int candidate_id;  // id of the candidate that this vote goes to
  private int priority;  // the priority of this vote to candidate with id candidate_id; 1 is the highest

  /**
   * Constructor
   *
   * @param id id of the candidate this vote goes to
   * @param v priority of this vote to the corresponding candidate
   */
  public Vote(int id, int v) {
    candidate_id = id;
    priority = v;
  }

  /** @return priority of this vote to corresponding candidate */
  public int get_priority() { return priority; }

  /** @return id of candidate this vote associates to */
  public int get_candidate() { return candidate_id; }
}

