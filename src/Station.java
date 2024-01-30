import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Station {

	private String name;
	private int zone; // fare zone
	private double distance; // distance from Wellington
	private Set<TrainLine> trainLines = new HashSet<TrainLine>();

	public Station(String name, int zone, double dist) {
		this.name = name;
		this.zone = zone;
		this.distance = dist;
	}

	public Station(String name) {
		// super();
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public int getZone() {
		return this.zone;
	}

	public void addTrainLine(TrainLine line) {
		trainLines.add(line);
	}

	public Set<TrainLine> getTrainLines() {
		return Collections.unmodifiableSet(trainLines); // Return an unmodifiable version of the set of train lines.
	}

	public String toString() {
		return name + " (zone " + zone + ", " + trainLines.size() + " lines)";
	}

}
