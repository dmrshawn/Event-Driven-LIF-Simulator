public class Spike
{
    public final long spikeLength;
    public final Neuron targetNeuron;
    public final double weight;

    public Spike(long inputTime, Neuron inputNeuron, double inputWeight) 
    {
        spikeLength = inputTime;
        targetNeuron = inputNeuron;
        weight = inputWeight;
    }
}