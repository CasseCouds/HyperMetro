package metro;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class Route {

    private MetroMap map;
    private Station start;
    private Station end;
    private LinkedList<Station> route = new LinkedList<>();
    private Queue<Station> queue= new LinkedList<>();

    public LinkedList<Station> getRoute() {
        return route;
    }

    Route (MetroMap map, Station start, Station end) {
        this.map = map;
        this.start = start;
        this.end = end;
        fillMap();
        findRoute();
        printRoute();
    }

    private void fillMap() {
        this.map.clearRoute();
        start.setNumberInRoute(0);
        this.queue.add(start);
        while (!queue.isEmpty()) {
            int routeNumber = queue.peek().getNumberInRoute() + 1;
            for (Station station : queue.peek().next()){
                if (station.getNumberInRoute() == -1) {
                    station.setNumberInRoute(routeNumber);
                    queue.add(station);
                }
            }
            for (Station station : queue.peek().prev()){
                if (station.getNumberInRoute() == -1) {
                    station.setNumberInRoute(routeNumber);
                    queue.add(station);
                }
            }
            for (Station station : queue.poll().getTransfer()){
                if (station.getNumberInRoute() == -1) {
                    station.setNumberInRoute(routeNumber);
                    queue.add(station);
                }
            }
        }

    }

    private void findRoute() {
        Station current = this.end;
        int routeNumber = current.getNumberInRoute();
        this.route.add(0,current);
        while (routeNumber != 0) {
            routeNumber -= 1;
            boolean nextSearch = true;

            for (Station previous : current.prev()) {
                if (nextSearch && previous.getNumberInRoute() == routeNumber) {
                    this.route.add(0, previous);
                    current = previous;
                    nextSearch = false;
                    break;
                }
            }

            for (Station next : current.next()) {
                if (nextSearch && next.getNumberInRoute() == routeNumber) {
                    this.route.add(0, next);
                    current = next;
                    nextSearch = false;
                    break;
                }
            }

            for (Station station : current.getTransfer()){
                if (nextSearch && station.getNumberInRoute() == routeNumber) {
                        this.route.add(0,station);
                        current = station;
                        break;
                    }
            }

        }
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
    }
}
