import com.example.Neuron;
import com.example.Simulator;
import com.example.Synapse;
import java.util.Locale;

public class SimulatorTest 
{
    private static int passed = 0;
    private static int failed = 0;

    private static final double EPS = 1e-9;

    public static void main(String[] args) 
    {
        Locale.setDefault(Locale.US);

        System.out.println("========================================");
        System.out.println(" Event-Driven LIF Simulator Test Suite");
        System.out.println("========================================");


        run("Neuron starts with supplied voltage", SimulatorTest::testNeuronInitialState);
        run("Neuron exposes threshold", SimulatorTest::testNeuronThreshold);
        run("Neuron accepts positive spike", SimulatorTest::testNeuronReceiveSpike);
        run("Neuron accepts negative spike", SimulatorTest::testNeuronReceiveInhibitorySpike);
        run("Neuron doesn't spike during refractory period", SimulatorTest::testNeuronRefractoryRejectsSpike);
        run("Neuron membrane voltage decays between ticks", SimulatorTest::testNeuronDecay);
        run("Neuron fires when membranve voltage exceeds threshold", SimulatorTest::testNeuronFiresAtThreshold);
        run("Neuron enters a refractory period after firing", SimulatorTest::testNeuronRefractoryAfterFire);
        run("Neuron stays in refractoryperiod  until period expires", SimulatorTest::testNeuronRefractoryDuration);
        run("Neuron is able to receive spike after refractory period", SimulatorTest::testNeuronRecovers);
        run("Neuron can be reset to base stats", SimulatorTest::testNeuronReset);

        run("Synapse stores pre and post neurons", SimulatorTest::testSynapseConnections);
        run("Synapse delays spike delivery by internal amount", SimulatorTest::testSynapseDelay);
        run("Synapse delivers spike at or after delay", SimulatorTest::testSynapseDelivery);
        run("Synapse can queue multiple spikes", SimulatorTest::testSynapseMultipleSpikes);

        run("Simulator starts at time zero", SimulatorTest::testSimulatorInitialTime);
        run("Simulator advances by timestep", SimulatorTest::testSimulatorStepTime);
        run("Simulator runSteps advances repeatedly", SimulatorTest::testSimulatorRunSteps);
        run("Simulator triggers synapse when presynaptic neuron fires", SimulatorTest::testSimulatorTriggersSynapse);
        run("Simulator only triggers specific synapses", SimulatorTest::testSimulatorIgnoresUnrelatedSynapse);
        run("Simulator propagates spike through two neurons", SimulatorTest::testSimulatorTwoNeuronPropagation);
        run("Simulator doesn't override postsynaptic neuron's refractory state", SimulatorTest::testSimulatorPostsynapticRefractory);
        run("Simulator supports chains of neurons", SimulatorTest::testSimulatorThreeNeuronChain);


        System.out.println("\n========================================");
        System.out.printf("RESULT: %d tests passed, %d tests failed%n", passed, failed);
        System.out.println("========================================");
        System.out.flush();
        if (failed > 0)
            System.exit(1);
    }


    private static void testNeuronInitialState() 
    {
        Neuron neuron = new Neuron(0.25, 1.0);

        checkClose("initial membrane voltage", 0.25, neuron.getMembraneVoltage());
        checkFalse("initial refractory state", neuron.isRefractory());
    }

    private static void testNeuronThreshold() 
    {
        Neuron neuron = new Neuron(0.0, 0.75);

        checkClose("threshold", 0.75, neuron.getThreshold());
    }

    private static void testNeuronReceiveSpike() 
    {
        Neuron neuron = new Neuron(0.2, 1.0);

        checkTrue("spike accepted", neuron.receiveSpike(0.3));
        checkClose("voltage after spike", 0.5, neuron.getMembraneVoltage());
    }

    private static void testNeuronReceiveInhibitorySpike() 
    {
        Neuron neuron = new Neuron(0.8, 1.0);

        checkTrue("inhibitory spike accepted", neuron.receiveSpike(-0.3));
        checkClose("voltage after inhibitory spike", 0.5, neuron.getMembraneVoltage());
    }

    private static void testNeuronRefractoryRejectsSpike() 
    {
        Neuron neuron = new Neuron(1.0, 1e-238);

        checkTrue("neuron fired", neuron.tick(0.001));
        checkTrue("neuron is refractory", neuron.isRefractory());
        checkFalse("spike rejected while refractory", neuron.receiveSpike(0.5));
        checkClose("voltage unchanged while refractory", 1.0, neuron.getMembraneVoltage());
    }

    private static void testNeuronDecay() 
    {
        Neuron neuron = new Neuron(1.0, 100.0);
        neuron.tick(0.01);

        checkTrue("voltage decreased", neuron.getMembraneVoltage() < 1.0);
        checkTrue("voltage remains positive", neuron.getMembraneVoltage() > 0.0);
        checkFalse("neuron did not fire", neuron.isRefractory());
    }

