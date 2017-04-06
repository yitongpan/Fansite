package fansite;

import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.Collections;
import java.util.List;

/**
 * The MaxiRecord class use a Priority Queue to stores
 * the list of maximum values for the (String, count) tuples
 * It has a helper class called StringNode
 */

public class MaxiRecord {

    private ArrayList<StringNode> maxiList;
    private int size;
    private int min;


    /*
    Definition for the maximum list
    Size is the number of maximum objects
    the user wants to keep
     */
    public MaxiRecord(int size) {
        this.maxiList = new ArrayList<>();
        this.min = 0;
        this.size = size;
    }


    /*
     Updates the maximum list if we add a (object, count) tuples
     Size of the list reminds the same
     */
    public void update(String name, Integer count) {
        if (count < min) {
            return;
        }
        StringNode node = new StringNode(name, count);
        int pos = Collections.binarySearch(maxiList, node);
        if (pos < 0) {
            pos = -(pos) - 1;
        }
        maxiList.add(pos, node);

        for (int i = maxiList.size() - 1; i >= 0; i--) {
            if (i != pos && maxiList.get(i).name.equals(name)) {
                maxiList.remove(i);
                break;
            }

        }
        if (maxiList.size() > size) {
            maxiList.remove(0);
        }
        if (maxiList.size() == size) {
            this.min = maxiList.get(0).count;
        }

    }

    /*
     Prints the maximum (object, count) pair separated by the separator
     Each record is in a new line
     */
    public String printWithValue(String separator) {
        List<String> max = new ArrayList<>();
        String output = "";
        for (int i = maxiList.size() - 1; i >= 0; i--) {
            StringNode node = maxiList.get(i);
            StringJoiner join = new StringJoiner(separator);
            join.add(node.name);
            join.add(Integer.toString(node.count));
            output = output + join.toString() + "\n";
        }
        return output;

    }

    public String printName() {
        List<String> max = new ArrayList<>();
        String output = "";
        for (int i = maxiList.size() - 1; i >= 0; i--) {
            StringNode node = maxiList.get(i);
            output = output + node.name + "\n";
        }
        return output;
    }


    /*
     Access size of our maxlist
     */
    public Integer size() {
        return this.size;
    }


    /*
     Access the List of names for the object in the maximum records
     */
//    public List<String> records() {
//        List<String> max = new LinkedList<>();
//        for (StringNode node : maxiList) {
//            max.add(maxiList.poll().name);
//        }
//        return max;
//    }


    /**
     * The private class is used to create pair for (Object, count)
     */
    private class StringNode implements Comparable<StringNode> {
        String name;
        Integer count;

        public StringNode(String name, Integer count) {
            this.name = name;
            this.count = count;
        }


        @Override
        public int compareTo(StringNode other) {
            return this.count.compareTo(other.count);
        }
    }



}
