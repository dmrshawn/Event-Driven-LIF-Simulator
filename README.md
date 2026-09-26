# Event-Driven Leaky Integrate-and-Fire Simulator

The goal of this project is to create a simulator that simulate neuron states, processes their spikes, propagates those spikes through synapses, and visualizes the membrane voltage over time. 

The core architecture will be built around Neuron objects that fire off signals. 
These signals are sent to other neurons through synapses, which will add their own weights to the spikes.

I also plan to graph each neuron's membrane voltage over time using JavaFX. 
By doing this, I will be able to visualize how different neuron spike inputs can create different neuron spike outputs.


Since this is primarily a project to familiarize myself with the basics of neuromorphic computing and Spiking Neural Networks, the primary focus of the codebase will be to have very readable code that I can easily follow and explain. Then, my next focus would be to make the simulator efficiently scale with large numbers of simulated neurons and synapses. I might also make another branch that's less readable but more efficient if I feel like it. idk.


# Current Progress:
I finished up a base implementation of the Neuron object that tracks its voltage and refractory period. The neuron can also simulate time steps with the tick() function, membrane potential decay with the decay() function, and sending signals with the fire() function. Currently, the actual sending of signals has not been implemented yet. 


# Future Progress:
I'm working on creating a Synapse object that contains two neurons and a weight. The synapse will transmit electrical impulses from the pre-synaptic neuron to the post-synaptic neuron while adding its own weight into the mix.
