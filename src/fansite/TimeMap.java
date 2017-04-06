package fansite;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Created by Ellie on 4/5/17.
 */
public class TimeMap {
    private Map<DateTime, Integer> hours;
    private PriorityQueue<TimeNode> maxTime;

    public TimeMap() {
        hours = new HashMap<>();
        maxTime = new PriorityQueue<>();
    }

    public void insert(DateTime newTime) {



    }
}
