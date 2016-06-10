package nl.tudelft.contextproject.audio;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Mockito;

import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioSource;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.util.FileUtil;
import nl.tudelft.contextproject.logging.Log;
import nl.tudelft.contextproject.model.TickListener;

/**
 * Class for playing/managing background music.
 */
public final class BackgroundMusic implements TickListener {
	private static final BackgroundMusic INSTANCE = new BackgroundMusic();
	
	private List<String> music = new ArrayList<String>();
	private int currentIndex = -1;
	private AudioNode current;
	private boolean testing;
	
	/**
	 * Creates a new BackgroundMusic object and loads the music.
	 */
	private BackgroundMusic() {
		loadMusic();
	}
	
	/**
	 * @return
	 * 		the BackgroundMusic instance
	 */
	public static BackgroundMusic getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Retrieves a list of all music files available.
	 * 
	 * <p>This method should only be called once.
	 */
	private final void loadMusic() {
		String[] names = FileUtil.getFileNames("/Sound/Music/");
		if (names == null) {
			Log.getLog("Audio").warning("No music could be found!");
			return;
		}

		for (String name : names) {
			//Skip files that are not in a supported format
			if (!name.endsWith(".wav") && !name.endsWith(".ogg")) {
				continue;
			}
			
			music.add(name);
		}
	}
	
	/**
	 * Sets testing on or off. When testing, audio is not played from the
	 * default location.
	 * 
	 * @param testing
	 * 	disable or enable testing mode
	 */
	synchronized void setTesting(boolean testing) {
		this.testing = testing;
	}
	
	/**
	 * Plays the given AudioNode as background music.
	 * 
	 * This stops playing any song that was already running.
	 * 
	 * @param audioNode
	 * 		the AudioNode to play
	 */
	public synchronized void playSong(AudioNode audioNode) {
		AudioManager.getInstance().registerVolume(audioNode, SoundType.BACKGROUND_MUSIC);

		audioNode.play();
		
		if (current != null) {
			stop();
		}
		
		current = audioNode;
	}
	
	/**
	 * Stops the currently playing song and switches to the next song.
	 * If there are no songs, this method does the same as {@link #stop()}.
	 */
	public synchronized void next() {
		if (music.isEmpty()) {
			if (current != null) {
				stop();
			}
			
			return;
		}

		int index = (currentIndex + 1) % music.size();
		String nextName = music.get(index);

		AudioNode nextAudioNode;
		if (testing) {
			nextAudioNode = Mockito.mock(AudioNode.class);
		} else {
			nextAudioNode = new AudioNode(Main.getInstance().getAssetManager(), "Sound/Music/" + nextName, DataType.Stream);
		}
		
		nextAudioNode.setPositional(false);

		AudioManager.getInstance().registerVolume(nextAudioNode, SoundType.BACKGROUND_MUSIC);
		
		//Start the new music before stopping the old one for a smoother transition.
		nextAudioNode.play();
		
		if (current != null) {
			stop();
		}

		current = nextAudioNode;
		currentIndex = index;
	}
	
	/**
	 * Starts music playback.
	 * 
	 * <p>If the music was paused, it is unpaused.
	 * <br>If the music was not started, it is started.
	 */
	public synchronized void start() {
		if (current == null) {
			next();
		} else if (current.getStatus() == AudioSource.Status.Paused) {
			current.play();
		}
	}
	
	/**
	 * Pauses music playback.
	 */
	public synchronized void pause() {
		if (current == null) return;
		
		current.pause();
	}
	
	/**
	 * Stops the background music.
	 */
	public synchronized void stop() {
		if (current == null) return;

		current.stop();
		AudioManager.getInstance().unregisterVolume(current, SoundType.BACKGROUND_MUSIC);
		
		current = null;
	}
	
	/**
	 * Returns the AudioNode that is currently playing/paused.
	 * If there is no AudioNode being played, then this method returns null.
	 * 
	 * @return
	 * 		the current AudioNode
	 */
	public synchronized AudioNode getCurrent() {
		return current;
	}
	
	@Override
	public void update(float tpf) {
		AudioNode audioNode = current;
		if (audioNode == null) return;

		if (audioNode.getStatus() == AudioSource.Status.Stopped) {
			next();
		}
	}
}
