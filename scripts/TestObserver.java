import nl.tudelft.contextproject.model.Observer;

/**
 * Script used for testing.
 */
public class TestObserver implements Observer {
	@Override
	public void update(float tpf) {
		throw new IllegalMonitorStateException();
	}
}
