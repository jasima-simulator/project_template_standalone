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
import jasima.core.util.MsgCategory;

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
		ConsolePrinter.printResults(null, res);
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
				trace("procStarted", job);
				double st = serviceTimes.nextDbl();
				waitFor(st);
				numServed++;
				trace("procFinished", job);
			}
		}

		@Override
		public void produceResults(Map<String, Object> res) {
			super.produceResults(res);
			res.put("numServed", numServed);
		}
	}

	// parameter
	int numJobs = 1000;
	double trafficIntensity = 0.85;

	// fields used during run
	int numCreated;
	Q<Integer> q = new Q<>();

	@Override
	protected void lifecycle() throws MightBlock {
		getSim().setPrintLevel(MsgCategory.ALL);
		getSim().addPrintListener(System.out::println);

		// init
		addComponent(new Server());
		numCreated = 0;
		DblSequence iats = initRndGen(new DblExp(INTER_ARRIVAL_TIME), "arrivals");

		// creation of jobs
		for (int n = 0; n < numJobs; n++) {
			waitFor(iats.nextDbl());
			trace("created job", n);
			q.put(n);
			numCreated++;
		}

		// terminate simulation
		end();
	}

	@Override
	public void produceResults(Map<String, Object> res) {
		super.produceResults(res);
		res.put("numCreated", numCreated);
	}

}
