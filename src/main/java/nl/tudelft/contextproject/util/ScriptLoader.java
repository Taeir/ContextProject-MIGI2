package nl.tudelft.contextproject.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Class that loads objects from external .class files.
 */
public class ScriptLoader {	
	private URLClassLoader cl;
	
	/**
	 * Constructor for ScriptLoader.
	 *
	 * @param path
	 * 		the folder in which the scripts are located
	 * @throws ScriptLoaderException
	 * 		when the given path is an invalid location
	 */
	public ScriptLoader(String path) throws ScriptLoaderException {
		try {
			URL url = (new File(path.replace("%20", " "))).toURI().toURL();
			URL[] urls = {url};
		    cl = new URLClassLoader(urls);
		} catch (MalformedURLException e) {
			throw new ScriptLoaderException("The script folder '" + path + "' is invalid.");
		}
	}
	
	/**
	 * Create an instance of the specified object from a given location.
	 *
	 * @param <T>
	 *     	the type of object to return
	 * @param path
	 * 		the path of the folder in which the file is located
	 * @param name
	 * 		the name of the classFile to load the class from
	 * @param type
	 * 		the type of object to load
	 * @return
	 * 		an instance of the class loaded from the file
	 * @throws ScriptLoaderException
	 * 		when something goes wrong while loading/casting the instance
	 */
	public static <T> T getInstanceFrom(String path, String name, Class<T> type) throws ScriptLoaderException {
		ScriptLoader sl = new ScriptLoader(path);
		return sl.getInstanceOf(name, type);
	}
	
	/**
	 * Create an instance of the specified class with a given type.
	 *
	 * <p>Suppresses warning for unchecked cast that is checked.
	 *
	 * @param <T>
	 *     	the type of the returned object
	 * @param name
	 * 		the name of the class to load
	 * @param type
	 * 		the type of the returned object
	 * @return
	 * 		an instance of the class
	 * @throws ScriptLoaderException 
	 * 		when the class is not found, something went wrong
	 * 		when instantiating the object or the object couldn't be casted
	 */
	@SuppressWarnings("unchecked")
	public <T> T getInstanceOf(String name, Class<T> type) throws ScriptLoaderException {
		try {
			Class<?> c = cl.loadClass(name);
			if (type.isAssignableFrom(c)) {
				return (T) c.newInstance();
			} else {
				throw new ScriptLoaderException(name + " is not a subclass of " + type.getSimpleName() + ".");
			}
		} catch (ClassNotFoundException e) {
			throw new ScriptLoaderException(name + " is not a valid class file.");
		} catch (InstantiationException e) {
			throw new ScriptLoaderException("Something went wrong when instantiating the class: " + e.getMessage());
		} catch (IllegalAccessException e) {
			throw new ScriptLoaderException("Invalid access: " + e.getMessage());
		}
	}
}
