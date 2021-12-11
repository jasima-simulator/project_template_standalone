package examples.experiments.running;

import jasima.core.util.ExcelSaver;
import jasima.shopSim.models.dynamicShop.DynamicShopExperiment;

/**
 * This is basically the same as {@link RunExistingExperiment}, but adds an
 * {@link ExcelSaver} to produce an xls file for the results.
 * 
 * @author Torsten Hildebrandt
 */
public class RunExistingExperimentExcelOutput {

	public static void main(String[] args) {
		// create experiment and set parameter
		DynamicShopExperiment dse = new DynamicShopExperiment();
		dse.setInitialSeed(23);
		dse.setNumMachines(10);
		dse.setUtilLevel(0.85);
		
		// NEW: add ExcelSaver
		dse.addListener(new ExcelSaver("dseResults"));

		// run experiment
		dse.runExperiment();

		// show results on the console, SummaryStats first, then normal values
		dse.printResults();
	}

}
