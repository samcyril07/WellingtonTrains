import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrainLine {
	// Fields
	private String name;
	private List<Station> stations = new ArrayList<Station>(); // list of stations on the line
	private List<TrainService> trainServices = new ArrayList<TrainService>(); // set of TrainServices running on the
																				// line

	// Constructor
	public TrainLine(String name) {
		this.name = name;
	}

	public void addTrainService(TrainService train) {
		trainServices.add(train);
	}

	public void addStation(Station station) {
		stations.add(station);
	}

	// Getters
	public String getName() {
		return name;
	}

	public List<Station> getStations() {
		return Collections.unmodifiableList(stations); // an unmodifiable version of the list of stations
	}

	public List<TrainService> getTrainServices() {
		return Collections.unmodifiableList(trainServices); // an unmodifiable version of the list of trainServices
	}

	public String toString() {
		return (name + " (" + stations.size() + " stations, " + trainServices.size() + " services)");
	}

}
