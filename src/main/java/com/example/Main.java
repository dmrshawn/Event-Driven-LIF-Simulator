package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.chart.ScatterChart;

public class Main extends Application 
{
    public static void main(String[] args) 
    {
        launch(args);
    }

    @Override
    public void start(Stage stage)
    {
        Simulator simulator = new Simulator(0.1);

        Neuron neuron1 = new Neuron();
        Neuron neuron2 = new Neuron();
        Neuron neuron3 = new Neuron();

        simulator.addNeuron(neuron1);
        simulator.addNeuron(neuron2);
        simulator.addNeuron(neuron3);

        simulator.runSteps(1000);

        ScatterChart<Number, Number> chart = simulator.getRosterGraph().createGraph();

        Scene scene = new Scene(chart, 800, 600);

        stage.setTitle("Neuron Spike Roster");
        stage.setScene(scene);
        stage.show();
    }
}