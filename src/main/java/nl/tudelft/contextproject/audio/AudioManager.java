package nl.tudelft.contextproject.audio;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.WeakHashMap;

import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioRenderer;
import com.jme3.audio.Environment;
import com.jme3.audio.Listener;
import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioSource.Status;
import com.jme3.renderer.Camera;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.Observer;

import jmevr.app.VRApplication;

/**
 * Singleton class to manage audio.
 */
public final class AudioManager implements Observer {
	private static final AudioManager INSTANCE = new AudioManager();
	
	private HashMap<SoundType, Set<AudioNode>> sounds = new HashMap<>();
	private Listener listener;
	
	private AudioManager() {
		for (SoundType soundType : SoundType.values()) {
			//We use a HashSet with weak keys, to prevent us from keeping AudioNodes loaded unnecessarily.
			Set<AudioNode> set = Collections.newSetFromMap(new WeakHashMap<AudioNode, Boolean>());
			sounds.put(soundType, set);
		}
	}
	
	/**
	 * @return
	 * 		the AudioManager instance.
	 */
	public static AudioManager getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Initializes the AudioManager.
	 * 
	 * @param renderer
	 * 		the audio renderer to apply the cave effect to
	 * @param listener
	 * 		the listener to use
	 */
	public void init(AudioRenderer renderer, Listener listener) {
		this.listener = listener;
		if (renderer != null) {
			renderer.setEnvironment(Environment.Cavern);
		}
		
		SoundType.BACKGROUND_MUSIC.setGain(2f);
		SoundType.EFFECT.setGain(6f);
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

		Set<AudioNode> set = sounds.get(soundType);
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

		synchronized (set) {
			set.remove(audioNode);
		}
	}
	
	/**
	 * Activates the reverb effect on the AudioNode, which will make it sound more "cavern" like.
	 * 
	 * @param audioNode
	 * 		the AudioNode
	 * 
	 * @throws IllegalArgumentException
	 * 		if the given AudioNode is not positional.
	 */
	public void addCaveFeel(AudioNode audioNode) {
		if (!audioNode.isPositional()) {
			throw new IllegalArgumentException("Cave feel can only be added to positional audio nodes! [AudioNode=" + audioNode.getName() + "]");
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
		Set<AudioNode> set = sounds.get(soundType);

		synchronized (set) {
			for (AudioNode an : set) {
				an.setVolume(soundType.getGain());
			}
		}
	}
	
	@Override
	public void update(float tpf) {
		Camera cam;
		if (VRApplication.getVRViewManager() != null) {
			cam = VRApplication.getVRViewManager().getCamLeft();
		} else {
			cam = Main.getInstance().getCamera();
		}
		
		listener.setLocation(cam.getLocation());
		listener.setRotation(cam.getRotation());
	}
	

	/**
	 * Creates a new positional AudioNode with the given source.
	 * 
	 * @param location
	 * 		the location of the audio file
	 * @return
	 * 		the newly created AudioNode
	 */
	public static AudioNode newPositionalSoundEffect(String location) {
		AudioNode audioNode = new AudioNode(Main.getInstance().getAssetManager(), location, DataType.Buffer);
		audioNode.setPositional(true);
		audioNode.setRefDistance(0.8f);
		AudioManager.getInstance().registerVolume(audioNode, SoundType.EFFECT);
		
		return audioNode;
	}
	
	/**
	 * Ensures that the given audio node is playing, starting it if it has stopped.
	 * 
	 * @param node
	 * 		the AudioNode to play
	 */
	public static void ensurePlaying(AudioNode node) {
		if (node != null && node.getStatus() != Status.Playing) {
			Main.getInstance().enqueue(node::play);
		}
	}
	
	/**
	 * Stops the given AudioNode from playing.
	 * 
	 * @param node
	 * 		the audio node to stop
	 */
	public static void stop(AudioNode node) {
		if (node != null && node.getStatus() != Status.Stopped) {
			Main.getInstance().enqueue(node::stop);
		}
	}
}
