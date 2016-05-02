package nl.tudelft.contextproject.audio;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.WeakHashMap;

import com.jme3.audio.AudioNode;
import com.jme3.audio.Environment;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.logging.Log;

/**
 * Singleton class to manage audio.
 */
public final class AudioManager {
	private static final AudioManager INSTANCE = new AudioManager();
	
	private HashMap<SoundType, Set<AudioNode>> sounds = new HashMap<>();
	
	private AudioManager() {}
	
	/**
	 * @return
	 * 		the AudioManager instance.
	 */
	public static AudioManager getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Initializes the AudioManager.
	 */
	public void init() {
		//Set the environment to cavern, for effects.
		Main.getInstance().getAudioRenderer().setEnvironment(Environment.Cavern);
	}
	
	/**
	 * Registers the given AudioNode for the given SoundType. This sets its volume to the correct
	 * value, and will ensure it gets updated when the volume changes.
	 * 
	 * <p>The AudioManager will keep a weak reference to the AudioNode, so unregistering is not
	 * necessary (but still helpful).
	 * 
	 * @param audioNode
	 * 		the audioNode to register
	 * @param soundType
	 * 		the SoundType to register under
	 */
	public void registerVolume(AudioNode audioNode, SoundType soundType) {
		audioNode.setVolume(soundType.getGain());
		
		//Get or create the set of AudioNodes for the given SoundType.
		//We use a HashSet with weak keys, to prevent us from keeping AudioNodes loaded unnecessarily.
		Set<AudioNode> set = sounds.computeIfAbsent(soundType, s -> Collections.newSetFromMap(new WeakHashMap<AudioNode, Boolean>()));
		synchronized (set) {
			set.add(audioNode);
		}
	}
	
	/**
	 * Unregisters the given AudioNode from the AudioManager. This means that its volume will no
	 * longer be updated.
	 * 
	 * @param audioNode
	 * 		the AudioNode to unregister
	 * @param soundType
	 * 		the SoundType the AudioNode is registered under
	 */
	public void unregisterVolume(AudioNode audioNode, SoundType soundType) {
		Set<AudioNode> set = sounds.get(soundType);
		if (set == null) return;
		
		//Remove the AudioNode from the set
		synchronized (set) {
			set.remove(audioNode);
		}
	}
	
	/**
	 * Activates the reverb effect on the AudioNode, which will make it sound more "cavern" like.
	 * Has no effect if the AudioNode is not positional.
	 * 
	 * @param audioNode
	 * 		the AudioNode
	 */
	public void addCaveFeel(AudioNode audioNode) {
		if (!audioNode.isPositional()) {
			Log.getLog("Audio").warning("Enabling reverb on non-positional audioNode has no effect. [AudioNode=" + audioNode.getName() + "]");
		}
		
		audioNode.setReverbEnabled(true);
	}
	
	/**
	 * Pauses all audio playback.
	 */
	public void pauseAllAudio() {
		Main.getInstance().getAudioRenderer().pauseAll();
	}
	
	/**
	 * Resumes all audio playback.
	 */
	public void resumeAllAudio() {
		Main.getInstance().getAudioRenderer().resumeAll();
	}
	
	/**
	 * Indicates an update to the volume of the given SoundType.
	 * 
	 * <p>The AudioManager will change the volume of all registered AudioNodes.
	 * 
	 * @param soundType
	 * 		the soundType whose volume was updated
	 */
	public void updateVolume(SoundType soundType) {
		//Get all sounds of the given SoundType
		Set<AudioNode> set = sounds.get(soundType);
		if (set == null) return;
		
		//Update the volume of all items in the set.
		synchronized (set) {
			for (AudioNode an : set) {
				an.setVolume(soundType.getGain());
			}
		}
	}
}
