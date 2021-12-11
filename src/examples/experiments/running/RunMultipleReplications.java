package examples.experiments.running;

import jasima.core.experiment.MultipleReplicationExperiment;
import jasima.core.util.ExcelSaver;
import jasima.shopSim.models.dynamicShop.DynamicShopExperiment;

/**
 * Execute a base experiment (here: a {@link DynamicShopExperiment}) a certain
 * number of times varying the seed, i.e., run multiple, independent
 * replications. {@link MultipleReplicationExperiment} has a number of
 * additional options to fine-tune execution (such as making the number of runs
 * dynamic).
 * 
 * @author Torsten Hildebrandt
 */
public class RunMultipleReplications {

	public static void main(String[] args) {
		// create base experiment, set default parameters; initialSeed is varied
		// automatically by the MultipleReplicationExperiment created below
		DynamicShopExperiment dse = new DynamicShopExperiment();
		dse.setNumMachines(10);
		dse.setUtilLevel(0.85);

		// create a MultipleReplicationExperiment executing the base experiment "dse" 10
		// times
		MultipleReplicationExperiment mre = new MultipleReplicationExperiment(dse, 10);

		// report results as Excel file
		mre.addListener(new ExcelSaver("mreResults"));

		// run and print averaged results over all 10 replications
		mre.runExperiment();
		mre.printResults();
	}

}
