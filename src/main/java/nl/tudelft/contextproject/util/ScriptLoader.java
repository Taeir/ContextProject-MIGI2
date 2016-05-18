package nl.tudelft.contextproject.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;

import nl.tudelft.contextproject.model.TickListener;

public class ScriptLoader {
	
	private static URLClassLoader cl;
	private static final String PATH = "scripts/";
	
	private static void setup() throws MalformedURLException {
		if (cl != null) return;
		URL uri;
		uri = (new File(PATH)).toURI().toURL();
		URL[] uris = {uri};
	    cl = new URLClassLoader(uris);
	}
	
	public static TickListener getTickListener(String name) throws ClassNotFoundException, ClassCastException {
		try {
			setup();
		} catch (MalformedURLException e1) {
			throw new ClassNotFoundException("The script folder '" + PATH + "' is invalid.");
		}
		Class<?> c = cl.loadClass(name);
		if (TickListener.class.isAssignableFrom(c)) {
			try {
				return (TickListener) c.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new ClassCastException(e.getMessage());
			}
		}
		throw new ClassCastException("The loaded clas does not implement TickListener.");
	}
}
