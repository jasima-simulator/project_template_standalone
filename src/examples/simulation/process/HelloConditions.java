package examples.simulation.process;

import static jasima.core.simulation.SimContext.activate;
import static jasima.core.simulation.SimContext.waitCondition;
import static jasima.core.simulation.SimContext.waitFor;

import jasima.core.simulation.SimEntity;
import jasima.core.simulation.SimProcess.MightBlock;
import jasima.core.simulation.Simulation;
import jasima.core.util.observer.ObservableValue;
import jasima.core.util.observer.ObservableValues;

/**
 * Example of using ObservableValue and waitCondition. It creates a counter as
 * an Observable. The counter is incremented by a simulation process
 * "incrementer". A second process "printer" waits until the counter modulus 3
 * is 0 before printing some message (so it prints a message after 3
 * increments). Furthermore the main process waits until the counter reaches 13
 * before ending the simulation.
 * 
 * @author Torsten Hildebrandt
 */
public class HelloConditions extends SimEntity {

	public static void main(String[] args) {
		Simulation.of(new HelloConditions());
	}

	private ObservableValue<Integer> callCounter;

	@Override
	protected void lifecycle() throws MightBlock {
		// init
		System.out.println("simulation main process started at: " + simTime());
		callCounter = new ObservableValue<>(0); //<1>
		activate(this::incrementer);
		activate(this::printer);

		// main process waiting until counter reaches 13
		ObservableValue<Boolean> counterEquals13 = ObservableValues.isEqual(callCounter, 13); // <2>
		waitCondition(counterEquals13); // <3>

		// end simulation
		System.out.println("simulation main process finished at: " + simTime());
		end();
	}

	void incrementer() throws MightBlock {
		System.out.println("incrementor() starting at: " + simTime());
		while (true) {
			waitFor(3.0);

			// increment counter
			callCounter.set(callCounter.get() + 1);
			// instead of get/set we could also have used: callCounter.update(n -> n+1);

			System.out.println("incrementor() executed at: " + simTime() + ", counter = " + callCounter.get());
		}
	}

	void printer() throws MightBlock {
		System.out.println("printer() starting at: " + simTime());
		while (true) {
			waitCondition(n -> n % 3 == 0, callCounter);
			System.out.println("printer() executed at: t=" + simTime() + ", counter = " + callCounter.get());
			waitFor(5.0);
		}
	}

}
