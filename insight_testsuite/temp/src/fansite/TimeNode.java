package fansite;

import org.joda.time.DateTime;

/**
 * TimeNode class stores the number of count
 * for each time interval
 */
public class TimeNode implements Comparable<TimeNode> {

    private DateTime start;
    private DateTime end;
    private Integer count;


    /*
     Definition with a start time and a time interval
     */
    public TimeNode(DateTime time, Integer interval, Integer count) {
        this.start = time;
        this.end = time.plusMinutes(interval);
        this.count = count;
    }


    /*
    Definition with a start and end time
     */
    public TimeNode(DateTime start, DateTime end, Integer count) {
        this.start = start;
        this.end = end;
        this.count = count;
    }


    /*
      Access count for the TimeNode
     */
    public Integer count() {
        return this.count;
    }


    /*
      Access starting time for the interval
     */
    public DateTime startTime() {
        return this.start;
    }


    /*
      Access ending time for the interval
     */
    public DateTime endTime() {
        return this.end;
    }


    /*
      Comparison method for TmeNode
      The bigger the count, the bigger the node
     */
    @Override
    public int compareTo(TimeNode other) {
        return this.count.compareTo(other.count);
    }


    /*
     Check if two TimeNodes have overlap time interval
     */
    public boolean overlap(TimeNode other) {
        if(this.start.compareTo(other.end) <= 0 && this.end.compareTo(other.start) >= 0) {
            return true;
        } else if (other.start.compareTo(this.end) <= 0 && other.end.compareTo(this.start) <= 0) {
            return true;
        }
        return false;
    }





}







