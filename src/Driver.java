import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import ecs100.UI;

public class Driver {

	private Map<String, Station> stations = new HashMap<String, Station>();
	private Map<String, TrainLine> trainLines = new HashMap<String, TrainLine>();
	private List<TrainService> services = new ArrayList<TrainService>();

	public void addStation() {
		String filename = "stations.data";
		try {
			Scanner sc = new Scanner(new File(filename));
			while (sc.hasNext()) {
				while (sc.hasNext()) {
					String s = sc.next();
					int i = sc.nextInt();
					double d = sc.nextDouble();
					Station stat = new Station(s, i, d);
					stations.put(s, stat);
					break;
				}
				sc.hasNextLine();
			}
			sc.close();
		} catch (FileNotFoundException e) {
			UI.printf("Error loading file: %s%n", e);
		}

	}

	public void addTrainLineAndService() {
		UI.clearText();
		String filename = "train-lines.data";
		try {
			Scanner sc = new Scanner(new File(filename));
			while (sc.hasNext()) {
				while (sc.hasNext()) {
					String s = sc.next();
					TrainLine t = new TrainLine(s);
					Scanner scm = new Scanner(new File(s + "-stations.data"));
					while (scm.hasNext()) {
						String d = scm.nextLine();
						Station sam = new Station(d);
						for (Entry<String, Station> e : stations.entrySet()) {
							if (e.getKey().equalsIgnoreCase(d)) {
								e.getValue().addTrainLine(t);
							}
						}
						sam.addTrainLine(t);
						t.addStation(sam);
					}
					Scanner sccm = new Scanner(new File(s + "-services.data"));
					while (sccm.hasNextLine()) {
						TrainService ts = new TrainService(t);
						String timings = sccm.nextLine();
						Scanner scan = new Scanner(timings);
						boolean check = true;
						while (scan.hasNext()) {
							int time = scan.nextInt();
							ts.addTime(time, true);
							check = false;
						}
						t.addTrainService(ts);
						services.add(ts);
					}

					trainLines.put(s, t);
				}
				sc.hasNextLine();
			}
			sc.close();
		} catch (FileNotFoundException e) {
			UI.printf("Error loading file: %s%n", e);
		}

	}

	public void printStation() {

		for (Entry<String, Station> e : stations.entrySet()) {
			UI.println(e.getValue());
		}
		UI.println("------------------------------------------------------------------------------------");
	}

	public void printTrains() {

		for (Entry<String, Station> e : stations.entrySet()) {
			List<TrainLine> lines = new ArrayList<TrainLine>();
			UI.println(e.getValue().getName());
			lines.addAll(e.getValue().getTrainLines());
			for (int j = 0; j < lines.size(); j++) {
				UI.println(j);
			}

		}
		UI.println("------------------------------------------------------------------------------------");
	}

	public void printTrainLine() {
		UI.clearText();
		for (Entry<String, TrainLine> e : trainLines.entrySet()) {
			UI.println(e.getValue());
		}
		UI.println("------------------------------------------------------------------------------------");
	}

	public void sortTrain() {
		UI.clearText();
		String Question = UI.askString("Please enter the station name:");
		boolean check = true;
		for (Entry<String, Station> e : stations.entrySet()) {
			List<TrainLine> lines = new ArrayList<TrainLine>();
			if (e.getKey().equalsIgnoreCase(Question)) {
				lines.addAll(e.getValue().getTrainLines());
				for (TrainLine t : lines) {
					UI.println(t);
				}
				check = false;
			}

		}
		if (check == true) {
			UI.println("The Given station does not exist");
		}
		UI.println("------------------------------------------------------------------------------------");
	}

	public void listStationsInLine() {
		List<Station> stations = new ArrayList<Station>();
		UI.clearText();
		String Question = UI
				.askString("Please enter name of the trainline to view the stations that it traverses through:");
		boolean check = true;
		for (Entry<String, TrainLine> e : trainLines.entrySet()) {
			if (e.getKey().equalsIgnoreCase(Question)) {
				for (int i = 0; i < e.getValue().getStations().size(); i++) {
					stations.add(e.getValue().getStations().get(i));
				}
				check = false;

			}
		}
		if (check == true) {
			UI.println("The Given line does not exist");
		}
		for (Station s : stations) {
			UI.println(s.getName());
		}
		UI.println("------------------------------------------------------------------------------------");
	}

