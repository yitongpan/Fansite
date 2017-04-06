package fansite;

import org.joda.time.DateTime;

/**
 * Host class stores the information for most recent visits of the host
 * It checks whether the host would be block from the website
 */

public class Host {
    private Integer count;
    private DateTime time;
    private String code;
    private Host prev;

    /*
     An empty node for host
     */
    public Host() {
        this.count = 0;
        this.time = new DateTime();
        this.code = "0";
        this.prev = null;
    }


    /*
     This definition is used when the host visit for the
     first time. Note that the previous node is empty
     */
    public Host(DateTime time, String c) {
        this.count = 1;
        this.time = time;
        this.code = c;
        this.prev = new Host();

    }

    /*
     This definition is used when it's not the host's first visit
     Note that we only keep at most two previous record of the host's visits
     */
    public Host(DateTime time, String c, Host prevhost) {
        this.count = prevhost.count + 1;
        this.time = time;
        this.code = c;
        this.prev = prevhost;
        this.prev.prev.prev = null;
    }


    /*
      Access count for the host
     */
    public Integer count() {
        return this.count;
    }


    /*
      Access time for the host
     */
    public DateTime time() {
        return this.time;
    }


    /*
      Access count for the host
     */
    public String replyCode() {
        return this.code;
    }


    /*
     Check if the host would be block for the next visit
     401 is the failed login code.
     Input is the timer (Seconds) for consecutive three failed login
     */
    public boolean block(int timeInterval) {
        if (code.equals("401") && prev.code.equals("401") && prev.prev.code.equals("401")) {
            if (time.compareTo(prev.prev.time.plusSeconds(timeInterval)) < 0) {
                return true;
            }
        }
        return false;
    }






}



