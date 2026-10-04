package com.example;

public class Main
{
    public static void main(String[] args) 
    {
        System.out.println("test");

        Simulator sim = new Simulator(1.0);
        Neuron preNeuron = new Neuron(0, 3);
        Neuron postNeuron = new Neuron(0, 4);
        Synapse synapse = new Synapse(preNeuron, postNeuron, 10);
    }
}