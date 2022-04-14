package examples.simulation.events;

import java.util.Map;

import jasima.core.experiment.MultipleReplicationExperiment;
import jasima.core.random.continuous.DblExp;
import jasima.core.random.continuous.DblSequence;
import jasima.core.run.ConsoleRunner;
import jasima.core.simulation.SimComponentBase;
import jasima.core.simulation.SimulationExperiment;
import jasima.core.simulation.generic.Q;
import jasima.core.statistics.SummaryStat;
import jasima.core.statistics.TimeWeightedSummaryStat;
import jasima.core.util.ExcelSaver;

/**
 * This class is the same as {@link MM1ModelEvents}, but demonstrates how to
 * capture additional statistics.
 * 
 * @author Torsten Hildebrandt
 */
public class MM1ModelEventsExtendedStatistics extends SimComponentBase {

	public static void main(String... args) {
		SimulationExperiment se = new SimulationExperiment();
		se.setModelRoot(new MM1ModelEventsExtendedStatistics());

		// try also using the 3 lines commented out below instead of running the se directly
		ConsoleRunner.run(se);
//		MultipleReplicationExperiment mre = new MultipleReplicationExperiment(se, 100);
//		mre.addListener(new ExcelSaver());
//		ConsoleRunner.run(mre);
	}

	private static final double INTER_ARRIVAL_TIME = 1.0;

	// Job contains additional job data, here we only add an attribute to record
	// record a Job's number and when queuing started
	public class Job {
		public int jobId;
		public double startQueuing;
	}

	// parameters
	private int numJobs = 1000;
	private double trafficIntensity = 0.85;

	// fields used during simulation run
	private Q<Job> q; // queue for waiting jobs
	private DblSequence iats; // inter-arrival times
	private DblSequence serviceTimes; // service times
	private Job currentJob; // job currently processed by the server
	private int numServed; // counter for number of serviced jobs
	private int numCreated; // counter for total number of jobs created

	// field used for additional stats collection
	private SummaryStat queuingTimes;
	private TimeWeightedSummaryStat queueLength;
	private TimeWeightedSummaryStat serverUtilization;

	@Override
	public void init() {
		// create new queue
		q = new Q<>();

		// initialize random number streams/sequences
		iats = initRndGen(new DblExp(INTER_ARRIVAL_TIME), "arrivals");
		serviceTimes = initRndGen(new DblExp(INTER_ARRIVAL_TIME * trafficIntensity), "services");

		// schedule first arrival
		scheduleIn(iats.nextDbl(), this::createNext);

		// initialize statistics
		numServed = 0;
		numCreated = 0;
		queuingTimes = new SummaryStat();
		queueLength = new TimeWeightedSummaryStat();
		serverUtilization = new TimeWeightedSummaryStat();
	}

	void createNext() {
		numCreated++;
		Job j = new Job();
		j.jobId = numCreated;
		j.startQueuing = simTime();

		q.tryPut(j);
		queueLength.value(q.numItems(), simTime());

//		System.out.println("created job " + j.jobId + " at time=" + simTime());

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
		// update queue length statistics
		queueLength.value(q.numItems(), simTime());

		// record queuing time
		double queuingTime = simTime() - currentJob.startQueuing;
		queuingTimes.value(queuingTime);

//		System.out.println("start processing job " + currentJob.jobId + " at time=" + simTime());

		// update utilization statistics, server is busy now
		serverUtilization.value(1.0, simTime());

		// schedule service end event
		scheduleIn(serviceTimes.nextDbl(), this::finishedService);
	}

	void finishedService() {
//		System.out.println("finished processing job " + currentJob.jobId + " at time=" + simTime());
		currentJob = null;
		numServed++;

		// update utilization statistics, server is idle now
		serverUtilization.value(0.0, simTime());

		// immediately check processing of next job
		checkStartService();
	}

	@Override
	public void simEnd() {
		// properly close time-weighted statistic values
		serverUtilization.value(0.0, simTime());
		queueLength.value(0.0, simTime());

		// we can also call addResult() at any time during a simulation run instead of
		// or in addition to overriding produceResults(); this is useful for instance
		// when using process-oriented modelling
		addResult("serverUtilization", serverUtilization);
	}

	@Override
	public void produceResults(Map<String, Object> res) {
		res.put("numCreated", numCreated);
		res.put("numServed", numServed);
		res.put("queuingTime", queuingTimes);
		res.put("queueLength", queueLength);
	}

}
