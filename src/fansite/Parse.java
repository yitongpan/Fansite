package fansite;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * This Parse Class parses the input file and
 * gets all the information: host, time, request, HTTP reply code
 * and bytes for each visit of the NASA home page
 */
public class Parse {
    private static final String html = "(.+)\\s*- -\\s*\\[(.*)\\s+-0400\\]\\s*\"(.*)\"\\s+([0-9]+)\\s+([0-9]+|-).*";
    private static final Pattern p = Pattern.compile(html),
                         time = Pattern.compile("(.*):(.*)\\s-0400"),
                         source1 = Pattern.compile("(GET|POST|HEAD)\\s+(\\/.*)"),
                         source2 = Pattern.compile("(GET|POST|HEAD)\\s+(.*)");




    /*
     Parses each line from the input and stores the information
     in a String array. The inputs by order are: host, resources,
     time, code, bytes
     */
    public static String[] eval(String line) {
        Matcher m;
        String[] input = new String[5];
        if ((m = p.matcher(line)).matches()) {
            input[0] = m.group(1).trim();
            input[1] = m.group(2).trim();
            input[3] = m.group(4).trim();
            input[4] = m.group(5).trim();
            Matcher k1 = source1.matcher(m.group(3));
            Matcher k2 = source2.matcher(m.group(3));
            if (k1.matches()) {
                input[2] = k1.group(2).trim().split(" ")[0];
            } else if (k2.matches()) {
                input[2] = k2.group(2).trim().split(" ")[0];
            } else {
                input[2] = m.group(3);
            }
        } else {
            System.out.println("Wrong Line");
            return null;
        }
        return input;

    }



}
