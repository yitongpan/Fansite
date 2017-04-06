package fansite;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.sql.Time;
import java.util.*;

/**
 *
 * Actural Module
 */
public class TimeModule {
	private TimeQueueList queueList;
	private List<TimeNode> maxTime;
	private int size;
	private boolean overlap;
	private int min;

	public TimeModule(int size, boolean overlap) {
		this.size = size;
		if (overlap) {
			this.queueList = new TimeQueueList(1);
		} else {
			this.queueList = new TimeQueueList(size);
		}
		this.maxTime = new ArrayList<>();
		this.overlap = overlap;
		this.min = 0;

	}


    public void updateMaxTime(TimeNode candidate) {
        if (candidate.count() < min) {
            return;
        }

        if (overlap) {
            for (int i = maxTime.size() - 1; i >= 0; i--) {
                if (maxTime.get(i).startTime().compareTo(candidate.startTime()) == 0) {
                    maxTime.remove(i);
                    break;
                }
            }
        }else {
            // remove any possible overlap first
            for (int i = maxTime.size() - 1; i >= 0; i--) {
                if (maxTime.get(i).overlap(candidate)) {
                    if (candidate.compareTo(maxTime.get(i)) < 0) {
                        // candidate is invalid, return.
                        return;
                    } else {
                        // remove the overlapping
                        maxTime.remove(i);
                    }
                }
            }
        }

        int pos = Collections.binarySearch(maxTime, candidate);
        if (pos < 0) {
            pos = -(pos) - 1;
        }
        maxTime.add(pos, candidate);

        if (maxTime.size() > size) {
            maxTime.remove(0);
        }
        if (maxTime.size() == size) {
            this.min = maxTime.get(0).count();
        }
    }

	// Check this logic!
//	public void updateMaxTime(TimeNode candidate) {
//		if (candidate.count() < min) {
//			return;
//		}
//		int pos = Collections.binarySearch(maxTime, candidate);
//		if (pos < 0) {
//			pos = -(pos) - 1;
//		}
//		maxTime.add(pos, candidate);
//		if (overlap) {
//			for (int i = maxTime.size() - 1; i >= 0; i--) {
//				if (i != pos && maxTime.get(i).startTime().compareTo(candidate.startTime()) == 0) {
//					maxTime.remove(i);
//					break;
//				}
//			}
//		}else {
//			// remove any possible overlap first
//			for (int i = maxTime.size() - 1; i >= 0; i--) {
//				if (i != pos && maxTime.get(i).overlap(candidate)) {
//					if (candidate.compareTo(maxTime.get(i)) < 0) {
//						// candidate is invalid, return.
//						return;
//					} else {
//						// remove the overlapping
//						maxTime.remove(i);
//					}
//				}
//			}
//		}
//
//		if (maxTime.size() > size) {
//			maxTime.remove(0);
//		}
//		if (maxTime.size() == size) {
//			this.min = maxTime.get(0).count();
//		}
//	}

	public void finalize() {
		List<TimeNode> candiadateList = this.queueList.finalDeque();
		for (int i = 0; i < candiadateList.size(); i++) {
		    updateMaxTime(candiadateList.get(i));
        }

	}

	public void insert(DateTime newTime) {
		List<TimeNode> candidateList = this.queueList.insert(newTime);
		for (int i = 0; i < candidateList.size(); i++) {
			updateMaxTime(candidateList.get(i));
		}
	}

	public List<TimeNode> getMaxTime() {
		return maxTime;
	}

	public String print(String separator) {
		StringJoiner newline = new StringJoiner("\n");
		DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MMM/yyyy:HH:mm:ss");
		TimeNode node;
		String output = "";
		for (int i = maxTime.size() - 1; i >= 0; i--) {
			node = maxTime.get(i);
			StringJoiner join = new StringJoiner(separator);
			join.add(fmt.print(node.startTime()) + " -0400");
			join.add(Integer.toString(node.count()));
			output = output + join.toString() + "\n";
		}
		return output;
	}


    public class TimeQueue {
        private LinkedList<DateTime> timeQueue;
        private int firstMaxCount;
        private DateTime firstMaxTime;
        private TimeQueue nextQueue;
        private boolean makeNode;

        public TimeQueue() {
            this.timeQueue = new LinkedList<DateTime>();
            this.nextQueue = null;
            this.firstMaxCount = 0;
            this.firstMaxTime = null;
            this.makeNode = false;
        }

        public DateTime peek() {
            return this.timeQueue.peek();
        }

        public DateTime poll() {
            return this.timeQueue.poll();
        }

        public void setNextQueue(TimeQueue next) {
            this.nextQueue = next;
        }

        public TimeQueue next() {
            return this.nextQueue;
        }

        public void clear() {
            this.timeQueue.clear();
        }

        public int size() {
            return timeQueue.size();
        }

        public LinkedList<DateTime> getTimeQueue() {
            return timeQueue;
        }

        public void add(DateTime newTime) {
            // deque the expired time and add them into the next queue
            while (this.peek() != null) {
                if (newTime.minusMinutes(60).compareTo(this.peek()) >= 0) {
                    this.nextQueue.add(this.poll());
                } else {
                    break;
                }
            }
            this.timeQueue.add(newTime);
            // update lastMax
            if (this.timeQueue.size() > this.firstMaxCount) {
                this.firstMaxTime = this.peek();
                this.firstMaxCount = this.timeQueue.size();
            }
        }

        public void resetMax() {
            this.firstMaxCount = this.timeQueue.size();
            if (this.firstMaxCount > 0)
                this.firstMaxTime = this.timeQueue.peek();
        }


        public TimeNode getLastMax() {
            return new TimeNode(this.firstMaxTime, 60, firstMaxCount);
        }

        public boolean isMakeNode() {
            return this.makeNode;
        }


    }

    public class TimeQueueList {
        private ArrayList<TimeQueue> timeQueueList;
        private int size;

        public TimeQueueList(int size) {
            this.size = size;
            this.timeQueueList = new ArrayList<TimeQueue>();

            for (int i = 0; i <= size; i++) {
                this.timeQueueList.add(new TimeQueue());
            }
            for (int i = 0; i < size; i++) {
                this.timeQueueList.get(i).setNextQueue(timeQueueList.get(i + 1));
            }
        }

        // parameter: the new TimeNode object to be inserted
        // return:    all the maxTimeNode returned by each of the queue after insertion
        public List<TimeNode> insert(DateTime newTime) {
            List<TimeNode> nodes = new ArrayList<>();
            this.timeQueueList.get(0).add(newTime);
            for (int i = 0; i < size; i++) {
                if (this.timeQueueList.get(i).firstMaxTime != null) {
                    nodes.add(this.timeQueueList.get(i).getLastMax());
                    break;
                }
            }
            this.timeQueueList.get(size).clear();
            return nodes;
        }

        public List<TimeNode> finalDeque() {
            LinkedList<DateTime> lastQueue = timeQueueList.get(0).getTimeQueue();
            List<TimeNode> nodes = new ArrayList<>();
            int queuesize = lastQueue.size();
            for (int i = 0; i < queuesize; i++) {
                nodes.add(new TimeNode(lastQueue.peek(), 60, lastQueue.size()));
                lastQueue.poll();
            }
            return nodes;
        }

    }
}











