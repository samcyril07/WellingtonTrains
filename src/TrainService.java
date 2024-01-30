import java.util.*;
import java.io.*;

public class TrainService {
    // Fields
    private TrainLine trainLine;
    private String trainID; // train line name + starting time of the train
    private List<Integer> times = new ArrayList<Integer>();

    // Constructor

    public TrainService(TrainLine line) {
        trainLine = line;
    }

    // getters
    public TrainLine getTrainLine() {
        return trainLine;
    }

    public String getTrainID() {
        return this.trainID;
    }

    public List<Integer> getTimes() {
        return Collections.unmodifiableList(times); // unmodifiable version of the list of times.
    }

    public void addTime(int time, boolean firstStop) {
        times.add(time);
        if (trainID == null && time != -1) {
            if (firstStop) {
                trainID = trainLine.getName() + "-" + time;
            } else {
                time += 10000;
                trainID = trainLine.getName() + "-" + time;
            }
        }
    }

    public int getStart() {
        for (int time : times) {
            if (time != -1) {
                return time;
            }
        }
        return -1;
    }

    public String toString() {
        if (trainID == null) {
            return trainLine.getName() + "-unknownStart";
        }
        int count = 0;
        for (int time : times) {
            if (time != -1)
                count++;
        }
        return trainID + " (" + count + " stops)";
    }

}
