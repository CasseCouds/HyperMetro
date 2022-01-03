package metro;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {
    public static void main(String[] args) {
        final Pattern commandPattern = Pattern.compile("/((((append)|(add-head)|(remove))(( \"[\\w\\- .&]+\")|( [\\w\\-.&]+)){2})|" +
                "((output)(( \"[\\w\\- .&]+\")|( [\\w\\-.&]+)))|" +
                "(((connect)|(route)|(fastest-route))(( \"[\\w\\- .&]+\")|( [\\w\\-.&]+)){4})|" +
                "(exit))");
        final Pattern fourParametesCommand = Pattern.compile("/((connect)|(route)|(fastest-route))");
        final Pattern twoParametersCommand = Pattern.compile("/((append)|(add-head)|(remove))");
        final Pattern oneParameterCommand = Pattern.compile("/output");
        final Pattern parameter = Pattern.compile("(\\b([\\w\\-.&]+)\\b)|(\"\\b([\\w\\- .&]+)\")");
        final Pattern removeBracers = Pattern.compile("\\b([\\w\\- .&]+)\\b");
        MetroMap map = new MetroMap();
        map.parseMetroMapJSON(args[0]);
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader inputCommand = new BufferedReader(input);
        while (true) {
            try {
                String command = inputCommand.readLine();
                Matcher commandMatcher = commandPattern.matcher(command);
                if (commandMatcher.matches()){
                    Matcher twoParametersCommandMatcher = twoParametersCommand.matcher(command);
                    Matcher oneParameterCommandMatcher = oneParameterCommand.matcher(command);
                    Matcher fourParametesCommandMatcher = fourParametesCommand.matcher(command);
                    if (twoParametersCommandMatcher.find()) {
                        String action = twoParametersCommandMatcher.group();
                        String parameters = command.substring(twoParametersCommandMatcher.end());
                        Matcher parameterMatcher = parameter.matcher(parameters);
                        parameterMatcher.find();
                        String lineName = parameterMatcher.group();
                        Matcher bracersMatcher = removeBracers.matcher(lineName);
                        bracersMatcher.find();
                        lineName = bracersMatcher.group();
                        parameterMatcher.find();
                        String stationName = parameterMatcher.group();
                        bracersMatcher = removeBracers.matcher(stationName);
                        bracersMatcher.find();
                        stationName = bracersMatcher.group();
                        switch (action) {
                            case "/append"  : {
                                MetroLine line = map.searchLineByName(lineName);
                                if (line != null) {
                                    Station station = new Station(stationName, lineName);
                                    line.addStation(station,line.getStations().size() - 1);
                                } else {
                                    System.out.println("Invalid command");
                                }
                                break;
                            }
                            case "/add-head" : {
                                MetroLine line = map.searchLineByName(lineName);
                                if (line != null) {
                                    Station station = new Station(stationName, lineName);
                                    line.addStation(station, 1);
                                } else {
                                    System.out.println("Invalid command");
                                }
                                break;
                            }
                            case "/remove"  : {
                                MetroLine line = map.searchLineByName(lineName);
                                if (line != null) {
                                    Station station = line.findStationByName(stationName);
                                    if (station != null) {
                                        line.removeStation(station);
                                    } else {
                                        System.out.println("Invalid command");
                                    }
                                } else {
                                    System.out.println("Invalid command");
                                }
                                break;
                            }
                        }
                    } else if (oneParameterCommandMatcher.find()) {
                        String parameters = command.substring(oneParameterCommandMatcher.end());
                        Matcher parameterMatcher = parameter.matcher(parameters);
                        parameterMatcher.find();
                        String lineName = parameterMatcher.group();
                        Matcher bracersMatcher = removeBracers.matcher(lineName);
                        bracersMatcher.find();
                        lineName = bracersMatcher.group();
                        MetroLine line = map.searchLineByName(lineName);
                        if (line != null) {
                            line.printMetroLine();
                        } else {
                            System.out.println("Invalid command");
                        }
                    } else if (fourParametesCommandMatcher.find()) {
                        String action = fourParametesCommandMatcher.group();
                        String parameters = command.substring(fourParametesCommandMatcher.end());
                        Matcher parameterMatcher = parameter.matcher(parameters);
                        parameterMatcher.find();
                        String lineNameFirst = parameterMatcher.group();
                        Matcher bracersMatcher = removeBracers.matcher(lineNameFirst);
                        bracersMatcher.find();
                        lineNameFirst = bracersMatcher.group();
                        parameterMatcher.find();
                        String stationNameFirst = parameterMatcher.group();
                        bracersMatcher = removeBracers.matcher(stationNameFirst);
                        bracersMatcher.find();
                        stationNameFirst = bracersMatcher.group();
                        parameterMatcher.find();
                        String lineNameSecond = parameterMatcher.group();
                        bracersMatcher = removeBracers.matcher(lineNameSecond);
                        bracersMatcher.find();
                        lineNameSecond = bracersMatcher.group();
                        parameterMatcher.find();
                        String stationNameSecond = parameterMatcher.group();
                        bracersMatcher = removeBracers.matcher(stationNameSecond);
                        bracersMatcher.find();
                        stationNameSecond = bracersMatcher.group();
                        switch (action) {
                            case "/connect" : {
                                map.connectStations(stationNameFirst, lineNameFirst, stationNameSecond, lineNameSecond);
                                break;
                            }
                            case "/route" : {
                                Station start = map.searchLineByName(lineNameFirst).findStationByName(stationNameFirst);
                                Station end = map.searchLineByName(lineNameSecond).findStationByName(stationNameSecond);
                                new Route(map, start, end);
                                break;
                            }
                            case "/fastest-route" : {
                                Station start = map.searchLineByName(lineNameFirst).findStationByName(stationNameFirst);
                                Station end = map.searchLineByName(lineNameSecond).findStationByName(stationNameSecond);
                                new FastetRoute(map, start, end);
                                break;
                            }
                        }

                    } else {
                        break;
                    }
                } else {
                    System.out.println("Invalid command");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

