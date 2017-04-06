package fansite;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;

import org.joda.time.DateTime;
import java.text.SimpleDateFormat;


/**
 * Created by Ellie on 4/1/17.
 */
public class Fansite {
    private Map<String, Host> hostMap;
    private Map<String, Integer> resourceMap;
    private TimeModule hours;
    private Map<String, DateTime> blockList;
    private List<String> blockRecord;
    private MaxiRecord maxiHost;
    private MaxiRecord maxiResource;
    private boolean overlap;


    public Fansite(int host, int resources, int hours, boolean overlap) {
        this.hostMap = new HashMap<>();
        this.resourceMap = new HashMap<>();
        this.hours = new TimeModule(hours, overlap);
        this.blockList = new HashMap<>();
        this.blockRecord = new ArrayList<>() ;
        this.maxiHost = new MaxiRecord(host);
        this.maxiResource = new MaxiRecord(resources);
        this.overlap = overlap;

    }


    public void add(String input) {
        String[] str = Parse.eval(input);
        if (str == null) {
            return;
        }
        SimpleDateFormat format = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss");
        DateTime date = null;
        String name = str[0];
        String resource = str[2];
        String code = str[3];
        String bandwidth = str[4];
        Integer bytes = 0;
        Host host;

        if (!bandwidth.equals("-")) {
            bytes = Integer.parseInt(bandwidth);
        }

        try {
            Date d = format.parse(str[1]);
            date = new DateTime(d);
        } catch (Exception e) {
            e.printStackTrace();
        }

        hours.insert(date);

        if (blockList.containsKey(name)) {
            if (date.compareTo(blockList.get(name)) < 0) {
                blockRecord.add(input);
            } else {
                blockList.remove(name);
            }
        }

        if (hostMap.containsKey(name)) {
            host = new Host(date, code, hostMap.get(name));
        } else {
            host = new Host(date, code);
        }
        hostMap.put(name, host);
        maxiHost.update(name, host.count());

        if (host.block(20)) {
            blockList.put(name, date.plusMinutes(5));
        }

        if (resourceMap.containsKey(resource)) {
            resourceMap.put(resource, resourceMap.get(resource) + bytes);
        } else {
            resourceMap.put(resource, bytes);
        }
        maxiResource.update(resource, resourceMap.get(resource));

    }


    public MaxiRecord getMaxiHost() {
        return maxiHost;
    }

    public MaxiRecord getMaxiResource() {
        return maxiResource;
    }

    public TimeModule getHours() {
        return hours;
    }

    public List<String> getBlockRecord() {
        return blockRecord;
    }

//    public void hoursInit(String path, String separator) {
//        hours.initOutputFile(path, separator);
//    }


}