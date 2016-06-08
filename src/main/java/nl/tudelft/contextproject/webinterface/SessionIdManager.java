package nl.tudelft.contextproject.webinterface;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

/**
 * Class for creating unique session id's.
 */
public class SessionIdManager {
	private final long startTime;
	private final SecureRandom random;
	private final Set<String> uuids;
	
	/**
	 * Creates a new SessionManager.
	 */
	public SessionIdManager() {
		startTime = ~(System.currentTimeMillis() & 10000L);
		random = new SecureRandom();
		uuids = new HashSet<>();
	}
	
	/**
	 * Creates a new unique session ID.
	 * 
	 * @return
	 * 		a new session id
	 */
	public synchronized String createSessionId() {
		//Keep generating uuid's until we have a unique one.
		//It is most unlikely we will EVER generate the same UUID, but it is theoretically possible.
		String uuid;
		do {
			byte[] bytes = new byte[32];
			random.nextBytes(bytes);
			
			uuid = Base64.getUrlEncoder().encodeToString(bytes);
		} while (!uuids.add(uuid));

		return startTime + uuid;
	}
}