	public void printTrainService(String s) {
		UI.clearText();
		String ques = s;
		List<String> timings = new ArrayList<String>();
		for (Entry<String, TrainLine> e : trainLines.entrySet()) {

			if (e.getValue().getStations().toString().toLowerCase().contains(ques)) {
				for (int i = 0; i < e.getValue().getTrainServices().size(); i++) {
					for (int j = 0; j < e.getValue().getTrainServices().get(i).getTrainLine().getStations()
							.size(); j++) {
						if (e.getValue().getTrainServices().get(i).getTrainLine().getStations().get(j).getName()
								.equalsIgnoreCase(ques))

						{
							timings.add(e.getKey() + " line is scheduled to arrive at " + s + " at "
									+ e.getValue().getTrainServices().get(i).getTimes().get(j).toString());
						}

					}

				}
			}
		}

		for (String q : timings) {
			if (!q.contains("-1"))
				UI.println(q);
		}
		UI.println("------------------------------------------------------------------------------------");
	}

	public void findLine() {
		UI.clearText();
		String question1 = UI.askString("Please enter the boarding station:");
		String question2 = UI.askString("Please enter the destination station");
		boolean check = true;
		String test = "";
		int first = 0;
		int second = 0;
		int diff_first = 0;
		int diff_second = 0;
		for (Entry<String, TrainLine> e : trainLines.entrySet()) {
			if (e.getValue().getStations().toString().toLowerCase().contains(question1)
					&& e.getValue().getStations().toString().toLowerCase().contains(question2)) {
				for (int i = 0; i < e.getValue().getStations().size(); i++) {
					int ss = e.getValue().getStations().size();
					test = e.getValue().getStations().get(i).toString().toLowerCase();
					String[] splitt = test.split(" ");
					if (splitt[0].equalsIgnoreCase(question1)) {
						first = i;
						diff_first = ss - first;
					}
					if (splitt[0].equalsIgnoreCase(question2)) {
						second = i;
						diff_second = ss - second;
					}
				}

				if (diff_first > diff_second) {
					UI.println(e.getKey());
					check = false;
				}

			}

		}
		if (check == true) {
			UI.println("No TrainLines found");
		}
		UI.println("------------------------------------------------------------------------------------");
	}

	public void addZoneFair() {
		UI.clearText();
		String question1 = UI.askString("Please enter the boarding station:");
		String question2 = UI.askString("Please enter the destination station");
		int time = UI.askInt("Please enter the time");
		boolean check = true;
		String test = "";
		int first = 0;
		int second = 0;
		int diff_first = 0;
		int diff_second = 0;
		List<Station> temp_stations = new ArrayList<Station>();
		for (Entry<String, TrainLine> e : trainLines.entrySet()) {
			if (e.getValue().getStations().toString().toLowerCase().contains(question1)
					&& e.getValue().getStations().toString().toLowerCase().contains(question2)) {
				for (int i = 0; i < e.getValue().getStations().size(); i++) {
					int ss = e.getValue().getStations().size();
					test = e.getValue().getStations().get(i).toString().toLowerCase();
					String[] splitt = test.split(" ");
					if (splitt[0].equalsIgnoreCase(question1)) {
						first = i;
						diff_first = ss - first;
					}
					if (splitt[0].equalsIgnoreCase(question2)) {
						second = i;
						diff_second = ss - second;
					}
				}

				if (diff_first > diff_second) {
					check = false;
					for (int i = 0; i < e.getValue().getTrainServices().size(); i++) {
						temp_stations.addAll(e.getValue().getStations());
						if (e.getValue().getTrainServices().get(i).getTimes().get(first) >= time) {
							if ((e.getValue().getTrainServices().get(i).getTimes().get(first) != -1)
									&& (e.getValue().getTrainServices().get(i).getTimes().get(second) != -1)) {
								UI.println("TrainLine name:" + e.getKey());
								UI.println("Boarding station: " + question1);
								UI.println("Boarding time: "
										+ e.getValue().getTrainServices().get(i).getTimes().get(first));
								UI.println("Arrival station: " + question2);
								UI.println("Arrival Time "
										+ e.getValue().getTrainServices().get(i).getTimes().get(second));
								break;
							} else
								continue;
						}

					}

					Set<Integer> s = new HashSet<Integer>();
					int d = 0;
					for (Entry<String, Station> e1 : stations.entrySet()) {
						for (int i = first; i <= second; i++) {
							if (e1.getValue().getName().equalsIgnoreCase(temp_stations.get(i).getName())) {
								s.add(e1.getValue().getZone());
							}
						}
					}
					UI.println("Number of fair zones the trip goes: " + s.size());
				}
			}

		}
		if (check == true) {
			UI.println("No TrainLines found");
		}
		UI.println("-------------------------------------------------------------------------------------");
	}

	public void addImage() {
		UI.drawImage("system-map.png", 0, 0);
	}

