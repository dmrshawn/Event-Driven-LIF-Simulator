# Event-Driven Leaky Integrate-and-Fire Simulator

The goal of this project is to create a simulator that simulate neuron states, processes their spikes, propogates those spikes through synapses, and visualizes the membrane voltage over time. 

The core architecture will be built around Neuron objects that fire off signals. 
These signals are sent to other neurons through synapses, which will add their own weights to the spikes.

I also plan to graph each neuron's membrane voltage over time using JavaFX. 
By doing this, I will be able to visualize how different neuron spike inputs can create different neuron spike outputs.


Since this is primarily a project to familiarize myself with the basics of neuromorphic computing and Spiking Neural Networks, the primary focus of the codebase will be to have very readable code that I can easily follow and explain. Then, my next focus would be to make the simulator efficiently scale with large numbers of simulated neurons and synapses. I might also make another branch that's less readable but more efficient if I feel like it. idk.
