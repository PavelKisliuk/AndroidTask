package com.epam;

import com.epam.api.GpsNavigator;
import com.epam.api.Path;
import com.epam.my.GraphPoint;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * This class app demonstrates how your implementation of {@link com.epam.api.GpsNavigator} is intended to be used.
 */
public class ExampleApp {

    public static void main(String[] args) {
        final GpsNavigator navigator = new StubGpsNavigator();
        navigator.readData("road_map.txt");

        final Path path = navigator.findPath("B", "C");
        System.out.println(path);
    }

    private static class StubGpsNavigator implements GpsNavigator {
        private List<GraphPoint> graphPointList;
        private List<String> listPath;
        private int fullCost;

        @Override
        public void readData(String filePath) {
            this.graphPointList = new ArrayList<>();
            try(BufferedReader reader = Files.newBufferedReader(Paths.get(filePath)))
            {
                if(!(reader.ready())) {
                    throw new IOException("Empty file!");
                }
                while(reader.ready()) {
                    String[] current = reader.readLine().split(" ");
                    if(current.length != 4) {
                        throw new IOException("Not 4 elements!");
                    }
                    String name = current[0];
                    String neighbour = current[1];
                    int length = Integer.valueOf(current[2]);
                    int cost = Integer.valueOf(current[3]);

                    GraphPoint graphPoint = new GraphPoint(name);
                    if((this.graphPointList.size() == 0) ||
                            !(this.graphPointList.contains(graphPoint))) {
                        graphPoint.addNeighbour(neighbour, length, cost);
                        this.graphPointList.add(graphPoint);
                    }else {
                        this.graphPointList.get(this.graphPointList.indexOf(graphPoint)).addNeighbour(neighbour, length, cost);
                    }
                }
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            }
        }

        @Override
        public Path findPath(String pointA, String pointB) {
            if(pointA.equals(pointB)) {
                return new Path(Collections.singletonList(pointA), 0);
            }
            GraphPoint startPoint = this.graphPointList.get(this.graphPointList.indexOf(new GraphPoint(pointA)));
            GraphPoint soughtPoint = this.graphPointList.get(this.graphPointList.indexOf(new GraphPoint(pointB)));
            startPoint.setMark(0);

            this.recursiveFind(startPoint, soughtPoint,0, 0, pointA);


            this.listPath = new ArrayList<>();
            this.pathAndCost(soughtPoint);
            if(this.fullCost == 0) {
                try {
                    throw new Exception("Path is absent!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Collections.reverse(this.listPath);

            return new Path(this.listPath, this.fullCost);
        }

        private void recursiveFind(GraphPoint startPoint, GraphPoint soughtPoint, int currentLength,
                                   int currentCost, String prevPoint)
        {
            if(!(startPoint.equals(soughtPoint))) {
                int i = 0;
                for(String s : startPoint.getNeighbourGroup()) {
                    GraphPoint newStart = this.graphPointList.get(this.graphPointList.indexOf(new GraphPoint(s)));

                    int newMark = currentLength + startPoint.getLengthGroupElement(i);
                    int newCost = currentCost + startPoint.getCostGroupElement(i) + startPoint.getLengthGroupElement(i);

                    if(newStart.getMark() == -1) {
                        newStart.setMark(newMark);
                        newStart.setPrevShortPoint(prevPoint);
                        newStart.setFullCost(newCost);
                        this.recursiveFind(newStart, soughtPoint, newMark, newCost, s);
                    }
                    else if((newStart.getMark() > newMark)) {
                        newStart.setMark(newMark);
                        newStart.setPrevShortPoint(prevPoint);
                        newStart.setFullCost(newCost);
                        this.recursiveFind(newStart, soughtPoint, newMark, newCost, s);
                    }
                    i++;
                }
            }
        }

        private void pathAndCost(GraphPoint startPoint)
        {
            String temp = startPoint.getName();
            this.listPath.add(temp);
            temp = startPoint.getPrevShortPoint();
            this.fullCost = startPoint.getFullCost();
            while (temp != null) {
                this.listPath.add(temp);
                startPoint = this.graphPointList.get(this.graphPointList.indexOf(
                        new GraphPoint(startPoint.getPrevShortPoint())));
                temp = startPoint.getPrevShortPoint();
            }
        }
    }
}
