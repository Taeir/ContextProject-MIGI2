package nl.tudelft.contextproject;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TestMain {
	
	@Test
	public void testMain() {
        Main mockedMain = mock(Main.class);
        Main.setInstance(mockedMain);
	    Main.main(new String[0]);
        verify(mockedMain, times(1)).start();
	}



}