    private static void testNeuronFiresAtThreshold() 
    {
        Neuron neuron = new Neuron(1.0, 1e-238);

        checkTrue("tick reports firing", neuron.tick(0.001));
        checkClose("voltage reset to base after fire", 1.0, neuron.getMembraneVoltage());
    }

    private static void testNeuronRefractoryAfterFire() 
    {
        Neuron neuron = new Neuron(1.0, 1e-255);
        neuron.tick(0.001);

        checkTrue("neuron enters refractory state", neuron.isRefractory());
    }

    private static void testNeuronRefractoryDuration() 
    {
        Neuron neuron = new Neuron(1.0, 1e-238);
        neuron.tick(0.001);
        neuron.tick(1.0);

        checkTrue("still refractory after 1 time unit", neuron.isRefractory());

        neuron.tick(1.0);

        checkFalse("refractory after 2 total time units", neuron.isRefractory());
    }

    private static void testNeuronRecovers() 
    {
        Neuron neuron = new Neuron(1.0, 1e-238);
        neuron.tick(0.001);
        neuron.tick(2.0);

        checkTrue("spike accepted after refractory period", neuron.receiveSpike(0.25));
        checkClose("voltage after recovery spike", 1.25, neuron.getMembraneVoltage());
    }

    private static void testNeuronReset() 
    {
        Neuron neuron = new Neuron(0.25, 1.0);
        neuron.receiveSpike(0.75);
        neuron.tick(0.0);
        neuron.reset();

        checkClose("reset voltage", 1.0, neuron.getMembraneVoltage());
        checkFalse("reset refractory state", neuron.isRefractory());
    }


    private static void testSynapseConnections() 
    {
        Neuron pre = new Neuron(0.0, 1.0);
        Neuron post = new Neuron(0.0, 1.0);
        Synapse synapse = new Synapse(pre, post, 0.4);

        checkSame("presynaptic neuron", pre, synapse.getPresynapticNeuron());
        checkSame("postsynaptic neuron", post, synapse.getPostsynapticNeuron());
    }

    private static void testSynapseDelay() 
    {
        Neuron pre = new Neuron(0.0, 1.0);
        Neuron post = new Neuron(0.0, 1.0);
        Synapse synapse = new Synapse(pre, post, 0.5);
        synapse.onSpike(10.0);
        synapse.update(12.99);

        checkClose("postsynaptic voltage before 3-unit delay", 0.0, post.getMembraneVoltage());
    }

    private static void testSynapseDelivery() 
    {
        Neuron pre = new Neuron(0.0, 1.0);
        Neuron post = new Neuron(0.0, 1.0);
        Synapse synapse = new Synapse(pre, post, 0.5);
        synapse.onSpike(10.0);
        synapse.update(13.0);

        checkClose("postsynaptic voltage at delay", 0.5, post.getMembraneVoltage());
    }

    private static void testSynapseMultipleSpikes() 
    {
        Neuron pre = new Neuron(0.0, 10.0);
        Neuron post = new Neuron(0.0, 10.0);
        Synapse synapse = new Synapse(pre, post, 0.25);
        synapse.onSpike(1.0);
        synapse.onSpike(2.0);
        synapse.onSpike(3.0);
        synapse.update(4.0);

        checkClose("one queued spike delivered", 0.25, post.getMembraneVoltage());

        synapse.update(5.0);

        checkClose("two queued spikes delivered", 0.50, post.getMembraneVoltage());

        synapse.update(6.0);

        checkClose("three queued spikes delivered", 0.75, post.getMembraneVoltage());
    }


    private static void testSimulatorInitialTime() 
    {
        Simulator simulator = new Simulator(0.5);

        checkClose("initial simulator time", 0.0, simulator.getCurrentTime());
    }

    private static void testSimulatorStepTime() 
    {
        Simulator simulator = new Simulator(0.25);

        simulator.step();

        checkClose("time after one step", 0.25, simulator.getCurrentTime());

        simulator.step();

        checkClose("time after two steps", 0.50, simulator.getCurrentTime());
    }

    private static void testSimulatorRunSteps() 
    {
        Simulator simulator = new Simulator(0.2);
        simulator.runSteps(5);

        checkClose("time after five steps", 1.0, simulator.getCurrentTime());
    }

    private static void testSimulatorTriggersSynapse() 
    {
        Neuron pre = new Neuron(1.0, 1e-238);
        Neuron post = new Neuron(0.0, 10.0);
        Synapse synapse = new Synapse(pre, post, 0.4);
        Simulator simulator = new Simulator(0.001);
        simulator.addNeuron(pre);
        simulator.addNeuron(post);
        simulator.addSynapse(synapse);
        simulator.step();

        checkClose("no immediate synapse delivery", 0.0, post.getMembraneVoltage());

        simulator.runSteps(3000);

        checkClose("still waiting for synaptic delay", 0.0, post.getMembraneVoltage());

        simulator.step();

        checkClose("synapse delivered at delay", 0.4, post.getMembraneVoltage());
    }

