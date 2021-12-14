package examples.simulation.events;

import static java.util.Objects.requireNonNull;

import java.util.Map;

import jasima.core.random.continuous.DblExp;
import jasima.core.random.continuous.DblSequence;
import jasima.core.simulation.SimComponentBase;
import jasima.core.simulation.Simulation;
import jasima.core.simulation.generic.Q;
import jasima.core.util.ConsolePrinter;
import jasima.core.util.MsgCategory;

/**
 * Event-oriented modelling of a single server queue with exponentially
 * distributed interarrival and service times.
 * 
 * @author Torsten Hildebrandt
 */
public class MM1ModelEvents extends SimComponentBase {

	public static void main(String... args) {
		Simulation sim = new Simulation();
		sim.setPrintLevel(MsgCategory.ALL);
		sim.addPrintListener(System.out::println);

		MM1ModelEvents mainComponent = new MM1ModelEvents();
		sim.addComponent(mainComponent);

		Map<String, Object> res = sim.performRun();

		ConsolePrinter.printResults(null, res);
	}

	private static final double INTER_ARRIVAL_TIME = 1.0;

	// parameters
	private int numJobs = 1000;
	private double trafficIntensity = 0.85;

	// fields used during run
	private Q<Integer> q;
	private DblSequence iats;
	private DblSequence serviceTimes;
	private Integer currentJob;
	private int numServed = 0;
	private int numCreated = 0;

	@Override
	public void init() {
		super.init();

		q = new Q<>();

		iats = initRndGen(new DblExp(INTER_ARRIVAL_TIME), "arrivals");
		serviceTimes = initRndGen(new DblExp(INTER_ARRIVAL_TIME * trafficIntensity), "services");

		// schedule first arrival
		scheduleIn(iats.nextDbl(), getSim().currentPrio(), this::createNext);
	}

	void createNext() {
		Integer n = numCreated++;
		if (!q.tryPut(n)) {
			throw new IllegalStateException("can't put in queue?");
		}
		trace("created job", n);
		checkStartService();
		if (numCreated < numJobs) {
			scheduleIn(iats.nextDbl(), getSim().currentPrio(), this::createNext);
		} else {
			end();
		}
	}

	void checkStartService() {
		if (q.numItems() == 0 || currentJob != null) {
			return; // nothing to do
		}
		currentJob = requireNonNull(q.tryTake());
		trace("procStarted", currentJob);
		scheduleIn(serviceTimes.nextDbl(), getSim().currentPrio(), this::finishedService);
	}

	void finishedService() {
		trace("procFinished", currentJob);
		currentJob = null;
		numServed++;
		checkStartService();
	}

	@Override
	public void produceResults(Map<String, Object> res) {
		super.produceResults(res);
		res.put("numCreated", numCreated);
		res.put("numServed", numServed);
	}

}
