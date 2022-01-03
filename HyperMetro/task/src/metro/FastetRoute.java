package metro;

import java.util.*;

public class FastetRoute {

    private MetroMap map;
    private Station start;
    private Station end;
    private LinkedList<Station> route = new LinkedList<>();
    private PriorityQueue<Station> queue= new PriorityQueue<>(stationWeightComparator);
    private Set<Station> processed = new HashSet<>();

    FastetRoute (MetroMap map, Station start, Station end) {
        this.map = map;
        this.start = start;
        this.end = end;
        fillMap();
    }

    public static Comparator<Station> stationWeightComparator = new Comparator<Station>() {
        @Override
        public int compare(Station o1, Station o2) {
            return o1.getWeight() - o2.getWeight();
        }
    };

    public void fillMap() {
        this.map.clearRoute();
        this.start.setWeight(0);
        queue.add(start);
        while (!queue.isEmpty()) {
            Station current = queue.poll();
            processed.add(current);
            for (Station next : current.next()) {
                if (current.getWeight() + current.getTime() < next.getWeight()) {
                    next.setWeight(current.getWeight() + current.getTime());
                    next.setFrom(current);
                    queue.add(next);
                }
            }
            for (Station previous : current.prev()) {
                if (current.getWeight() + previous.getTime() < previous.getWeight()) {
                    previous.setWeight(current.getWeight() + previous.getTime());
                    previous.setFrom(current);
                    queue.add(previous);
                }
            }

            for (Station station : current.getTransfer()) {
                if (current.getWeight() + 5 < station.getWeight()) {
                    station.setWeight(current.getWeight() + 5);
                    station.setFrom(current);
                    queue.add(station);
                }

            }

        }
        Station current = end;
        this.route.addFirst(current);
        while (!current.getName().equals(start.getName())) {
            this.route.addFirst(current.getFrom());
            current = current.getFrom();
        }
        printRoute();

    }

    private void printRoute(){
        String line = start.getLine();
        for (Station station : route) {
            if (!station.getLine().equals(line)) {
                System.out.println("Transition to line " + station.getLine());
            }
            System.out.println(station.getName());
            line = station.getLine();
        }
        System.out.printf("Total: %d minutes in the way%n", this.end.getWeight());
    }
 }
