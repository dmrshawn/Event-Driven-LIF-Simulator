package com.example;

import java.util.*;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;

public class RosterGraph<T>
{

    ScatterChart<Number, Number> createGraph() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    record Point(int neuronIndex, double time) {}
    
    private final Map<T, List<Point>> graphs = new LinkedHashMap<>();


    public ScatterChart<Number, Number> createGraph(T object)
    {
        ScatterChart<Number, Number> chart = makeNewScatterChart("Neuron Index", "Time", "Roster Graph");

        List<Point> points = graphs.get(object);

        if(points == null)
            System.out.println("No points to print!");
        else
            chart.getData().add(createSeries(String.valueOf(object), points));
        
        return chart;
    }

    public ScatterChart<Number, Number> createCombinedGraph() 
    { 
        ScatterChart<Number, Number> chart = makeNewScatterChart("Neuron Index", "Time", "Roster Graphs");
        
        for (Map.Entry<T, List<Point>> entry : graphs.entrySet()) 
            chart.getData().add(createSeries(String.valueOf(entry.getKey()), entry.getValue()));
        
        return chart;
    }

    private ScatterChart<Number, Number> makeNewScatterChart(String xAxisName, String yAxisName, String title)
    {
        NumberAxis xAxis = new NumberAxis(); 
        NumberAxis yAxis = new NumberAxis(); 
        xAxis.setLabel(xAxisName); 
        yAxis.setLabel(yAxisName); 
        
        ScatterChart<Number, Number> outputChart = new ScatterChart<>(xAxis, yAxis); 

        outputChart.setTitle(title);
        return outputChart;
    }

    private XYChart.Series<Number, Number> createSeries(String name, List<Point> points)
    {
        List<Point> sortedPoints = new ArrayList<>(points);
        sortedPoints.sort( Comparator.comparingInt(Point::neuronIndex) );

        XYChart.Series<Number, Number> series = new XYChart.Series<>(); 
        series.setName(name);
            
        for (Point point : sortedPoints)
            series.getData().add( new XYChart.Data<>( point.time, point.neuronIndex)); 

        return series;
    }


    public void addObject(T object)
    {
        graphs.putIfAbsent(object, new ArrayList<>());
    }

    public void addPoint(T object, int index, double time)
    {
        graphs.computeIfAbsent(object, k -> new ArrayList<>()).add(new Point(index, time));
    }

    public List<Point> getPoints(T object)
    {
        return Collections.unmodifiableList(graphs.getOrDefault(object, Collections.emptyList()));
    }

    public void removeObject(T object)
    {
        graphs.remove(object);
    }

    public void clear()
    {
        graphs.clear();
    }
}


