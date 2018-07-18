import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Stack;

import static org.junit.Assert.*;

public class DroopQuotaTest {
    Database db = new Database(2, 3,4, 1, 1);

    @Test
    public void run() {
        db.setShuffle(true);

        db.candidate_list = new ArrayList<>();
        db.candidate_list.add(new Candidate(0, "A"));
        db.candidate_list.add(new Candidate(1, "B"));
        db.candidate_list.add(new Candidate(2, "C"));

        db.votes = new HashMap<>();
        PriorityQueue<Vote> pq = new PriorityQueue<>(new VoteComparator());
        pq.add(new Vote(0, 1));
        db.votes.put(0 , pq);
        pq = new PriorityQueue<>(new VoteComparator());
        pq.add(new Vote(2,1));
        pq.add(new Vote(0,2));
        db.votes.put(1, pq);
        pq = new PriorityQueue<>(new VoteComparator());
        db.votes.put(2, pq);
        pq = new PriorityQueue<>(new VoteComparator());
        pq.add(new Vote(0, 1));
        pq.add(new Vote(1, 2));
        db.votes.put(3, pq);

        db.shuffled_list = new ArrayList<>();
        db.shuffled_list.add(0);
        db.shuffled_list.add(1);
        db.shuffled_list.add(2);
        db.shuffled_list.add(3);

        db.winners = new ArrayList<>();
        db.losers = new Stack<>();

        DroopQuota dq = new DroopQuota(db);
        dq.run();

        assertEquals(db.winners.get(0).get_name(), "A");
        assertEquals(db.winners.get(1).get_name(), "C");
        assertEquals(db.losers.get(0).get_name(), "B");
    }
}