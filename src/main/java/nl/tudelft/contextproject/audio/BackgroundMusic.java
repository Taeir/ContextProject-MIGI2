package nl.tudelft.contextproject.audio;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioSource;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.logging.Log;

/**
 * Class for playing/managing background music.
 */
public final class BackgroundMusic {
	private static final BackgroundMusic INSTANCE = new BackgroundMusic();
	
	private List<String> music = new ArrayList<String>();
	private int currentIndex = -1;
	private AudioNode current;
	
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
	 * Switches to the next song.
	 */
	public synchronized void next() {
		//If we have no music, we cannot play any.
		if (music.isEmpty()) return;
		
		//Get the next song
		int index = currentIndex + 1 % music.size();
		String nextName = music.get(index);
		
		//Create the AudioNode
		AudioNode nextAudioNode = new AudioNode(Main.getInstance().getAssetManager(), "Sound/Music/" + nextName, DataType.Stream);
		nextAudioNode.setPositional(false);
		nextAudioNode.setLooping(false);
		
		//Register for volume changes
		AudioManager.getInstance().registerVolume(nextAudioNode, SoundType.BACKGROUND_MUSIC);
		
		//Start the new music before stopping the old one for a smoother transition.
		nextAudioNode.play();
		
		if (current != null) {
			//Stop the old music, and unregister it from the AudioManager
			current.stop();
			AudioManager.getInstance().unregisterVolume(current, SoundType.BACKGROUND_MUSIC);
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
		} else {
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
	 * 		?? TODO
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
