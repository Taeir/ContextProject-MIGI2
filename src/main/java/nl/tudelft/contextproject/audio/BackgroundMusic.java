package nl.tudelft.contextproject.audio;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioSource;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.logging.Log;

import org.mockito.Mockito;

/**
 * Class for playing/managing background music.
 */
public final class BackgroundMusic {
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
	 * This method should only be called once.
	 */
	private final void loadMusic() {
		//Get a list of all music files available.
		File folder = new File("Sound", "Music");
		File[] files = folder.listFiles();
		if (files == null) {
			Log.getLog("Audio").warning("No music could be found!");
			return;
		}
		
		for (File file : files) {
			//Skip files that are not in a supported format
			if (!file.getName().endsWith(".wav") && !file.getName().endsWith(".ogg")) {
				continue;
			}
			
			music.add(file.getName());
		}
	}
	
	/**
	 * Sets testing on or off. When testing, audio is not played from the
	 * default location.
	 * 
	 * @param testing
	 * 	disable or enable testing mode
	 */
	void setTesting(boolean testing) {
		this.testing = testing;
	}
	
	/**
	 * Plays the given AudioNode as background music.
	 * 
	 * This stops playing any song that was already running.
	 * 
	 * @param an
	 * 		the AudioNode to play
	 */
	public synchronized void playSong(AudioNode an) {
		//Register for volume changes
		AudioManager.getInstance().registerVolume(an, SoundType.BACKGROUND_MUSIC);
		
		//Start the given song
		an.play();
		
		if (current != null) {
			//Stop the old music
			stop();
		}
		
		current = an;
	}
	
	/**
	 * Stops the currently playing song and switches to the next song.
	 * If there are no songs, this method does the same as {@link #stop()}.
	 */
	public synchronized void next() {
		//If we have no music, we cannot play any.
		if (music.isEmpty()) {
			//If a song is currently playing, then stop it.
			if (current != null) {
				stop();
			}
			
			return;
		}
		
		//Get the next song
		int index = currentIndex + 1 % music.size();
		String nextName = music.get(index);
		
		//Create the AudioNode
		AudioNode nextAudioNode;
		if (testing) {
			//For testing, create a mocked audio node
			nextAudioNode = Mockito.mock(AudioNode.class);
		} else {
			nextAudioNode = new AudioNode(Main.getInstance().getAssetManager(), "Sound/Music/" + nextName, DataType.Stream);
		}
		
		nextAudioNode.setPositional(false);
		
		//Register for volume changes
		AudioManager.getInstance().registerVolume(nextAudioNode, SoundType.BACKGROUND_MUSIC);
		
		//Start the new music before stopping the old one for a smoother transition.
		nextAudioNode.play();
		
		if (current != null) {
			//Stop the old music
			stop();
		}
		
		//Update the fields
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
		
		//Stop the music and unregister it.
		current.stop();
		AudioManager.getInstance().unregisterVolume(current, SoundType.BACKGROUND_MUSIC);
		
		current = null;
	}
	
	/**
	 * Called to indicate an update.
	 * 
	 * @param tpf
	 * 		tick indicator
	 */
	public void update(double tpf) {
		AudioNode an = current;
		if (an == null) return;
		
		//Play the next song if the last one is done playing.
		if (an.getStatus() == AudioSource.Status.Stopped) {
			next();
		}
	}
}
