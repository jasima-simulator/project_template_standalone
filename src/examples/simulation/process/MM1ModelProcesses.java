package examples.simulation.process;

import static jasima.core.simulation.SimContext.waitFor;

import java.util.Map;

import jasima.core.random.continuous.DblExp;
import jasima.core.random.continuous.DblSequence;
import jasima.core.simulation.SimEntity;
import jasima.core.simulation.SimProcess.MightBlock;
import jasima.core.simulation.Simulation;
import jasima.core.simulation.generic.Q;
import jasima.core.util.ConsolePrinter;

/**
 * Process-oriented modelling of a single server queue with exponentially
 * distributed interarrival and service times.
 * 
 * @author Torsten Hildebrandt
 */
public class MM1ModelProcesses extends SimEntity {

	private static final double INTER_ARRIVAL_TIME = 1.0;

	public static void main(String... args) {
		Map<String, Object> res = Simulation.of(new MM1ModelProcesses());
		ConsolePrinter.printResults(res);
	}

	class Server extends SimEntity {
		int numServed;

		@Override
		protected void lifecycle() throws MightBlock {
			// init
			DblSequence serviceTimes = initRndGen(new DblExp(INTER_ARRIVAL_TIME * trafficIntensity), "services");
			numServed = 0;

			// endless processing loop
			while (true) {
				Integer job = q.take();
				System.out.println("start processing job " + job + " at time=" + simTime());
				double st = serviceTimes.nextDbl();
				waitFor(st);
				numServed++;
				System.out.println("finished processing job " + job + " at time=" + simTime());
			}
		}

		@Override
		public void produceResults(Map<String, Object> res) {
			res.put("numServed", numServed);
		}
	}

	// parameter
	int numJobs = 1000;
	double trafficIntensity = 0.85;

	// fields used during run
	int numCreated;
	Q<Integer> q;

	@Override
	protected void lifecycle() throws MightBlock {
		// init
		q = new Q<>();
		addComponent(new Server());
		numCreated = 0;
		DblSequence iats = initRndGen(new DblExp(INTER_ARRIVAL_TIME), "arrivals");

		// creation of jobs
		for (int n = 0; n < numJobs; n++) {
			waitFor(iats.nextDbl());
			System.out.println("created job " + n + " at time=" + simTime());
			q.put(n);
			numCreated++;
		}

		// terminate simulation
		end();
	}

	@Override
	public void produceResults(Map<String, Object> res) {
		res.put("numCreated", numCreated);
	}

}
