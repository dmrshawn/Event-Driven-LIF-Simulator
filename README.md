# Event-Driven Leaky Integrate-and-Fire Simulator

The goal of this project is to create a simulator that simulate neuron states, processes their spikes, propagates those spikes through synapses, and visualizes the membrane voltage over time. 

The core architecture will be built around Neuron objects that fire off signals. 
These signals are sent to other neurons through synapses, which will add their own weights to the spikes.

I also plan to graph each neuron's membrane voltage over time using JavaFX. 
By doing this, I will be able to visualize how different neuron spike inputs can create different neuron spike outputs.


Since this is primarily a project to familiarize myself with the basics of neuromorphic computing and Spiking Neural Networks, the primary focus of the codebase will be to have very readable code that I can easily follow and explain. Then, my next focus would be to make the simulator efficiently scale with large numbers of simulated neurons and synapses. I might also make another branch that's less readable but more efficient if I feel like it. idk.


# Current Progress:
I finished up a base implementation of the Synapse object, which contains a presynaptic and postsynaptic neuron. 
Essentially, a synapse is the intersection of tow neurons. When the presynaptic neuron reaches its threshold, it sends a spike to the postsynaptic neuron. However, there is a delay in the signal transitioning between the two neurons, represented by synapticDelay. The synapse also applies its own weight to the magnitude of the electrical spike sent from the presynaptic neuron to the postsynaptic neuron.

I also created the Simulation class to orchestrate the entire network's simulation. Each simulation has a list of neurons and synapses that it contains. It can step through a certain amount of time and simulate all neuron and synapse interactions.


# Future Progress:
I want to create an easy way to create neuron networks and visualize the sending of spikes, likely using JavaFX components. Graphs or another visual indicator of each neuron's membrane voltage over time are also on the radar.
Adding some unit tests would also be nice once we have a complete framework of the project.
I would also like to optimize the Simulator class so that processing a spike doesn't take O(n) time.
