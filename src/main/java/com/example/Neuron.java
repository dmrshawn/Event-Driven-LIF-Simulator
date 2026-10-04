package com.example;

public class Neuron
{
    private final double membraneBaseVoltage = 1.0;
    private final double inverseMembraneTimeConstant = 200;
    private final double refractoryPeroidLength = 2;
    private final double threshold;

    private boolean inRefractory;
    private double refractoryPeriodLeft;

    private double membraneVoltage;


    public Neuron(double inputMV, double inputThreshold)
    {
        membraneVoltage = inputMV;
        threshold = inputThreshold;

        inRefractory = false;
    }


    public boolean tick(double step)
    {
        if(inRefractory)
        {
            refractoryPeriodLeft -= step;
            inRefractory = (refractoryPeriodLeft > 0);
        }
        else
        {
            decay(step);

            if(membraneVoltage >= threshold)
            {
                fire();
                return true;
            }
        }

        return false;
    }


    private void decay(double input)
    {
        //V = Vo * e^(-t/tau) --> V = Vo * e^(-t * 1/tau) --> V *= e^(-t * 1/tau)
        membraneVoltage *= fastExp(- input * inverseMembraneTimeConstant); 
    }

    private double fastExp(double input) 
    {
        // Schraudolph's algorithm
        final long tmp = (long) (15127753267988726L * input + 1072693248366914560L);
        return Double.longBitsToDouble(tmp);
    }


     private void fire()
    {
        membraneVoltage = membraneBaseVoltage;
        startRefractoryPeriod();
    }

    private void startRefractoryPeriod()
    {
        refractoryPeriodLeft = refractoryPeroidLength;
        inRefractory = true;
    }




    public boolean receiveSpike(double spikeWeight)
    {
        if(inRefractory)
            return false;
        
        membraneVoltage += spikeWeight;
        return true;
    }

    public boolean isRefractory()
    {
        return inRefractory;
    }

    public double getMembraneVoltage()
    {
        return membraneVoltage;
    }

    public double getThreshold()
    {
        return threshold;
    }

    public void reset()
    {
        membraneVoltage = membraneBaseVoltage;

        refractoryPeriodLeft = 0;
        inRefractory = false;
    }
}