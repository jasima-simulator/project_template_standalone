package examples.experiments.montecarlo;

import java.util.Random;

import jasima.core.experiment.Experiment;

/**
 * Example for creating a custom experiment. It calculates pi using a Monte
 * Carlo approach (see https://en.wikipedia.org/wiki/Monte_Carlo_method for
 * details).
 * <p>
 * The experiment has the number of trials to use as a parameter and produces
 * absolute and relative error results (see {@link #produceResults()}).
 * 
 * @author Torsten Hildebrandt
 */
public class CustomExperiment extends Experiment {

	private static final long serialVersionUID = 1L;

	// fields holding parameter values, getter and setter below
	private int numTrials = 1000;

	// fields used during run
	private Random rnd;
	private int numHits;

	public CustomExperiment() {
		super();
	}

	@Override
	protected void init() {
		super.init();
		rnd = new Random(getInitialSeed());
	}

	@Override
	protected void performRun() {
		for (int n = 0; n < getNumTrials(); n++) {
			if (checkHit()) {
				numHits++;
			}
		}
	}

	/**
	 * Checks if two randomly drawn numbers between 0.0 and 1.0 are in a (quarter)
	 * circle.
	 * 
	 * @return true, if the random point was within the circle or not
	 */
	private boolean checkHit() {
		double x = rnd.nextDouble();
		double y = rnd.nextDouble();
		return x * x + y * y <= 1.0;
	}

	@Override
	protected void produceResults() {
		super.produceResults();

		double piEstimate = 4.0 * numHits / getNumTrials();

		resultMap.put("piEstimate", piEstimate);
		resultMap.put("piErrorAbsolute", Math.abs(piEstimate - Math.PI));
		resultMap.put("piErrorRelative", Math.abs(piEstimate / Math.PI - 1.0));
	}

	// boring getter/setter below

	public int getNumTrials() {
		return numTrials;
	}

	public void setNumTrials(int numTrials) {
		this.numTrials = numTrials;
	}

}
