package metro;

import java.util.LinkedList;


class MetroLine {

    private String name;
    private LinkedList<Station> stations = new LinkedList<>();


    final Station depot = new Station("depot", this.name);

    MetroLine(String name) {
        this.name = name;
        this.stations.add(depot);
    }

    public LinkedList<Station> getStations() {
        return stations;
    }

    public void addStation(String name, String line) {

        this.stations.add(new Station(name, line));
    }

    public void addDepot(){

        this.stations.add(depot);

    }

    public void addStation(Station station, int position) {

        this.stations.add(position, station);

    }

    public void removeStation(Station station) {

        this.getStations().removeFirstOccurrence(station);

    }

    public String getName() {
        return name;
    }

    public Station findStationByName(String name) {

        for (Station station : this.getStations()) {
            if (station.getName().equals(name)) {
                return station;
            }
        }
        return null;
    }

    public void printMetroLine() {
        for (Station st : this.stations) {
            st.printStation();
        }


    }

    public void clearRoute() {
        for (Station station : this.stations) {
            station.setNumberInRoute(-1);
            station.setWeight(Integer.MAX_VALUE);
            station.setFrom(null);

        }
    }

}

