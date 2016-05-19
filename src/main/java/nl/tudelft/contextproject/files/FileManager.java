package nl.tudelft.contextproject.files;

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
 * Class to manage file access.
 * 
 * <p>This class contains methods to automatically extract files from the jar when they are needed.
 */
public final class FileManager {
	static boolean jar;
	static String path;
	
	/**
	 * Private constructor for static utility class.
	 */
	private FileManager() { };
	
	/**
	 * Initializes the FileManager.
	 */
	public static void init() {
		String feName = FileManager.class.getName().replace('.', '/') + ".class";
		URL feUrl = FileManager.class.getClassLoader().getResource(feName);
		jar = "jar".equals(feUrl.getProtocol());
		
		String path = feUrl.getPath().substring("file:/".length()).replace(feName, "");
		
		if (jar) {
			FileManager.path = path.substring(0, path.length() - 2).replace("%20", " ");
		} else {
			FileManager.path = path.replace('/', File.separatorChar).replace("%20", " ");
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
	 * 		a File object pointing to the file at the given location.
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
			URL url = FileManager.class.getResource(jLocation);
			if (url == null) return new File(fLocation);
			
			return new File(url.toURI());
		}
		
		//Check if we already have the file extracted
		String winLoc = fLocation.replace('/', File.separatorChar);
		File file = new File(winLoc);
		if (file.exists()) return file;

		//We need to extract this from the jar.
		try (JarFile jar = new JarFile(path)) {
			Enumeration<JarEntry> enumEntries = jar.entries();
			while (enumEntries.hasMoreElements()) {
				JarEntry entry = enumEntries.nextElement();
				if (!entry.getName().startsWith(fLocation)) continue;
				
				//Create the correct outputName, with fixed path separators.
				String outputName = entry.getName().replace('/', File.separatorChar);
				File outputFile = new File(outputName);
				
				Log.getLog("FileManager").fine("Extracting " + entry.getName() + " to " + outputFile.getAbsolutePath());
				
				if (outputFile.exists()) {
					//Outputfile already exists
					continue;
				} else if (entry.isDirectory()) {
					//This is a directory, so ensure that all folders are created
					outputFile.mkdirs();
					continue;
				} else if (outputFile.getParentFile() != null) {
					//Ensure that the parent folder is created
					outputFile.getParentFile().mkdirs();
				}
				
				//Write the file to the new location
				FileOutputStream fos = new FileOutputStream(outputFile);
				InputStream is = jar.getInputStream(entry);
				while (is.available() > 0) {  // write contents of 'is' to 'fos'
					fos.write(is.read());
				}
				fos.close();
				is.close();
			}
		} catch (IOException ex) {
			Log.getLog("FileManager").warning("Unable to extract file " + fLocation + " from jar!", ex);
		}
		
		return file;
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
	 * 		the location of the directory to get the file names in.
	 * 
	 * @return
	 * 		an array of filenames in the directory at the given location.
	 */
	@SneakyThrows(URISyntaxException.class)
	public static String[] getFileNames(String location) {
		if (!jar) {
			URL url = FileManager.class.getResource(location);
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
		
		//Construct the list of names
		List<String> names = new ArrayList<>();
		try (JarFile jar = new JarFile(path)) {
			Enumeration<JarEntry> enumEntries = jar.entries();
			while (enumEntries.hasMoreElements()) {
				JarEntry entry = enumEntries.nextElement();
				
				if (entry.getName().equals(location)) continue;
				if (!entry.getName().startsWith(location)) continue;
				
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
		FileManager.jar = jar;
	}
	
	/**
	 * Method only used by testing.
	 * 
	 * @param path
	 * 		the value to set for path
	 */
	static void setPath(String path) {
		FileManager.path = path;
	}
}
