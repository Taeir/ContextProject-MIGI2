package nl.tudelft.contextproject;

import org.junit.Before;
import org.junit.Test;

public class VRPlayerTest extends EntityTest{

	private Entity entity;
	@Override
	public Entity getEntity() {
		return new VRPlayer();
	}
	
	@Before
	public void setUp() {
		entity = getEntity();
	}

}
