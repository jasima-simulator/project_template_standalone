package examples.experiments.running;

import static java.util.Locale.ENGLISH;

import java.util.Map;

import jasima.core.statistics.SummaryStat;
import jasima.shopSim.models.dynamicShop.DynamicShopExperiment;

/**
 * Example demonstrating creating, running and finally printing results of an
 * Experiment on the console. We use the {@link DynamicShopExperiment} in this
 * example. This is a standard class coming with jasima offering various
 * parameters how to configure/parameterize the simulation.
 * 
 * @author Torsten Hildebrandt
 */
public class RunExistingExperiment {

	public static void main(String[] args) {
		// create experiment and set parameter
		DynamicShopExperiment dse = new DynamicShopExperiment();
		dse.setInitialSeed(23);
		dse.setNumMachines(10);
		dse.setUtilLevel(0.85);

		// run experiment
		Map<String, Object> res = dse.runExperiment();

		// show results on the console, SummaryStats first, then normal values
		dse.printResults();

		// we can also access individual results by their name and so something with
		// them
		SummaryStat flowtimes = (SummaryStat) res.get("flowtime");
		String msg = String.format(ENGLISH, "experiment finished %d jobs with an average flowtime of %.1f minutes.",
				flowtimes.numObs(), flowtimes.mean());
		System.out.println();
		System.out.println(msg);
	}

}
