package examples.experiments.montecarlo;

import jasima.core.run.ConsoleRunner;

/**
 * Runs the {@link CustomExperiment}.
 * 
 * @author Torsten Hildebrandt
 */
public class RunCustomExperimentSimple {

	/**
	 * Simple main method for test purposes, create and run experiment.
	 */
	public static void main(String... args) {
		CustomExperiment ce = new CustomExperiment();

		// set parameters inherited from Experiment: name and initialSeed
		ce.setName("myExperiment"); // optional name
		ce.setInitialSeed(42);

		// set new parameter defined below
		ce.setNumTrials(1000);

		// run experiment, show results on the console
		ConsoleRunner.run(ce);
	}

}
