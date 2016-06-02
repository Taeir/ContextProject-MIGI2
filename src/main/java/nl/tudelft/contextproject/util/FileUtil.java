package nl.tudelft.contextproject.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import nl.tudelft.contextproject.logging.Log;

import lombok.SneakyThrows;

/**
 * Class to make accessing files easier.
 * 
 * <p>This class contains methods to automatically extract files from the jar when they are needed.
 */
public final class FileUtil {
	static boolean jar;
	static String path;
	
	/**
	 * Private constructor for static utility class.
	 */
	private FileUtil() { };
	
	/**
	 * Initializes the FileManager.
	 */
	public static void init() {
		String feName = FileUtil.class.getName().replace('.', '/') + ".class";
		URL feUrl = FileUtil.class.getClassLoader().getResource(feName);
		jar = "jar".equals(feUrl.getProtocol());
		
		String path = feUrl.getPath().substring("file:/".length()).replace(feName, "");
		
		if (jar) {
			FileUtil.path = path.substring(0, path.length() - 2).replace("%20", " ");
		} else {
			FileUtil.path = path.replace('/', File.separatorChar).replace("%20", " ");
		}
	}
	
	/**
	 * Gets the file at the specified location.
	 * 
	 * <p>If the file exists outside of the jar, that file is returned.
	 * Otherwise, this method will search for any file inside the jar that is part of the specified
	 * location, and will extract it.
	 * 
	 * <p>The sneaky throws is there because the URISyntaxException cannot occur, as the URL given
	 * by getResource cannot be malformed.
	 * 
	 * @param location
	 * 		the fully qualified location (<code>/folder/filename.ext</code>)
	 * 
	 * @return
	 * 		a File object pointing to the file at the given location
	 */
	@SneakyThrows(URISyntaxException.class)
	public static File getFile(String location) {
		String jLocation;
		String fLocation;
		
		if (location.indexOf('/') == 0) {
			jLocation = location;
			fLocation = location.substring(1);
		} else {
			jLocation = "/" + location;
			fLocation = location;
		}
		
		if (!jar) {
			URL url = FileUtil.class.getResource(jLocation);
			if (url == null) return new File(fLocation);
			
			return new File(url.toURI());
		}

		//Check if we already have the file extracted
		String winLoc = fLocation.replace('/', File.separatorChar);
		File file = new File(winLoc);
		if (file.exists()) return file;

		//File has not yet been extracted, so we extract it.
		extractFromJar(fLocation);
		
		return file;
	}

	/**
	 * Extracts from the jar all files whose paths start with the given (f)location.
	 * 
	 * @param fLocation
	 * 		the location of the file(s) to extract
	 */
	private static void extractFromJar(String fLocation) {
		try (JarFile jar = new JarFile(path)) {
			Enumeration<JarEntry> enumEntries = jar.entries();

			while (enumEntries.hasMoreElements()) {
				JarEntry entry = enumEntries.nextElement();
				if (!entry.getName().startsWith(fLocation)) continue;
				
				extractFile(jar, entry);
			}
		} catch (IOException ex) {
			Log.getLog("FileManager").warning("Unable to extract file " + fLocation + " from jar!", ex);
		}
	}
	
	/**
	 * Extracts a single JarEntry from a jar file.
	 * 
	 * @param jar
	 * 		the jar file
	 * @param entry
	 * 		the entry to extract
	 * @throws IOException
	 * 		when writing or reading the file goes wrong
	 */
	private static void extractFile(JarFile jar, JarEntry entry) throws IOException {
		//Create the correct outputName, with fixed path separators.
		String outputName = entry.getName().replace('/', File.separatorChar);
		File outputFile = new File(outputName);
		
		Log.getLog("FileManager").fine("Extracting " + entry.getName() + " to " + outputFile.getAbsolutePath());
		
		if (outputFile.exists()) {
			return;
		} else if (entry.isDirectory()) {
			//Ensure that all parent folders are created
			outputFile.mkdirs();
			return;
		} else if (outputFile.getParentFile() != null) {
			//Ensure that the parent folder is created
			outputFile.getParentFile().mkdirs();
		}
		
		//Write the file to the new location
		try (FileOutputStream fos = new FileOutputStream(outputFile);
			InputStream is = jar.getInputStream(entry)) {
			while (is.available() > 0) {
				fos.write(is.read());
			}
		}
	}
	
	/**
	 * Returns a list of filenames from the directory at the given location.
	 * 
	 * <p>If the given location does not point to a directory, this method can return null.
	 * 
	 * <p>The sneaky throws is there because the URISyntaxException cannot occur, as the URL given
	 * by getResource cannot be malformed.
	 * 
	 * @param location
	 * 		the location of the directory to get the file names in
	 * @return
	 * 		an array of filenames in the directory at the given location
	 */
	@SneakyThrows(URISyntaxException.class)
	public static String[] getFileNames(String location) {
		if (!jar) {
			URL url = FileUtil.class.getResource(location);
			if (url == null) return null;

			return new File(url.toURI()).list();
		}
		
		if (location.indexOf('/') == 0) {
			location = location.substring(1);
		}
		
		//Ensure that location has a trailing /
		if (!location.isEmpty() && !location.endsWith("/")) {
			location = location + "/";
		}

		return listInJar(location);
	}

	/**
	 * Lists the names of all the files in a directory in the jar.
	 * 
	 * @param location
	 * 		the location of the directory
	 * @return
	 * 		an array of Strings, representing the names of the files in the directory
	 */
	private static String[] listInJar(String location) {
		List<String> names = new ArrayList<>();

		try (JarFile jar = new JarFile(path)) {
			Enumeration<JarEntry> enumEntries = jar.entries();

			while (enumEntries.hasMoreElements()) {
				JarEntry entry = enumEntries.nextElement();
				
				if (entry.getName().equals(location) || !entry.getName().startsWith(location)) continue;
				
				String name = entry.getName().substring(location.length());
				
				//Non directories that have a / in the name, are not direct children of this folder
				if (!entry.isDirectory() && name.contains("/")) continue;
				names.add(name);
			}

		} catch (IOException ex) {
			Log.getLog("FileManager").warning("Unable to retrieve files in " + location + " from jar!", ex);
		}
		
		return names.toArray(new String[names.size()]);
	}
	
	/**
	 * Method only used by testing.
	 * 
	 * @param jar
	 * 		the value to set for jar
	 */
	static void setJar(boolean jar) {
		FileUtil.jar = jar;
	}
	
	/**
	 * Method only used by testing.
	 * 
	 * @param path
	 * 		the value to set for path
	 */
	static void setPath(String path) {
		FileUtil.path = path;
	}
}
