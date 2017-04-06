package fansite;

import java.util.Comparator;
import java.util.Date;
import java.lang.*;
import org.joda.time.DateTime;

/**
 * Created by Ellie on 4/1/17.
 */
public class Node {
    public DateTime time;
    public Integer count;

    public Node(DateTime time, int count) {
        this.time = time.minusMinutes(60);
        this.count = count;

    }

    public void add() {
        count += 1;
    }

    public static class NodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node a1, Node a2) {
            return a1.count.compareTo(a2.count);
        }
    }



}







