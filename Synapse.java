import java.util.LinkedList;
import java.util.Queue;

public class Synapse
{
    private final double weight;
    private final double synapticDelay = 3;

    private final Neuron preSynapticNeuron;
    private final Neuron postSynapticNeuron;

    private final Queue<Double> pendingSpikes = new LinkedList<>();

    public Synapse(Neuron inputPreNeuron, Neuron inputPostNeuron, double inputWeight)
    {
        preSynapticNeuron = inputPreNeuron;
        postSynapticNeuron = inputPostNeuron;
        weight = inputWeight;
    }

    public void onSpike(double time)
    {
        pendingSpikes.add(time + synapticDelay);
    }

    public void update(double currentTime)
    {
        while (!pendingSpikes.isEmpty() && pendingSpikes.peek() <= currentTime)
        {
            pendingSpikes.poll();
            postSynapticNeuron.receiveSpike(weight);
        }
    }


    public Neuron getPresynapticNeuron()
    {
        return preSynapticNeuron;
    }

    public Neuron getPostsynapticNeuron()
    {
        return postSynapticNeuron;
    }
}