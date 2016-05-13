package nl.tudelft.contextproject.audio;

/**
 * Enum for the different sound types.
 */
public enum SoundType {
	/**
	 * Sound effects (e.g. pickup/interaction noises)
	 */
	EFFECT,
	
	/**
	 * Background music.
	 */
	BACKGROUND_MUSIC,
	
	/**
	 * Ambient sounds (e.g. cave sounds).
	 */
	AMBIENT;
	
	private float gain;
	
	/**
	 * Gets the gain for sounds of this type.
	 * 
	 * <p>A gain of 0 is muted, 2 is twice as loud.
	 * 
	 * @return
	 * 		the gain of sounds of this type
	 */
	public float getGain() {
		return gain;
	}
	
	/**
	 * Sets the gain of sounds of this type.
	 * 
	 * <p>A gain of 0 is muted, 2 is twice as loud.
	 * 
	 * @param gain
	 * 		the new value for the gain
	 */
	public void setGain(float gain) {
		//Gain must be >= 0
		this.gain = Math.max(0, gain);
		
		//Notify the AudioManager of the volume change.
		AudioManager.getInstance().updateVolume(this);
	}
}