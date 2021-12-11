package examples.experiments.running;

import jasima.core.experiment.FullFactorialExperiment;
import jasima.core.util.ExcelSaver;
import jasima.shopSim.models.dynamicShop.DynamicShopExperiment;

/**
 * A {@link FullFactorialExperiment} executes all combinations resulting from
 * varying certain parameters of a base experiment.
 * 
 * @author Torsten Hildebrandt
 */
public class RunParameterCombinations {

	public static void main(String[] args) {
		// create base experiment, set default parameters; initialSeed and created below
		DynamicShopExperiment dse = new DynamicShopExperiment();
		dse.setNumMachines(10);

		// create a MultipleReplicationExperiment executing the base experiment "dse" 15
		// times (all combinations of utilLevel and initialSeed)
		FullFactorialExperiment ffe = new FullFactorialExperiment(dse);
		ffe.addFactor("utilLevel", 0.8, 0.85, 0.9);
		ffe.addFactor("initialSeed", 1, 2, 3, 4, 5);

		// report results as Excel file
		ffe.addListener(new ExcelSaver("ffeResults"));

		// run and print averaged results over all 10 replications
		ffe.runExperiment();
		ffe.printResults();
	}

}
