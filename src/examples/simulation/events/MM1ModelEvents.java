package examples.simulation.events;

import java.util.Map;

import jasima.core.random.continuous.DblExp;
import jasima.core.random.continuous.DblSequence;
import jasima.core.simulation.SimComponentBase;
import jasima.core.simulation.Simulation;
import jasima.core.simulation.generic.Q;
import jasima.core.util.ConsolePrinter;

/**
 * Event-oriented modelling of a single server queue with exponentially
 * distributed interarrival and service times.
 * 
 * @author Torsten Hildebrandt
 */
public class MM1ModelEvents extends SimComponentBase {

	public static void main(String... args) {
		// create and parameterize simulation
		Simulation sim = new Simulation();

		MM1ModelEvents mainComponent = new MM1ModelEvents();
		sim.addComponent(mainComponent);

		// run simulation
		Map<String, Object> res = sim.performRun();

		// show results on console
		ConsolePrinter.printResults(res);
	}

	private static final double INTER_ARRIVAL_TIME = 1.0;

	// parameters
	private int numJobs = 1000;
	private double trafficIntensity = 0.85;

	// fields used during simulation run
	private Q<Integer> q; // queue for waiting jobs
	private DblSequence iats; // inter-arrival times
	private DblSequence serviceTimes; // service times
	private Integer currentJob; // job currently processed by the server
	private int numServed = 0; // counter for number of serviced jobs
	private int numCreated = 0; // counter for total number of jobs created

	@Override
	public void init() {
		// create new queue
		q = new Q<>();

		// initialize random number streams/sequences
		iats = initRndGen(new DblExp(INTER_ARRIVAL_TIME), "arrivals");
		serviceTimes = initRndGen(new DblExp(INTER_ARRIVAL_TIME * trafficIntensity), "services");

		// schedule first arrival
		scheduleIn(iats.nextDbl(), this::createNext);
	}

	void createNext() {
		Integer n = numCreated++;
		q.tryPut(n);
		
		System.out.println("created job " + n + " at time=" + simTime());
		
		checkStartService();

		// schedule next arrival or end simulation
		if (numCreated < numJobs) {
			scheduleIn(iats.nextDbl(), this::createNext);
		} else {
			end();
		}
	}

	void checkStartService() {
		if (q.numItems() == 0 || currentJob != null) {
			// nothing to do because either "q" is empty or server is currently busy
			return;
		}

		// take next job from queue
		currentJob = q.tryTake();
		System.out.println("start processing job " + currentJob + " at time=" + simTime());

		// schedule service end event
		scheduleIn(serviceTimes.nextDbl(), this::finishedService);
	}

	void finishedService() {
		System.out.println("finished processing job " + currentJob + " at time=" + simTime());
		currentJob = null;
		numServed++;

		// immediately check processing of next job
		checkStartService();
	}

	@Override
	public void produceResults(Map<String, Object> res) {
		res.put("numCreated", numCreated);
		res.put("numServed", numServed);
	}

}
