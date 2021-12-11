package examples.simulation;

import static jasima.core.simulation.SimContext.*;

import jasima.core.simulation.SimEntity;
import jasima.core.simulation.SimProcess.MightBlock;
import jasima.core.simulation.Simulation;

/**
 * Very simple simulation to demonstrate the basic simulation setup. The example
 * uses the process-oriented modelling style and creates two independent
 * processes (slowClock and fastClock) in addition to a main process.
 * 
 * @author Torsten Hildebrandt
 */
public class HelloWorld extends SimEntity {

	public static void main(String[] args) {
		Simulation.of(new HelloWorld());
	}

	@Override
	protected void lifecycle() throws MightBlock {
		System.out.println("simulation main process started at: " + simTime());
		// schedule simulation end event at time 20, this call immediately returns
		// without consuming any simulation time
		getSim().setSimulationLength(20.0);
		// spawn slockClock and fastClock as two separate processes, conceptually
		// running in parallel to this main process
		activate(this::slowClock);
		activate(this::fastClock);
		// main process waiting until time=5 before continuing
		waitFor(5.0);
		System.out.println("simulation main process finished at: " + simTime());
	}

	void slowClock() throws MightBlock {
		System.out.println("slowClock() starting at: " + simTime());
		while (true) {
			waitFor(7.0);
			System.out.println("slowClock() executed at: " + simTime());
		}
	}

	void fastClock() throws MightBlock {
		System.out.println("fastClock() starting at: " + simTime());
		while (true) {
			waitFor(3.0);
			System.out.println("fastClock() executed at: " + simTime());
		}
	}

}
