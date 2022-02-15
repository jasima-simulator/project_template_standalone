package examples.experiments.montecarlo;

import jasima.core.experiment.FullFactorialExperiment;
import jasima.core.experiment.MultipleReplicationExperiment;
import jasima.core.run.ConsoleRunner;
import jasima.core.util.ExcelSaver;

/**
 * Performs a simulation study based on {@link CustomExperiment} to analyse the
 * prediction quality when varying the number of trials. It runs 100 independent
 * replications for each setting of numTrials.
 * <p>
 * Results are written to an Excel file.
 * 
 * @author Torsten Hildebrandt
 */
public class RunCustomExperimentComplex {

	public static void main(String... args) {
		// create the base Experiment
		CustomExperiment ce = new CustomExperiment();

		// run 100 replications of it
		MultipleReplicationExperiment mre = new MultipleReplicationExperiment();
		mre.setBaseExperiment(ce);
		mre.setMaxReplications(100);
//		mre.setKeepResults("piEstimate");
		
		// vary the number of trials, each running 100 independent replications
		FullFactorialExperiment ffe = new FullFactorialExperiment(mre);
		ffe.addFactor("baseExperiment.numTrials", 250, 500, 1000, 2000, 4000, 8000, 16000);
		
		// create Excel results
		ffe.addListener(new ExcelSaver("monteCarlo"));
		
		// run experiment, show results on the console
		ConsoleRunner.run(ffe);
	}

}
