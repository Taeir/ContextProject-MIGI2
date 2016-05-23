package nl.tudelft.contextproject.audio;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.jme3.audio.AudioRenderer;
import com.jme3.audio.Listener;
import com.jme3.renderer.Camera;

import nl.tudelft.contextproject.Main;

/**
 * Utility class for testing with audio.
 */
public final class AudioTestUtil {
	
	private AudioTestUtil() { }
	
	/**
	 * Creates a spy Main object with a mocked Listener and Camera,
	 * to properly allow audio support.
	 * 
	 * @return
	 * 		a spy Main object that has a mocked Listener and Camera
	 */
	public static Main fakeMain() {
		Listener listener = mock(Listener.class);
		Camera camera = mock(Camera.class);
		AudioRenderer audioRenderer = mock(AudioRenderer.class);
		
		//Determine what needs to be mocked
		Main noSpy = new Main();
		boolean are = noSpy.getAudioRenderer() == null;
		boolean lis = noSpy.getListener() == null;
		boolean cam = noSpy.getCamera() == null;

		Main main = spy(noSpy);

		if (are) when(main.getAudioRenderer()).thenReturn(audioRenderer);
		if (lis) when(main.getListener()).thenReturn(listener);
		if (cam) when(main.getCamera()).thenReturn(camera);
		
		return main;
	}
}
