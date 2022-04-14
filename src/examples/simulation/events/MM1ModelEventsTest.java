package examples.simulation.events;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import jasima.core.simulation.SimContext;

/**
 * JUnit test for basic behavior of {@link MM1ModelEvents}.
 */
public class MM1ModelEventsTest {

	@Test
	public void testBasicBehaviour() {
		// run the simulation
		Map<String, Object> res = SimContext.simulationOf(new MM1ModelEvents());
		
		// check results are present and have correct values
		assertEquals("numCreated", 1000, res.get("numCreated"));
		// both values below depend on the random numbers generated 
		assertEquals("numServed", 982, res.get("numServed"));
		assertEquals("simTime", 1023.158662528794, (Double) res.get("simTime"), 1e-6);
	}

}