	public void doMouse(String action, double x, double y) {

		if (action.equals("clicked")) {

			if (x > 315 && x < 390) {
				if (y > 658 && y < 676) {
					printTrainService("wellington");
				}
			}
		}
		if (action.equals("clicked")) {
			if (x > 65 && x < 125) {
				if (y > 440 && y < 455) {
					printTrainService("johnsonville");
				}
			}

		}
		if (action.equals("clicked")) {
			if (x > 90 && x < 120) {
				if (y > 470 && y < 480) {
					printTrainService("raroa");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 60 && x < 120) {
				if (y > 485 && y < 500) {
					printTrainService("khandallah");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 60 && x < 120) {
				if (y > 485 && y < 500) {
					printTrainService("khandallah");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 80 && x < 120) {
				if (y > 510 && y < 525) {
					printTrainService("box-hill");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 50 && x < 120) {
				if (y > 530 && y < 545) {
					printTrainService("simla-crescent");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 50 && x < 120) {
				if (y > 550 && y < 565) {
					printTrainService("awarua-street");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 80 && x < 120) {
				if (y > 570 && y < 585) {
					printTrainService("ngaio");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 80 && x < 120) {
				if (y > 590 && y < 705) {
					printTrainService("crofton-downs");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 175 && x < 225) {
				if (y > 310 && y < 325) {
					printTrainService("waikanae");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 150 && x < 220) {
				if (y > 330 && y < 345) {
					printTrainService("paraparaumu");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 160 && x < 220) {
				if (y > 350 && y < 370) {
					printTrainService("paekakariki");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 160 && x < 220) {
				if (y > 375 && y < 390) {
					printTrainService("pukerua-bay");
				}
			}
		}
		if (action.equals("clicked")) {
			UI.println("x: " + x + "y: " + y);
			if (x > 160 && x < 220) {
				if (y > 395 && y < 410) {
					printTrainService("plimmerton");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 190 && x < 220) {
				if (y > 420 && y < 435) {
					printTrainService("mana");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 170 && x < 220) {
				if (y > 445 && y < 457) {
					printTrainService("paremata");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 180 && x < 220) {
				if (y > 463 && y < 477) {
					printTrainService("porirua");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 170 && x < 220) {
				if (y > 485 && y < 500) {
					printTrainService("kenepuru");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 180 && x < 220) {
				if (y > 505 && y < 520) {
					printTrainService("linden");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 190 && x < 220) {
				if (y > 530 && y < 545) {
					printTrainService("tawa");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 170 && x < 220) {
				if (y > 550 && y < 565) {
					printTrainService("redwood");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 155 && x < 220) {
				if (y > 570 && y < 590) {
					printTrainService("takapu-road");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 155 && x < 220) {
				if (y > 570 && y < 590) {
					printTrainService("takapu-road");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 304 && x < 336) {
				if (y > 456 && y < 468) {
					printTrainService("melling");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 304 && x < 346) {
				if (y > 485 && y < 510) {
					printTrainService("western-hutt");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 408 && x < 455) {
				if (y > 43 && y < 55) {
					printTrainService("masterton");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 415 && x < 475) {
				if (y > 70 && y < 85) {
					printTrainService("renall-street");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 414 && x < 450) {
				if (y > 97 && y < 110) {
					printTrainService("solway");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 413 && x < 460) {
				if (y > 123 && y < 136) {
					printTrainService("carterton");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 414 && x < 460) {
				if (y > 150 && y < 165) {
					printTrainService("matarawa");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 413 && x < 460) {
				if (y > 175 && y < 190) {
					printTrainService("woodside");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 413 && x < 470) {
				if (y > 200 && y < 215) {
					printTrainService("featherston");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 413 && x < 460) {
				if (y > 225 && y < 240) {
					printTrainService("maymorn");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 417 && x < 470) {
				if (y > 252 && y < 264) {
					printTrainService("upper-hutt");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 420 && x < 480) {
				if (y > 279 && y < 292) {
					printTrainService("wallaceville");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 420 && x < 470) {
				if (y > 305 && y < 320) {
					printTrainService("trentham");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 420 && x < 480) {
				if (y > 330 && y < 345) {
					printTrainService("heretaunga");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 420 && x < 485) {
				if (y > 355 && y < 370) {
					printTrainService("silverstream");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 420 && x < 485) {
				if (y > 383 && y < 395) {
					printTrainService("manor-park");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 420 && x < 460) {
				if (y > 410 && y < 420) {
					printTrainService("pomare");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 420 && x < 445) {
				if (y > 435 && y < 448) {
					printTrainService("taita");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 420 && x < 463) {
				if (y > 458 && y < 473) {
					printTrainService("wingate");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 412 && x < 450) {
				if (y > 492 && y < 502) {
					printTrainService("naenae");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 390 && x < 420) {
				if (y > 515 && y < 527) {
					printTrainService("epuni");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 360 && x < 407) {
				if (y > 538 && y < 550) {
					printTrainService("waterloo");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 344 && x < 381) {
				if (y > 560 && y < 575) {
					printTrainService("woburn");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 319 && x < 341) {
				if (y > 580 && y < 590) {
					printTrainService("ava");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 310 && x < 345) {
				if (y > 605 && y < 617) {
					printTrainService("petone");
				}
			}
		}
		if (action.equals("clicked")) {

			if (x > 310 && x < 365) {
				if (y > 635 && y < 646) {
					printTrainService("ngauranga");
				}
			}
		}

	}

	public void clickMap() {
		UI.clearText();
		UI.println(
				"Please click on station name in map to check the trainlines and it's time that passess through the clicked station");
		UI.setMouseListener(this::doMouse);
	}

	List<TrainLine> lines = new ArrayList<TrainLine>();
	TreeMap<Integer, String> optimizedLine = new TreeMap<Integer, String>();

	public void test(List<TrainLine> line, String source, String destination, int index, int time) {

		if (index > 0) {
			int koi = 0;
			for (int i = 0; i < line.get(index - 1).getStations().size(); i++) {
				// UI.println(line.get(index - 1).getStations().get(i).getName());
				if (line.get(index - 1).getStations().get(i).getName().equalsIgnoreCase(destination)) {
					koi = i;
				}
			}
			for (int j = 0; j < line.get(index - 1).getTrainServices().size(); j++) {
				if (line.get(index - 1).getTrainServices().get(j).getTimes().get(koi) >= time) {
					UI.println(line.get(index - 1).getTrainServices().get(0).getTrainLine().toString());
					UI.println(line.get(index - 1).getTrainServices().get(j).getTimes().get(koi));
					optimizedLine.put(line.get(index - 1).getTrainServices().get(j).getTimes().get(koi),
							line.get(index - 1).getTrainServices().get(0).getTrainLine().toString());
					break;
				}
			}
			index = index - 1;
			test(line, source, destination, index, time);

		} else {

			UI.println("Best Trip is " + optimizedLine.firstEntry());
		}

	}

	public void optimizedTrip() {
		UI.clearText();
		List<TrainLine> lines = new ArrayList<TrainLine>();
		String question1 = UI.askString("Please enter the boarding station:");
		String question2 = UI.askString("Please enter the destination station");
		int time = UI.askInt("Please enter the time");
		UI.println("Inside optimizedTrip");
		boolean check = true;
		String test = "";
		int first = 0;
		int second = 0;
		int diff_first = 0;
		int diff_second = 0;
		List<Station> temp_stations = new ArrayList<Station>();
		for (Entry<String, TrainLine> e : trainLines.entrySet()) {
			if (e.getValue().getStations().toString().toLowerCase().contains(question1)
					&& e.getValue().getStations().toString().toLowerCase().contains(question2)) {
				for (int i = 0; i < e.getValue().getStations().size(); i++) {
					int ss = e.getValue().getStations().size();
					test = e.getValue().getStations().get(i).toString().toLowerCase();
					String[] splitt = test.split(" ");
					if (splitt[0].equalsIgnoreCase(question1)) {
						first = i;
						diff_first = ss - first;
					}
					if (splitt[0].equalsIgnoreCase(question2)) {
						second = i;
						diff_second = ss - second;
					}
				}

				if (diff_first > diff_second) {
					check = false;
					for (int i = 0; i < e.getValue().getTrainServices().size(); i++) {
						temp_stations.addAll(e.getValue().getStations());
						if (e.getValue().getTrainServices().get(i).getTimes().get(first) >= time) {
							if ((e.getValue().getTrainServices().get(i).getTimes().get(first) != -1)
									&& (e.getValue().getTrainServices().get(i).getTimes().get(second) != -1)) {
								lines.add(e.getValue());
//								UI.println("TrainLine name:" + e.getKey());
//								UI.println("Boarding station: " + question1);
//								UI.println("Boarding time: "
//										+ e.getValue().getTrainServices().get(i).getTimes().get(first));
//
//								UI.println("Arrival station: " + question2);
//								UI.println("Arrival Time "
//										+ e.getValue().getTrainServices().get(i).getTimes().get(second));
								break;
							} else
								continue;
						}

					}
				}
			}
		}
		test(lines, question1, question2, lines.size(), time);

	}

	public Driver() {
		UI.initialise();
		addStation();
		addTrainLineAndService();
		addImage();
		UI.addButton("Print Stations", this::printStation);
		UI.addButton("Print trainLines", this::printTrainLine);
		UI.addButton("Find TrainLines", this::sortTrain);
		UI.addButton("Find stations in a Line", this::listStationsInLine);
		UI.addButton("Find trains by entering stations", this::findLine);
		UI.addButton("Plan Trip", this::addZoneFair);
		UI.addButton("Find station schedules", this::clickMap);
		UI.addButton("Find shortest trip", this::optimizedTrip);

	}

	public static void main(String[] args) {
		new Driver();
	}

}
