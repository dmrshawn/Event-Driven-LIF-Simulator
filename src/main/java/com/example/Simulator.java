package com.example;

import java.util.ArrayList;
import java.util.List;

public class Simulator
{
    private final List<Neuron> neurons;
    private final List<Synapse> synapses;

    private double currentTime;
    private final double timeStep;

    public Simulator(double inputTimeStep)
    {
        neurons = new ArrayList<>();
        synapses = new ArrayList<>();
        currentTime = 0.0;
        timeStep = inputTimeStep;
    }

    public void addNeuron(Neuron neuron)
    {
        neurons.add(neuron);
    }

    public void addSynapse(Synapse synapse)
    {
        synapses.add(synapse);
    }

    public void step()
    {
        currentTime += timeStep;

        for (Neuron neuron : neurons)
            if (neuron.tick(timeStep))
                triggerSynapsesForNeuron(neuron);

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
}
