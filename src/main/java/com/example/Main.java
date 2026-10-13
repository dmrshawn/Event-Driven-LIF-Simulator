package com.example;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.ScatterChart;
import javafx.stage.Stage;

public class Main extends Application 
{
    public static void main(String[] args) 
    {
        launch(args);
    }

    @Override
    public void start(Stage stage)
    {
        ArrayList<Neuron> neurons = new ArrayList<>();

        neurons.add(new Neuron("Input Neuron")); // N0: input
        neurons.add(new Neuron("Excitatory #1")); // N1: excitatory
        neurons.add(new Neuron("Excitatory #2")); // N2: excitatory
        neurons.add(new Neuron("Downstream Neuron")); // N3: downstream

        ArrayList<Synapse> synapses = new ArrayList<>();

        synapses.add(new Synapse(neurons.get(0), neurons.get(1), 20.0));
        synapses.add(new Synapse(neurons.get(0), neurons.get(2), 20.0));
        synapses.add(new Synapse(neurons.get(1), neurons.get(3), 25.0));
        synapses.add(new Synapse(neurons.get(2), neurons.get(3), -5.0));


        Simulator simulator = new Simulator(0.1, neurons, synapses);

        for (int i = 0; i < 500; i++)
        {
            if(i%25 == 5)
                neurons.get(0).receiveSpike(20.0);

            simulator.step();
        }

        ScatterChart<Number, Number> chart = simulator.getRasterGraph().createCombinedGraph();

        Scene scene = new Scene(chart, 800, 600);

        stage.setTitle("Neuron Spike Raster Graph");
        chart.getXAxis().setLabel("Simulation Time");
        chart.getYAxis().setLabel("Neuron Index");
        stage.setScene(scene);
        stage.show();
    }
}