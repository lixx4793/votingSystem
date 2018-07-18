import java.util.Comparator;

/**
 * CandidateComparator.java
 * <p>
 * This is the CandidateComparator object class. It implements the Comparator interface.
 * It contains only 1 CandidateComparator() method that compares two candidates. This class
 * is useful when sorting candidates in Plurality class.
 * </p>
 * @author Xueman Liang, Floyd Chen and Yuhao Li
 * @version 1.0
 */

public class CandidateComparator implements Comparator <Candidate> {
    /**
     * A comparator method that compares two Candidates based on the votes they have received. This
     * method is useful when sorting a list of Candidates in descending order based on the number of
     * votes received. This method is used in Plurality object class to sort the Candidate list.
     *
     * @param c1 first candidate
     * @param c2 second candidate
     * @return -1 if Candidate c1 has fewer votes than Candidate c2; 1 if c1 has more votes than c2;
     * 0 if two candidates have the same amount of votes
     */
    public int compare(Candidate c1, Candidate c2) {
        if(c1.get_num_vote() == c2.get_num_vote()) {
            return 0;
        }
        return c1.get_num_vote() > c2.get_num_vote() ? -1 : 1;
    }
}