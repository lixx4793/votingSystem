import java.util.Comparator;

/**
 * VoteComparator.java
 * <p>
 * This is the VoteComparator object class. It implements the Comparator interface.
 * It contains only 1 VoteComparator() method that compares two votes. This class
 * is useful when adding votes to the priority queue list of each voter in VotingSystem
 * class.
 * </p>
 * @author Xueman Liang, Floyd Chen and Yuhao Li
 * @version 1.1
 */

public class VoteComparator implements Comparator <Vote> {
  /**
   * A comparator method that compares two Votes based on the priority value assigned. This
   * method is useful when reading input voting data and adding each vote to the priority queue
   * associates with each voter. Votes of each voter is organized and stored in priority queue
   * such that votes with higher priority will be processed before votes with lower priority.
   *
   * @param v1 first vote
   * @param v2 second vote
   * @return 1 if Vote v1 has higher priority than Vote v2; -1 if v1 has lower priority than v2;
   * 0 if two votes have the same priority
   */
  public int compare(Vote v1, Vote v2) {
    if(v1.get_priority() == v2.get_priority()) {
      return 0;
    }
    return v1.get_priority() > v2.get_priority() ? 1 : -1;
  }
}
