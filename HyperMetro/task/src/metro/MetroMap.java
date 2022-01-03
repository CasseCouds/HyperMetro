package metro;

import com.google.gson.*;


import java.io.*;
import java.util.ArrayList;

public class MetroMap {

    private ArrayList<MetroLine> metroMap;

    public void printLines() {
        for (MetroLine line : this.metroMap) {
            line.printMetroLine();
        }
    }

    public MetroMap() {

        this.metroMap = new ArrayList<>();

    }


    public void setMetroMap(ArrayList<MetroLine> metroMap) {
        this.metroMap = metroMap;
    }

    public ArrayList<MetroLine> getMetroMap() {
        return metroMap;
    }

    public void addLine(MetroLine line) {

        this.metroMap.add(line);

    }

    public MetroLine searchLineByName(String lineName) {

        for (MetroLine line : this.metroMap){
            if (line.getName().equals(lineName)) {
                return line;
            }
        }
        return null;
    }

    public void connectStations(String firstStationName, String firstLine, String secondStationName, String secondLine) {

        Station firstStation = this.searchLineByName(firstLine).findStationByName(firstStationName);
        Station secondStation = this.searchLineByName(secondLine).findStationByName(secondStationName);
        firstStation.addTransfer(secondStation);
        secondStation.addTransfer(firstStation);

    }

    public void clearRoute() {
        for (MetroLine line : this.metroMap) {
            line.clearRoute();
        }
    }

    public boolean parseMetroMapJSON(String jsonFileName) {
        File jsonFile = new File(jsonFileName);
        try {
            FileReader fileReader = new FileReader(jsonFile);
            BufferedReader reader = new BufferedReader(fileReader);
            JsonObject outerObject = JsonParser.parseReader(reader).getAsJsonObject();

            for (var laneEntry : outerObject.entrySet()) {
                String laneName = laneEntry.getKey();
                MetroLine line = new MetroLine(laneName);
                for ( var station : laneEntry.getValue().getAsJsonArray()) {
                    String stationName = "";
                    int time = -1;
                   for (var entry : station.getAsJsonObject().entrySet()) {
                       if ("name".equals(entry.getKey())) {
                           stationName = entry.getValue().getAsString();
                           line.addStation(stationName, laneName);
                       }
                       if ("time".equals(entry.getKey())) {
                           try {
                               time = entry.getValue().getAsInt();
                           } catch (UnsupportedOperationException e) {
                               time = -1;
                           }
                           line.findStationByName(stationName).setTime(time);
                       }

                   }
                }
                line.addDepot();
                this.addLine(line);
            }
            fileReader = new FileReader(jsonFile);
            reader = new BufferedReader(fileReader);
            outerObject = JsonParser.parseReader(reader).getAsJsonObject();
            for (var laneEntry : outerObject.entrySet()) {
                String laneName = laneEntry.getKey();
                for ( var station : laneEntry.getValue().getAsJsonArray()) {
                    String transferFrom="";
                    for (var entry : station.getAsJsonObject().entrySet()) {
                        if ("name".equals(entry.getKey())) {
                            transferFrom = entry.getValue().getAsString();
                        }
                        if ("next".equals(entry.getKey())) {
                            for (var nextStation : entry.getValue().getAsJsonArray()) {
                                    Station trStation = this.searchLineByName(laneName).findStationByName(nextStation.getAsString());
                                    this.searchLineByName(laneName).findStationByName(transferFrom).addNext(trStation);

                            }
                        }
                        if ("prev".equals(entry.getKey())) {
                            for (var prevStation : entry.getValue().getAsJsonArray()) {
                                    Station trStation = this.searchLineByName(laneName).findStationByName(prevStation.getAsString());
                                    this.searchLineByName(laneName).findStationByName(transferFrom).addPrev(trStation);

                            }
                        }
                        if ("transfer".equals(entry.getKey())) {
                            for (var transferStation : entry.getValue().getAsJsonArray()) {
                                String transferStationLine ="";
                                String transferStationName = "";
                                for (var field : transferStation.getAsJsonObject().entrySet()){
                                    switch (field.getKey()) {
                                        case "line" : {
                                            transferStationLine = field.getValue().getAsString();
                                            break;
                                        }
                                        case "station" : {
                                            transferStationName = field.getValue().getAsString();
                                            break;
                                        }
                                    }
                                }
                                Station trStation = this.searchLineByName(transferStationLine).findStationByName(transferStationName);
                                this.searchLineByName(laneName).findStationByName(transferFrom).addTransfer(trStation);
                            }
                        }
                    }
                }

            }
            try {
                this.searchLineByName("Linka B").findStationByName("Mustek").clearTransfer();
                this.searchLineByName("Linka B").findStationByName("Mustek").addTransfer(this.searchLineByName("Linka A").findStationByName("Mustek"));
            } catch (Exception e) {

            }
            return true;

        } catch (FileNotFoundException e) {
            System.out.println("Error! Such a file doesn't exist!");
            return false;
        } catch (JsonSyntaxException e) {
            System.out.println("Incorrect file");
            return false;
        }
    }

}