    private static void testSimulatorIgnoresUnrelatedSynapse() 
    {
        Neuron firing = new Neuron(1.0, 1e-100);
        Neuron unrelated = new Neuron(0.0, 10.0);
        Neuron destination = new Neuron(0.0, 10.0);
        Synapse unrelatedSynapse = new Synapse(unrelated, destination, 0.5);
        Simulator simulator = new Simulator(1.0);
        simulator.addNeuron(firing);
        simulator.addNeuron(unrelated);
        simulator.addNeuron(destination);
        simulator.addSynapse(unrelatedSynapse);
        simulator.step();
        simulator.runSteps(3);

        checkClose("unrelated synapse not triggered", 0.0, destination.getMembraneVoltage());
    }

    private static void testSimulatorTwoNeuronPropagation() 
    {
        Neuron pre = new Neuron(1.0, 1e-238);
        Neuron post = new Neuron(0.0, 1e-238);
        Synapse synapse = new Synapse(pre, post, 1.0);
        Simulator simulator = new Simulator(0.001);
        simulator.addNeuron(pre);
        simulator.addNeuron(post);
        simulator.addSynapse(synapse);
        simulator.step();

        checkClose("target before propagation", 0.0, post.getMembraneVoltage());

        simulator.runSteps(3000);

        checkClose("target before delay expires", 0.0, post.getMembraneVoltage());

        simulator.step();

        checkClose("target receives propagated spike", 1.0, post.getMembraneVoltage());

        checkFalse("target has not fired until its next tick", post.isRefractory());

        simulator.step();

        checkTrue("target fires on following simulator step", post.isRefractory());
    }

    private static void testSimulatorPostsynapticRefractory() 
    {
        Neuron source = new Neuron(0.0, 1.0);
        Neuron target = new Neuron(1.0, 1e-238);
        Synapse synapse = new Synapse(source, target, 0.5);
        target.tick(0.001);

        checkTrue("target starts refractory", target.isRefractory());

        synapse.onSpike(0.0);
        synapse.update(3.0);

        checkClose("refractory target rejects delivered spike", 1.0, target.getMembraneVoltage());
    }

    private static void testSimulatorThreeNeuronChain() 
    {
        Neuron a = new Neuron(1.0, 1e-238);
        Neuron b = new Neuron(0.0, 1e-238);
        Neuron c = new Neuron(0.0, 1e-238);
        Synapse aToB = new Synapse(a, b, 1.0);
        Synapse bToC = new Synapse(b, c, 1.0);
        Simulator simulator = new Simulator(0.001);
        simulator.addNeuron(a);
        simulator.addNeuron(b);
        simulator.addNeuron(c);
        simulator.addSynapse(aToB);
        simulator.addSynapse(bToC);
        simulator.step();
        simulator.runSteps(3000);

        checkClose("B before first delay", 0.0, b.getMembraneVoltage());
        checkClose("C before first delay", 0.0, c.getMembraneVoltage());

        simulator.step();

        checkClose("B receives first spike", 1.0, b.getMembraneVoltage());
        checkClose("C still waiting", 0.0, c.getMembraneVoltage());

        simulator.step();

        checkTrue("B fires after receiving threshold input", b.isRefractory());

        simulator.runSteps(2990);

        checkClose("C before second delay", 0.0, c.getMembraneVoltage());

        simulator.runSteps(20);

        checkClose("C receives propagated spike", 1.0, c.getMembraneVoltage());

        simulator.step();

        checkTrue("C fires at end of chain", c.isRefractory());
    }

    
    private static void run(String name, Runnable test) 
    {
        System.out.printf("TEST %s%n", name);

        try 
        {
            test.run();
            passed++;
            System.out.println("    (:  PASSED!");
        }
        catch (AssertionError error) 
        {
            failed++;
            System.out.println("    \\:  FAILED: " + error.getMessage());
        }
        catch (Exception error) 
        {
            failed++;
            System.out.println("    ):  ERROR: " + error);
        }
    }


    private static void checkTrue(String description, boolean actual)
    {
        if (!actual) 
            throw new AssertionError(description + " expected true but was false");
    }


    private static void checkFalse(String description, boolean actual) 
    {
        if (actual)
            throw new AssertionError(description + " expected false but was true");
    }


    private static void checkClose(String description, double expected, double actual) 
    {
        if (Double.isNaN(actual) || Math.abs(expected - actual) > EPS) 
            throw new AssertionError(String.format("%s expected %.12f but was %.12f", description, expected, actual));
    }


    private static void checkSame(String description, Object expected, Object actual) 
    {
        if (expected != actual)
            throw new AssertionError(description +" was not the same object");
    }
}