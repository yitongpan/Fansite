# Table of Contents
1. [Challenge Summary](README.md#challenge-summary)
2. [Overall Summary](README.md#overall-summary)
3. [Features](README.md#features)
4. [Features And Data Structure](README.md#features-and-data-structure)
5. [Java Classes](README.md#java-classes)
6. [The end](README.md#the-end)


# Challenge Summary

Picture yourself as a backend engineer for a NASA fan website that generates a large amount of Internet traffic data. Your challenge is to perform basic analytics on the server log file, provide useful metrics, and implement basic security measures.

# Overall Summary

For this project, the programming language I pick is Java. My code implements the basic four feature, and an additional feature to generate the top 10 busiest hours which are not overlap with each other. I have wrote 8 classes to support the implementation, and a main class for implementation.

However, Since I didn't know that we need to update the busiest hour by every second until the last day, I didn't have enough time to modify and I am not able to pass the hours test. I believe my result is correct if I got enough data.
 
User can run run.sh script to implement the program. Note that the input for the script should follow this order:
input file name, host file name, resources file name, busyHours file name, blocked name, overlap option(true or false)

# Features
The feature I implement and the file that stores the information is listed below:

### Feature 1:
List the top 10 most active host/IP addresses that have accessed the site.
File: hosts.txt

### Feature 2:
Identify the 10 resources that consume the most bandwidth on the site.
File: resources.txt

### Feature 3:
List the top 10 busiest (or most frequently visited) 60-minute periods which are overlap.
File: hours.txt

### Feature 4:
Detect patterns of three failed login attempts from the same IP address over 20 seconds so that all further attempts to the site can be blocked for 5 minutes. Log those possible security breaches.
File: blocked.txt

### Feature 5:
List the top 10 busiest 60-minute periods which are not overlap with each other
File: hoursNotOverlap.txt

# Features And Data Structure

In order to optimize the performance of my program, I have pick the following data structure to store the information for each feature.

### Host
We choose HashMap to store the information for each host and their number of visit the website. For each update, we need to first search the name of the host, and then update the count (number of visit) for them. Thus, we need a data structure for quick search and insert. HashMap has a constant and quick speed for searching and Inserting. The speed is O(1).

### Resources
Similar to host, We choose HashMap to store the information for each resource and their total bandwidth.

### Time
we choose a queue (LinkedList) to store the Date Time of each visit within a 60-minute interval since the nature of time is last come first out. The time of each new visit would first be appended at the end of the queue. And then we would dequeue all the date time that are more than 60 minutes earlier than our new record. Thus, we make sure that all the date time in our queue are within the same 60 minute interval. The first date time in our queue would be the start of our 60-minute interval, and the number of visit within that time interval would be the size of our queue. The record for this time interval with the number of visits would be generated into a time node and added to our MaxiTime list for comparison (See below). The reason I choose to use a queue is that it takes constant time for appending and polling since we only need to enqueue and dequeue our date time. Also, getting the size is constant time. All the operation mentioned has a speed of O(1).

If we have only one time queue, our list for the busiest time would have overlapping hours. Thus, we create a list of time queues that described above. The list has a length of 10 since we are looking for the 10 busiest hours. And the time queues are connected to each other. We would interpret it as a time interval of 10 hours. Each time we dequeue an element, it would be appended to the next queue, which represents the previous 60-minute interval. Thus, we are able to generate 10 time nodes with not overlapping time intervals each time for comparison.

### Blocked hosts
We choose a HashMap to store the record that are blocked. The key for the map is the host name and the value are the time which is the end of the 5-minute block request interval. Thus, each time we input a visit of the website, we would first search if the host is in the map, if it is, we would further search if the time is before the end of blocked time, if it is, then the host is blocked and the record would be appended to a list of blocked records. Note that if the time is after the end of blocked time, which means that the block is no loner valid, we would update the map and delete the record. The reason we choose a map is similar to above, map has very quick search.

### MaxiList
We choose LinkedList as a MaxiList to keep check of all the maximum entries (hosts, resources, time node). The list has a constant length of 10 and it's sorted in a ascending order. When we want to compare a candidate node to the nodes in the MaxiList, we would check if the node has a bigger count than the minimum value in the MaxiList. If it is, we would perform a binary search tree to insert the node and remove all the elements in conflicts. For example, the time nodes with overlapping time interval or the string nodes have the same name. For binary search, we have a speed of O(log10) and for insert and compare the candidate node with the existing nodes, we would have a liner time speed O(10). Note that 10 is the size of our maximum list. Thus, the update should be quick.


# Java Classes

We have 8 java classes to support the implementation, and a main class for implementation.

### Host class
Host class generates the node for host. The class keep check of the count, time, HTTP entry code and the node for the host's previous visit. Node that we would only keep at most 3 previous node to save for space. This class not only keeps check of the host's current visit but also checks if the host would be block. If the host have three consecutive HTTP entry code for 401 (the entry code for the current node, the previous node and the previous of previous node), and the visit time interval between this visit and the previous of previous visit is less than 20 seconds, the user would be blocked.

Libraries used: `org.joda.time.DateTime` for Date Time computation and comaprison.

### Time node
Time node class keeps check of the total count for each time interval. It can be used to check if the time node has time interval overlaps with other time node. Time Node would be used in Time Module class.

Libraries used: `org.joda.time.DateTime`

### Time Module
Time Module class is used to update the number of counts within the hour. It updates the file every second. It has two helper classes. One to make a Time Queue and the other one to make a list of TimeQueues.


Libraries used: `org.joda.time.DateTime` for Date Time computation and comaprison.

### MaxiRecord
MaxiRecord class generates a list to keep check of the maximum input. It's mainly used to keep check of the Host and Resources since both of them are String. there is a helper class called StringNode to generate (String, Count) tuples. The way it keeps updating the maxiList is described above in the `MaxiList` description.

Libraries used: `java.util.List`, `java.util.ArrayList`, `java.util.StringJoiner` for print, `java.util.Collections` for binary search

### Fansite
Fansite class behaves like an interface. It takes each line of the input file and updates the host, resources and hours MaxiLists.

Libraries used: `java.util.Map`, `java.util.List`, `java.util.HashMap`, `java.util.ArrayList`, `java.util.Date`, `org.joda.time.DateTime`, `java.text.SimpleDateFormat`

### Parse
Parse class makes a parser to parse the input and return a string array for different information

Libraries used: `java.util.regex.Matcher`, `java.util.regex.Pattern`

### Main
For input and output file and the main function

# The end
Please contact me if you have any question about my project.
