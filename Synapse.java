public class Synapse
{
    private final double weight;
    private final double synapticDelay = 0.0003;

    private Neuron preSynapticNeuron;
    private Neuron postSynapticNeuron;

    public Synapse(Neuron inputPreNeuron, Neuron inputPostNeuron, double inputWeight)
    {
        preSynapticNeuron = inputPreNeuron;
        postSynapticNeuron = inputPostNeuron;
        weight = inputWeight;
    }

    public void transmit() 
    {
        postSynapticNeuron.receiveSpike(weight);
    }

}