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

    private Simulator createSimulator()
    {
        List<Neuron> neurons = List.of(
                new Neuron("Input Neuron"),
                new Neuron("Excitatory #1"),
                new Neuron("Excitatory #2"),
                new Neuron("Downstream Neuron")
        );

        List<Synapse> synapses = List.of(
                new Synapse(neurons.get(0), neurons.get(1), 20.0),
                new Synapse(neurons.get(0), neurons.get(2), 20.0),
                new Synapse(neurons.get(1), neurons.get(3), 25.0),
                new Synapse(neurons.get(2), neurons.get(3), -5.0)
        );

        return new Simulator(0.1, neurons, synapses);
    }

    private void runSimulator(Simulator simulator, int stepCount, int spikeInterval, int spikeOffset, double spikeMag)
    {
        spikeOffset = spikeOffset % spikeInterval;
        List<Neuron> neurons = simulator.getNeurons();

        for(int i = 0; i < stepCount; i++)
        {
            if(i % spikeInterval == spikeOffset)
                neurons.get(0).receiveSpike(spikeMag);
            
            simulator.step();
        }
    }

    @Override
    public void start(Stage stage)
    {
        Simulator simulator = createSimulator();

        runSimulator(simulator, 500, 25, 5, 20.0);

        ScatterChart<Number, Number> chart = simulator.getRasterGraph().createCombinedGraph();

        Scene scene = new Scene(chart, 800, 600);

        stage.setTitle("Neuron Spike Raster Graph");
        chart.getXAxis().setLabel("Simulation Time");
        chart.getYAxis().setLabel("Neuron Index");
        stage.setScene(scene);
        stage.show();
    }
}