import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Stack;

import static org.junit.Assert.*;

public class PluralityTest {

    Database db = new Database(3, 6,13, 0, 1);

    // helper method to test input2_test.csv file
    public boolean isThirdWinner(String third) {
        return third.equals("B") || third.equals("C") || third.equals("D");
    }

    @Test
    public void run() {
        db.setShuffle(false);
        db.candidate_list = new ArrayList<>();
        db.candidate_list.add(new Candidate(0, "A"));
        db.candidate_list.add(new Candidate(1, "B"));
        db.candidate_list.add(new Candidate(2, "C"));
        db.candidate_list.add(new Candidate(3, "D"));
        db.candidate_list.add(new Candidate(4, "E"));
        db.candidate_list.add(new Candidate(5, "F"));

        db.votes = new HashMap<>();
        PriorityQueue<Vote> pq = new PriorityQueue<>(new VoteComparator());
        pq.add(new Vote(0, 1));
        db.votes.put(0 , pq);

        pq = new PriorityQueue<>(new VoteComparator());
        pq.add(new Vote(2,1));
        db.votes.put(1 , pq);

        pq = new PriorityQueue<>(new VoteComparator());
        pq.add(new Vote(5,1));
        db.votes.put(2 , pq);

        pq = new PriorityQueue<>(new VoteComparator());
        pq.add(new Vote(0,1));
        db.votes.put(3 , pq);

        pq = new PriorityQueue<>(new VoteComparator());
        pq.add(new Vote(3,1));
        db.votes.put(4 , pq);

        pq = new PriorityQueue<>(new VoteComparator());
        pq.add(new Vote(5,1));
        db.votes.put(5 , pq);

        pq = new PriorityQueue<>(new VoteComparator());
        pq.add(new Vote(1,1));
        db.votes.put(6 , pq);

        pq = new PriorityQueue<>(new VoteComparator());
        pq.add(new Vote(3,1));
        db.votes.put(7 , pq);

        pq = new PriorityQueue<>(new VoteComparator());
        pq.add(new Vote(5,1));
        db.votes.put(8 , pq);

        pq = new PriorityQueue<>(new VoteComparator());
        pq.add(new Vote(5,1));
        db.votes.put(9 , pq);

        pq = new PriorityQueue<>(new VoteComparator());
        pq.add(new Vote(2,1));
        db.votes.put(10 , pq);

        pq = new PriorityQueue<>(new VoteComparator());
        pq.add(new Vote(1,1));
        db.votes.put(11 , pq);

        pq = new PriorityQueue<>(new VoteComparator());
        pq.add(new Vote(0,1));
        db.votes.put(12 , pq);

        db.shuffled_list = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            db.shuffled_list.add(i);
        }

        db.winners = new ArrayList<>();
        db.losers = new Stack<>();

        Plurality p = new Plurality(db);
        p.run();

        assertEquals(db.winners.get(0).get_name(), "F");
        assertEquals(db.winners.get(1).get_name(), "A");
        assertTrue("The third winner is not correct after the tie.", isThirdWinner(db.winners.get(2).get_name()));
    }
}
