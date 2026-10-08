package com.example;

import java.util.ArrayList;
import java.util.List;

public class Simulator
{
    private final List<Neuron> neurons;
    private final List<Synapse> synapses;

    private double currentTime;
    private final double timeStep;

    private final RosterGraph<Neuron> rosterGraph;


    public Simulator(double inputTimeStep)
    {
        neurons = new ArrayList<>();
        synapses = new ArrayList<>();
        rosterGraph = new RosterGraph<>();
        currentTime = 0.0;
        timeStep = inputTimeStep;
    }

    public void addNeuron(Neuron neuron)
    {
        neurons.add(neuron);
        rosterGraph.addObject(neuron);
    }

    public void addSynapse(Synapse synapse)
    {
        synapses.add(synapse);
    }

    public void step()
    {
        currentTime += timeStep;
        
        int n = neurons.size();

        for(int i = 0; i < n; i++)
        {
            Neuron neuron = neurons.get(i);

            if (neuron.tick(timeStep))
            {
                triggerSynapsesForNeuron(neuron);
                rosterGraph.addPoint(neuron, i, currentTime);
            }
        }   

        for (Synapse synapse : synapses)
            synapse.update(currentTime);
    }

    public void runSteps(int numberOfSteps)
    {
        for (int i = 0; i < numberOfSteps; i++)
            step();
    }

    private void triggerSynapsesForNeuron(Neuron neuron)
    {
        for (Synapse synapse : synapses)
            if (synapse.getPresynapticNeuron() == neuron)
                synapse.onSpike(currentTime);
    }

    public double getCurrentTime()
    {
        return currentTime;
    }

    public RosterGraph<Neuron> getRosterGraph()
    {
        return rosterGraph;
    }
}
