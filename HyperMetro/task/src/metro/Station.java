package metro;


import java.util.ArrayList;

class Station {

    private String name;
    private String line;
    private int numberInRoute = -1;
    private int time = -1;
    private int weight = Integer.MAX_VALUE;
    private ArrayList<Station> transfer;
    private ArrayList<Station> prev;
    private ArrayList<Station> next;
    private Station from = null;

    public Station(String name, String line) {

        this.name = name;
        this.line = line;
        this.transfer = new ArrayList<>();
        this.prev = new ArrayList<>();
        this.next = new ArrayList<>();
    }

    public int getNumberInRoute() {
        return numberInRoute;
    }

    public void setNumberInRoute(int numberInRoute) {
        this.numberInRoute = numberInRoute;
    }

    public Station getFrom() {
        return from;
    }

    public void setFrom(Station from) {
        this.from = from;
    }

    public ArrayList<Station> next() {
        return this.next;
    }


    public ArrayList<Station> prev() {

        return this.prev;

    }



    public void setName(String name, String line) {

        this.name = name;
        this.line = line;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public String getName() {

        return name;
    }

    public String getLine() {
        return line;
    }

    public ArrayList<Station> getTransfer() {
        return transfer;
    }

    public void addTransfer (Station transferStation) {
        this.transfer.add(transferStation);
    }

    public void addPrev (Station transferStation) {
        this.prev.add(transferStation);
    }

    public void addNext (Station transferStation) {
        this.next.add(transferStation);
    }

    public void printStation() {
        String result = this.name;
        for (Station st: this.transfer) {
            result = String.format("%s - %s (%s)",result, st.getName(), st.getLine());
        }
        System.out.println(result + this.getNumberInRoute());
    }

    public void setTransfer(ArrayList<Station> transfer) {
        this.transfer = transfer;
    }

    public void clearTransfer() {
        this.transfer.clear();
    }
}

